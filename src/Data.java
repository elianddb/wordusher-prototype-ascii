public class Data {
    public static final int OUT_OF_BOUNDS = -1;
    public static final int DEF_MIN_X = 0;
    public static final int DEF_MIN_Y = 0;

    public static final char NOT_FOUND_CHAR = '?';
    public static final char STYLE_EMPTY_CHAR = '.';
    public static final char EMPTY_CHAR = ' ';

    public static final String EMPTY_STR = " ";

    public enum Type {
        NONE,
        SOLID,
        G_SOLID,
        ENTITY,
        G_ENTITY,   // Entity that serves as a place-holder. G = ghost.
        MOVABLE;

        private static boolean showAll = false;

        public boolean isBuffered() {
            return this == G_ENTITY;
        }

        public boolean isSolid() {
            return switch(this) {
                case SOLID, ENTITY, MOVABLE -> true;
                default -> false;
            };
        }

        public boolean isFixed() {
            return switch(this) {
                case SOLID, ENTITY, G_ENTITY, G_SOLID, NONE -> true;
                default -> false;
            };
        }

        public boolean isFixedSolid() {
            return isFixed() && isSolid();
        }

        public boolean isMovable() {
            return !isFixed();
        }

        public boolean isGhost() {
            return switch(this) {
                case G_SOLID, G_ENTITY -> true;
                default -> false;
            };
        }

        public boolean isNull() {
            return this == NONE;
        }

        public boolean isRendered() {
            if (showAll)
                return true;

            return switch(this) {
                case SOLID, ENTITY, MOVABLE -> true;
                default -> false;
            };
        }

        public static boolean getShowAll() {
            return showAll;
        }

        public static void setShowAll(boolean s) {
            showAll = s;
        }
    }

    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        NONE;

        public static Direction parseDirection(int x, int y) {
            Direction direc = NONE;
            if (x > DEF_MIN_X) {
                direc = RIGHT;
            } else if (x < DEF_MIN_X) {
                direc = LEFT;
            } else if (y < DEF_MIN_Y) {
                direc = UP;
            } else if (y > DEF_MIN_Y) {
                direc = DOWN;
            }

            return direc;
        }

        public static Direction parseDirection(char c) {
            return switch (c) {
                    case 'w', 'W' -> UP;
                    case 's', 'S' -> DOWN;
                    case 'a', 'A' -> LEFT;
                    case 'd', 'D' -> RIGHT;
                    default -> NONE;
            };
        }
    }

    /**
     * Data necessary to keep track of an object.
     */
    public static abstract class Essential {
        private char actor;
        private Type type;
        private int y;
        private int x;
        private Scene scene;

        Essential(char c, Type t, int x, int y) {
            actor = c;
            type = t;
            this.x = x;
            this.y = y;
        }

        public char getActor() {
            return actor;
        }

        public Type getType() {
            return type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Scene getScene() {
            return scene;
        }

        public void setActor(char c) {
            actor = c;
        }

        public void setType(Type t) {
            type = t;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setXY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        protected void setScene(Scene sc) {
            scene = sc;
        }

        public boolean inScene() {
            return scene != null;
        }

        public int parseX(Direction direc) {
            return switch (direc) {
                case RIGHT -> x + 1;
                case LEFT -> x - 1;
                default -> x;
            };
        }

        public int parseY(Direction direc) {
            return switch (direc) {
                case UP -> y - 1;
                case DOWN -> y + 1;
                default -> y;
            };
        }
    }

    /**
     * Template for controlling an object through the command prompt.
     */
    public static abstract class CommandsModel<T> {
        private boolean exit;
        protected T control;

        public static final int HEAD = 0;
        public static final int ARG_0 = 1;
        public static final int ARG_1 = 2;

        CommandsModel(T obj) {
            exit = false;
            control = obj;
        }

        abstract boolean execute(String cmd);

        public boolean exited() {
            return exit;
        }

        public void setExit(boolean set) {
            exit = set;
        }
    }
}
