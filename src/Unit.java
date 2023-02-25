public class Unit extends Data.Essential {
    public final static char G_CHAR = 'U';

    Unit() {
        this(Data.NOT_FOUND_CHAR, Data.Type.NONE, Data.OUT_OF_BOUNDS, Data.OUT_OF_BOUNDS, null);
    }

    Unit(int x, int y, Scene sc) {
        this(G_CHAR, Data.Type.G_SOLID, x, y, sc);
    }

    Unit(char c, Data.Type t, int x, int y, Scene sc) {
        super(c, t, x, y);
        setScene(sc);
    }
}
