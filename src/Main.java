import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player('P');
        Player.Commands cmds = new Player.Commands(p1);

        Movable obj = new Movable('O');
        Movable obj2 = new Movable('O');

        Entity solid = new Entity('#');

        Scene myScene = new Scene(10, 10, 0, 2);
        myScene.add(p1, 1, 1);
        myScene.add(obj, 2, 2);
        myScene.add(obj2, 3, 2);
        myScene.add(solid, 4, 4);

        WordBuilder word = new WordBuilder("Minecraft", myScene);

        Scanner kbd = new Scanner(System.in);
        while (!cmds.exited()) {
            System.out.println(myScene);
            cmds.execute(kbd.nextLine());
            if (word.isBuilt()) {
                System.out.println("YOU SOLVED THE WORD!");
            }
        }
    }
}
