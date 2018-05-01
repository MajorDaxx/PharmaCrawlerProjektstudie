/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComClass;

/**
 *
 * @author lasse
 */
public class PZN {

    private static int PZNlength = 8;

    private String pzn;

    public static PZN getPZN(int num) throws UnvalidPzn {
        return getPZN(Integer.toString(num));
    }

    public static PZN getPZN(String num) throws UnvalidPzn {
        //Wird wieder im Fertigen Programm eingeführt im Entwickeln hinderlich
        //if (num.length() > PZNlength||num.length() < PZNlength) {
        //    throw new UnvalidPzn();
        //} else {
        return new PZN(num);
        //}
    }

    private PZN(String num) {
        pzn = num;
    }

    @Override
    public String toString() {
        return pzn;
    }

    public static class UnvalidPzn extends Exception {

        String pzn;

        /**
         * genauer Spezifizierbar :P.
         *
         * @return
         */
        @Override
        public String getMessage() {
            return super.getMessage() + ": Die PZN hat ein ungültiges Format"; //To change body of generated methods, choose Tools | Templates.
        }

    }
}
