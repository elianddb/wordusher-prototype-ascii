import java.util.Random;

/**
 * Creates letters added to the passed scene, and places them in random locations. If
 * not added afterwards may interfere with items added later since they may have the
 * same coordinates. Can track if letters have been put in the correct order.
 */
public class WordBuilder {
    private final Letter[] letters;

    WordBuilder(String w, Scene sc) {
        letters = new Letter[w.length()];
        final int start = 0;
        for (int i = start; i < letters.length; ++i) {
            letters[i] = new Letter(w.charAt(i));
        }

        final int lastIndex = letters.length - 1;
        Random rand = new Random();
        for (int i = start; i < letters.length; ++i) {
            final int leftIndex = i - 1;
            if (leftIndex >= start) {
                letters[i].setLeft(letters[leftIndex]);
            }

            final int rightIndex = i + 1;
            if (rightIndex <= lastIndex) {
                letters[i].setRight(letters[rightIndex]);
            }

            int x;
            int y;
            do {
                x = rand.nextInt(sc.getMaxX());
                y = rand.nextInt(sc.getMaxY());
            } while (sc.isSolidAt(x, y));

            sc.add(letters[i], x, y);
        }
    }

    public boolean isBuilt() {
        boolean allMatch = true;
        final int start = 0;
        for (int i = start; i < letters.length && allMatch; ++i) {
            if (!letters[i].isMatched()) {
                allMatch = false;
            }
        }

        return allMatch;
    }

    public static class Letter extends Movable {
        private Letter left;
        private Letter right;

        Letter(char c) {
            this(c, null, null);
        }

        Letter(char c, Letter l, Letter r) {
            super(c);
            left = l;
            right = r;
        }

        public void setRight(Letter letter) {
            right = letter;
        }

        public void setLeft(Letter letter) {
            left = letter;
        }

        public boolean rightMatched() {
            return right == null || read(Data.Direction.RIGHT) == right;
        }

        public boolean leftMatched() {
            return left == null || read(Data.Direction.LEFT) == left;
        }

        public boolean isMatched() {
            return leftMatched() && rightMatched();
        }
    }
}
