/**
 *
 */
package battleship;

import util.I18n;

import java.util.Locale;

/**
 * The type Main.
 *
 * @author britoeabreu
 * @author adrianolopes
 * @author miguelgoulao
 */
public class Main {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args) {
        System.out.println("***  Battleship  ***");

        I18n.setLocale(new Locale("en"));
        Tasks.menu();
    }
}