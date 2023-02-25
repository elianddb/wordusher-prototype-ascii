public class Player extends Entity {
    private Entity gripped;

    Player(char c) {
        super(c);
    }

    public void grip(Data.Direction direc) {
        Entity grip = getScene().getEntityAt(parseX(direc), parseY(direc));
        if (!grip.getType().isFixed()) {
            gripped = grip;
        }
    }

    public void letGo() {
        if (isGripping()) {
            gripped = null;
        }
    }

    public boolean isGripping() {
        return gripped != null;
    }

    @Override
    public boolean move(int x, int y) {
        // Needs to be declared before player moves because if not done player appears
        // as if they have not moved.
        Data.Direction direc = parseDirection(x, y);

        // Need to make sure object is not in front of player, since that would be pushing.
        // If read(direc) is out of bounds that means gripped is not in front of player.
        boolean moved = false;
        if (!isGripping() || read(direc) == gripped) {  // pushes
            moved = super.move(x, y);
        } else if (gripped.read(direc) == this) {   // pulls
            moved = super.move(x, y);
            gripped.move(direc);
        } else if (!gripped.isBlocked(direc)){
            if (super.move(x, y)) {
                gripped.move(direc);
                moved = true;
            }
        }

        return moved;
    }

    public boolean move(char c) {
        return move(Data.Direction.parseDirection(c));
    }

    public static class Commands extends Data.CommandsModel<Player> {
        public static final char GRIP = 'g';
        public static final char LET_GO = 'l';
        public static final char TELEPORT = 't';
        public static final char EXIT = 'e';

        Commands(Player player) {
            super(player);
        }

        @Override
        public boolean execute(String cmd) {
            final int EMPTY = 0;
            if (cmd.trim().length() == EMPTY) {
                return false;
            }

            String[] parts = cmd.split(" ");
            final char head = parts[HEAD].charAt(0);
            switch(head) {
                case GRIP -> {
                    final int numOfParts = 2;
                    if (parts.length == numOfParts) {
                        control.grip(Data.Direction.parseDirection(parts[ARG_0].charAt(0)));
                    }
                }
                case LET_GO -> {
                    final int numOfParts = 1;
                    if (parts.length == numOfParts) {
                        control.letGo();
                    }
                }
                case TELEPORT -> {
                    final int numOfParts = 3;
                    if (parts.length == numOfParts) {
                        try {
                            control.move(Integer.parseInt(parts[ARG_0]), Integer.parseInt(parts[ARG_1]));
                        } catch(NumberFormatException err) {
                            System.out.println("Error converting coordinates to integers.");
                        }
                    }
                }
                case EXIT -> {
                    final int numOfParts = 1;
                    if (parts.length == numOfParts) {
                        setExit(true);
                    }
                }
                default -> {
                    if (control.move(head)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
