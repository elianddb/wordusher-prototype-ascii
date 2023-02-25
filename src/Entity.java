public class Entity extends Data.Essential {
    public static final char G_CHAR = 'E';

    public static final Entity NO_BUFFER = new Entity();

    /**
     * Each time an entity moves it enters a square that had a
     * ghost entity. This g_entity is temporarily stored in the entity's
     * buffer to later be released when it moves. It lets objects know
     * the square is available again.
     */
    private Entity buffer;

    /**
     * Alternative to null entity, primarily for G_ENTITY types' buffers and out of bounds reach.
     */
    Entity() {
        super(G_CHAR, Data.Type.NONE, Data.OUT_OF_BOUNDS, Data.OUT_OF_BOUNDS);
    }

    /**
     * Normal type of entities.
     *
     * @param c actor
     */
    Entity(char c) {
        this(c, Data.Type.ENTITY, Data.OUT_OF_BOUNDS, Data.OUT_OF_BOUNDS);
    }

    /**
     * Static entities that are meant to serve as place-holders in layers.
     *
     * @param x coord
     * @param y coord
     * @param sc scene
     */
    protected Entity(int x, int y, Scene sc) {
        super(Data.NOT_FOUND_CHAR, Data.Type.G_ENTITY, x, y);
        setScene(sc);
        buffer = NO_BUFFER;
    }

    /**
     * Create new type of Entity.
     *
     * @param c actor
     * @param t type
     * @param x coord
     * @param y coord
     */
    protected Entity(char c, Data.Type t, int x, int y) {
        super(c, t, x, y);
    }

    public boolean hasBuffer() {
        return buffer.getType().isBuffered();
    }

    protected void release() {
        if (hasBuffer()) {
            getScene().setEntityAt(getX(), getY(), buffer);
        } else {
            if (buffer.getType().isFixed()) {
                throw new IllegalCallerException("Entity is static, unable to release.");
            }
            // Something went wrong in order for this type to get into the buffer.
            throw new IllegalCallerException("Entity buffer is incompatible for release.");
        }
    }

    protected void hold(Entity ent) {
        if (ent.getScene() == this.getScene()) {
            if (ent.getType().isBuffered()) {
                buffer = ent;
            } else {
                throw new IllegalArgumentException("Unable to hold type in buffer.");
            }
        } else {
            throw new IllegalArgumentException("Entity is not in same scene to hold buffer.");
        }
    }

    public Data.Direction parseDirection(int x, int y) {
        return Data.Direction.parseDirection(x - getX(), y - getY());
    }

    /**
     * Instantly moves entity with no pre-check.
     *
     * @param x coord
     * @param y coord
     */
    public void force(int x, int y) {
        release();
        hold(getScene().getEntityAt(x, y));
        getScene().setEntityAt(x, y, this);
    }

    /**
     * Additionally handles interactions between entities and enforces the scene's bounds.
     *
     * @param x coord
     * @param y coord
     */
    public boolean move(int x, int y) {
        boolean moved = false;

        if (!getScene().inRange(x, y)) {
            return false;
        }

        Entity ref = getScene().getEntityAt(x, y);
        if (ref.getType().isMovable()) {
            ref.move(parseDirection(x, y));
        }

        if (getScene().getEntityAt(x, y).getType().isGhost()) {
            force(x, y);
            moved = true;
        }

        return moved;
    }

    public boolean move(Data.Direction direc) {
        return move(parseX(direc), parseY(direc));
    }

    public Entity read(Data.Direction direc) {
        return getScene().getEntityAt(parseX(direc), parseY(direc));
    }

    /**
     * Search for first instance of ghost, then the first instance of a fixed solid. If the ghost comes before
     * the fixed solid the direction is not blocked.
     *
     * @param direc direction
     * @return is blocked
     */
    public boolean isBlocked(Data.Direction direc) {
        // Find first instance of a ghost and a fixed solid.
        Entity findGhost = this.read(direc);
        while (!findGhost.getType().isNull() && !findGhost.getType().isGhost()) {
            findGhost = findGhost.read(direc);
        }

        Entity findFS = this.read(direc);
        while (!findFS.getType().isNull() && !findFS.getType().isFixedSolid()) {
            findFS = findFS.read(direc);
        }

        // Decide whether the object is blocked in direction.
        if (findGhost.getType().isNull()) {
            return true;
        } else if (findFS.getType().isNull()) {
            return false;
        } else {
            switch (direc) {
                case RIGHT -> {
                    if (findGhost.getX() < findFS.getX()) {
                        return false;
                    }
                }
                case LEFT -> {
                    if (findGhost.getX() > findFS.getX()) {
                        return false;
                    }
                }
                case UP -> {
                    if (findGhost.getY() > findFS.getY()) {
                        return false;
                    }
                }
                case DOWN -> {
                    if (findGhost.getY() < findFS.getY()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Scene getScene() {
        if (inScene()) {
            return super.getScene();
        } else {
            throw new IllegalCallerException("Entity must be attached to a scene.");
        }
    }
}
