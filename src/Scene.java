import java.lang.StringBuilder;

public class Scene {
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    private final int height;
    private final int length;

    private final Display display;
    private final Unit[][] unitLayer;
    private final Entity[][] entityLayer;

    Scene(int h, int l, int rs, int cs) {
        height = h;
        length = l;

        minX = Data.DEF_MIN_X;
        minY = Data.DEF_MIN_Y;
        maxX = height - 1;
        maxY = length - 1;

        unitLayer = new Unit[h][l];
        entityLayer = new Entity[h][l];
        final int start = 0;
        for (int row = start; row < h; ++row) {
            for (int i = start; i < l; ++i) {
                unitLayer[row][i] = new Unit(i, row, this);
                entityLayer[row][i] = new Entity(i, row, this);
            }
        }

        display = new Display(rs, cs);
        display.render(unitLayer);
        display.render(entityLayer);
    }

    protected Unit getUnitAt(int x, int y) {
        if (inRange(x, y)) {
            return unitLayer[y][x];
        } else {
            return new Unit();
        }
    }

    protected Entity getEntityAt(int x, int y) {
        if (inRange(x, y)) {
            return entityLayer[y][x];
        } else {
            // Lets the calling function know the entity being looked for is out of bounds.
            return new Entity();
        }
    }

    protected void setEntityAt(int x, int y, Entity ent) {
        entityLayer[y][x] = ent;
        ent.setXY(x, y);
        display.render(ent);
    }

    public void add(Entity ent, int x, int y) {
        if (!ent.inScene()) {
            ent.setScene(this);
        } else {
            throw new IllegalArgumentException("Entity is already bound to another scene.");
        }

        ent.hold(getEntityAt(x, y));
        setEntityAt(x, y, ent);
    }

    public boolean remove(Entity ent) {
        boolean found = false;
        if (getEntityAt(ent.getX(), ent.getY()) == ent) {
            found = true;
            ent.release();
            ent.setScene(null);
            display.render(getEntityAt(ent.getX(), ent.getY()));
        }

        return found;
    }

    public boolean inRange(int x, int y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public boolean isSolidAt(int x, int y) {
        return getUnitAt(x, y).getType().isSolid() || getEntityAt(x, y).getType().isSolid();
    }

    public void reload() {
        display.render(unitLayer);
        display.render(entityLayer);
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    @Override
    public String toString() {
        return display.toString();
    }

    private class Display {
        private String render;

        private final int rowDist;
        private final int colDist;

        private final int rowLen;

        Display(int r, int c) {
            rowDist = r;
            colDist = c;

            // Create the structure where renders will be placed.
            StringBuilder strBld = new StringBuilder();
            final int start = 0;
            for (int row = start; row < height; ++row) {
                for (int i = start; i < length; ++i) {
                    strBld.append(Data.STYLE_EMPTY_CHAR);
                    // Assures a new line character is placed at the end of a row.
                    strBld.append(i < maxX ? Data.EMPTY_STR.repeat(colDist) : '\n');
                }
                strBld.append("\n".repeat(rowDist));
            }

            render = strBld.toString();

            // Number of spacers in-between columns in each row.
            final int colSpcs = length - 1;
            // +1 accounts for the new line character.
            rowLen = length + 1 + colSpcs * colDist;
        }

        /**
         * Coordinates are converted to an index in the render string.
         *
         * @param x coord
         * @param y coord
         * @return index
         */
        public int toIndex(int x, int y) {
            final int rowJump = rowLen + rowDist;
            // +1 accounts for the first object in the row.
            final int colJump = colDist + 1;

            return y * rowJump + x * colJump;
        }

        public void clear() {
            final int start = 0;
            StringBuilder strBld = new StringBuilder(render);
            for (int row = start; row < height; ++row) {
                for (int i = start; i < length; ++i) {
                    strBld.setCharAt(toIndex(i, row), Data.EMPTY_CHAR);
                }
            }

            render = strBld.toString();
        }

        /**
         * Add layer to render, will overwrite existing areas in render
         * with the new layer.
         */
        public void render(Data.Essential[][] layer) {
            final int start = 0;
            StringBuilder strBld = new StringBuilder(render);
            for (int row = start; row < height; ++row)
                for (int i = start; i < length; ++i) {
                    if (layer[row][i].getType().isRendered())
                        strBld.setCharAt(toIndex(i, row), layer[row][i].getActor());
                }

            render = strBld.toString();
        }

        public void render(Data.Essential data) {
            StringBuilder strBld = new StringBuilder(render);
            if (data.getType().isRendered()) {
                strBld.setCharAt(toIndex(data.getX(), data.getY()), data.getActor());
            } else {
                strBld.setCharAt(toIndex(data.getX(), data.getY()), Data.STYLE_EMPTY_CHAR);
            }

            render = strBld.toString();
        }

        @Override
        public String toString() {
            return render;
        }
    }
}
