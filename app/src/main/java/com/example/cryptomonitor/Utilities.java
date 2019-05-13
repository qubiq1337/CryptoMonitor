package com.example.cryptomonitor;

/**
 * This is a collection of static constants and methods used
 * in many calculations.
 *
 * @author Silent Hussar
 *
 */
public class Utilities {
//<<<< some stuff deleted >>>>
    /**
     * This method returns x rounded to n digits
     */
    public static double round(double x, int n) {
        return Double.parseDouble(roundStr(x, n));
    }
    /**
     * Rounds a double number to n digits and returns a String.
     * Depending on the last unused digit, rounding is done up or down.
     *
     * @param x
     * @param n
     * @return
     */
    public static String roundStr(double x, int n) {
        String strRoundedX = roundStr(x, n, 0);
        return strRoundedX;
    }
    /**
     * This method is recursive; it rounds down a double number
     * to n digits and returns a String.
     *
     * The recursive call is needed when the (n+1)th digit is 5 or greater.
     * In this case, a small delta is added to the input number and rounding
     * down of the modified number is done once again. (This is only necessary
     * when carry-ons occur, but there is no way to easily figure it out.)
     *
     *  Test case: rounding number 129999 to 4 digits.
     *  The program first ends in 129900 which is imprecise; then it
     *  adds 50 to the input because the 5-th digit is >= 5.
     *  On the recursive call, rounding down is done on 129999 + 50 = 130049;
     *  thus we get more precise result 130000.
     *
     * @param x
     * @param n
     * @param rcnt - recursion counter (0 or 1)
     * @return
     */
    private static String roundStr(double x, int n, int rcnt) {
        String nonZeroes = "123456789";
        String allZeroes = "000000000000000000";
// convert input to string
        String strX = Double.toString(x);
// remove difference between 'E' and 'e'
        strX = strX.toLowerCase();
// this will be the rounded output
        String strRoundedX = "";
        boolean gotDot = false;
        boolean gotNZ = false;
        int count = 0; // digit counter
// parse the input number in string representation
        for (int i=0; i<strX.length(); i++) {
// current character
            char ch = strX.charAt(i);
            if (ch != 'e') {
                if (!gotNZ && nonZeroes.indexOf(ch) >= 0)
                    gotNZ = true; // a non-zero digit found
                if (ch == '.')
                    gotDot = true; // decimal point found
                if (ch != '-' && ch != '+' && ch != '.'  // do not count these characters
                        && !(gotDot && ch == '0' && !gotNZ) // do not count leading zeroes in numbers < 1.0
                ) {
                    count++; // count digits only
                }
                if (!gotNZ && ch == '0' && i <= 1)
                    count--; // ignore the leading zero (possibly with the minus)
                if (count > n) {
// now have parsed sufficient number of digits
                    int idxE = strX.indexOf('e');
                    if (!gotDot) {
// add trailing zeroes if necessary
                        int countOfZeroes;
                        int idxDot = strX.indexOf('.');
                        if (idxDot > 0) {
                            countOfZeroes = idxDot - i;
                        } else {
                            if (idxE >= 0)
                                countOfZeroes = idxE - i;
                            else
                                countOfZeroes = strX.length() - i;
                        }
                        strRoundedX = strRoundedX + allZeroes.substring(0, countOfZeroes);
                    }
// append the exponent part, if any
                    if (idxE >= 0)
                        strRoundedX = strRoundedX + strX.substring(idxE);
// explore the next digit, if any
                    if (ch != '.') {
                        int nextDigit = Integer.parseInt(ch + "");
// check if rounding up is needed indeed
                        if (nextDigit >= 5 && rcnt == 0) {
                            String extra = "";
                            for (int j=0; j<strX.length(); j++) {
                                char chj = strX.charAt(j);
                                if (j < i) {
// replace non-zeroes with leading zeroes
                                    if (nonZeroes.indexOf(chj) >= 0)
                                        extra = extra + '0';
                                    else
                                        extra = extra + chj;
                                } else if (j == i)
                                    extra = extra + '5';
                                else
                                    extra = extra + chj;
                            }
                            double delta = Double.parseDouble(extra);
                            double x1 = x + delta; // this results in rounding up
//System.out.println("### recursive call on  x1 = " + x1
//+ "\n\tdelta = " + delta + "  extra = " + extra );
                            strRoundedX = roundStr(x1, n, rcnt+1); //#### recursion ###
                        }
                    }
                    break;
                } else
                    strRoundedX = strRoundedX + ch;
            } else {
// exponent found; append it
                strRoundedX = strRoundedX + strX.substring(i);
                break;
            }
        }
// remove the trailing single dot, if any
        if (strRoundedX.endsWith("."))
            strRoundedX = strRoundedX.substring (0, strRoundedX.length()-1);
        return strRoundedX;
    }
}
