package name.marcelomorales.cantador;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Rule-based formatter.
 *
 * @author Marcelo Morales
 */
public class Cantador {

    /**
     * Default constructor.
     *
     * @param rulesSpec curently unused
     */
    public Cantador(String rulesSpec) {
        // TODO: parsear rulesSpec segun lenguaje
        rules = new LinkedList<Rule>();
        rules.add(new Rule(new BigInteger("0"), "cero", false, ""));
        rules.add(new Rule(new BigInteger("1"), "uno", false, ""));
        rules.add(new Rule(new BigInteger("2"), "dos", false, ""));
        rules.add(new Rule(new BigInteger("3"), "tres", false, ""));
        rules.add(new Rule(new BigInteger("4"), "cuatro", false, ""));
        rules.add(new Rule(new BigInteger("5"), "cinco", false, ""));
        rules.add(new Rule(new BigInteger("6"), "seis", false, ""));
        rules.add(new Rule(new BigInteger("7"), "siete", false, ""));
        rules.add(new Rule(new BigInteger("8"), "ocho", false, ""));
        rules.add(new Rule(new BigInteger("9"), "nueve", false, ""));
        rules.add(new Rule(new BigInteger("10"), "diez", false, ""));
        rules.add(new Rule(new BigInteger("11"), "once", false, ""));
        rules.add(new Rule(new BigInteger("12"), "doce", false, ""));
        rules.add(new Rule(new BigInteger("13"), "trece", false, ""));
        rules.add(new Rule(new BigInteger("14"), "catorce", false, ""));
        rules.add(new Rule(new BigInteger("15"), "quince", false, ""));
        rules.add(new Rule(new BigInteger("16"), "dieciséis", false, ""));
        rules.add(new Rule(new BigInteger("17"), "diecisiete", false, ""));
        rules.add(new Rule(new BigInteger("18"), "dieciocho", false, ""));
        rules.add(new Rule(new BigInteger("19"), "diecinueve", false, ""));
        rules.add(new Rule(new BigInteger("20"), "veinte", false, ""));
        rules.add(new Rule(new BigInteger("21"), "veintiuno", false, ""));
        rules.add(new Rule(new BigInteger("22"), "veintidos", false, ""));
        rules.add(new Rule(new BigInteger("23"), "veintitrés", false, ""));
        rules.add(new Rule(new BigInteger("24"), "veinticuatro", false, ""));
        rules.add(new Rule(new BigInteger("25"), "veinticinco", false, ""));
        rules.add(new Rule(new BigInteger("26"), "veintiséis", false, ""));
        rules.add(new Rule(new BigInteger("27"), "veintisiete", false, ""));
        rules.add(new Rule(new BigInteger("28"), "veintiocho", false, ""));
        rules.add(new Rule(new BigInteger("29"), "veintinueve", false, ""));
        rules.add(new Rule(new BigInteger("30"), "treinta", false, "treinta y {0}"));
        rules.add(new Rule(new BigInteger("40"), "cuarenta", false, "cuarenta y {0}"));
        rules.add(new Rule(new BigInteger("50"), "cincuenta", false, "cincuenta y {0}"));
        rules.add(new Rule(new BigInteger("60"), "sesenta", false, "sesenta y {0}"));
        rules.add(new Rule(new BigInteger("70"), "setenta", false, "setenta y {0}"));
        rules.add(new Rule(new BigInteger("80"), "ochenta", false, "ochenta y {0}"));
        rules.add(new Rule(new BigInteger("90"), "noventa", false, "noventa y {0}"));
        rules.add(new Rule(new BigInteger("100"), "cien", false, ""));
        rules.add(new Rule(new BigInteger("101"), "", false, "ciento {0}", new BigInteger("1")));
        rules.add(new Rule(new BigInteger("200"), "doscientos", false, "doscientos {0}"));
        rules.add(new Rule(new BigInteger("300"), "trescientos", false, "trescientos {0}"));
        rules.add(new Rule(new BigInteger("400"), "cuatrocientos", false, "cuatrocientos {0}"));
        rules.add(new Rule(new BigInteger("500"), "quinientos", false, "quinientos {0}"));
        rules.add(new Rule(new BigInteger("600"), "seiscientos", false, "seiscientos {0}"));
        rules.add(new Rule(new BigInteger("700"), "setecientos", false, "setecientos {0}"));
        rules.add(new Rule(new BigInteger("800"), "ochocientos", false, "ochocientos {0}"));
        rules.add(new Rule(new BigInteger("900"), "novecientos", false, "novecientos {0}"));
        rules.add(new Rule(new BigInteger("1000"), "mil", true, "{1} mil{0}"));
        rules.add(new Rule(new BigInteger("1000000"), "un millón", false, "un millón {0}"));
        rules.add(new Rule(new BigInteger("2000000"), "millones", true, "{1} millones{0}"));
    }

    private List<Rule> rules;

    public String cantar(BigDecimal bigDecimal) {
        if (bigDecimal.scale() == 0) {
            return cantarParteEntera(bigDecimal);
        } else {
            return cantarParteEntera(bigDecimal) + " " + cantarParteDecimal(bigDecimal);
        }
    }

    /**
     * Sings the decimal.
     * @param bigDecimal the decimal.
     * @return a song representing the decimal.
     */
    public String cantarParteEntera(BigDecimal bigDecimal) {
        BigInteger integerPart = bigDecimal.toBigInteger();

        int i = Collections.binarySearch(rules, new Rule(integerPart, null, false, null));
        if (i < 0) {
            i = -i - 2;
        }
        if (rules.get(i).recursegreater) {
            int cuantosdigitos = rules.get(i).index.toString().length() - 1;
            BigDecimal mayor = new BigDecimal(integerPart).movePointLeft(cuantosdigitos).setScale(0, RoundingMode.FLOOR);
            BigDecimal menor = new BigDecimal(integerPart).subtract(mayor.movePointRight(cuantosdigitos));
            /*
             * TODO: como paramtrizar que el mayor no puede terminar en "uno"?
             */
            String cantadoMayor = cantar(mayor);
            if (cantadoMayor.endsWith("veintiuno")) {
                cantadoMayor = cantadoMayor.substring(0, cantadoMayor.length() - 3).concat("ún");
            } else if (cantadoMayor.endsWith("uno")) {
                cantadoMayor = cantadoMayor.substring(0, cantadoMayor.length() - 1);
            }
            if (menor.equals(BigDecimal.ZERO)) {
                return MessageFormat.format(rules.get(i).literalcompleto, "", cantadoMayor);
            } else {
                /*
                 * TODO: este espacio no me gusta.
                 */
                return MessageFormat.format(rules.get(i).literalcompleto, " " + cantar(menor), cantadoMayor);
            }
        }
        integerPart = integerPart.subtract(rules.get(i).index.subtract(rules.get(i).minus));
        if (integerPart.signum() > 0) {
            String parteSiguiente = cantar(new BigDecimal(integerPart));
            return MessageFormat.format(rules.get(i).literalcompleto, parteSiguiente);
        }
        return rules.get(i).literal;
    }

    private String cantarParteDecimal(BigDecimal bigDecimal) {
        /*
         * TODO: no internacionalizado
         */
        bigDecimal = bigDecimal.subtract(new BigDecimal(bigDecimal.toBigInteger()));
        BigInteger bi = bigDecimal.movePointRight(2).toBigInteger();
        String s = bi.toString();
        return "0".substring(0, 2 - s.length()) + s + "/100";
    }

    private class Rule implements Comparable<Rule> {

        public Rule(BigInteger index, String literal, boolean recursegreater, String completo) {
            this.index = index;
            this.literal = literal;
            this.literalcompleto = completo;
            this.recursegreater = recursegreater;
            this.minus = BigInteger.ZERO;
        }

        public Rule(BigInteger index, String literal, boolean recursegreater, String extra, BigInteger mm) {
            this.index = index;
            this.literal = literal;
            this.literalcompleto = literal + extra;
            this.recursegreater = recursegreater;
            this.minus = mm;
        }

        BigInteger index;

        BigInteger minus;

        String literal;

        String literalcompleto;

        boolean recursegreater;

        public int compareTo(Cantador.Rule o) {
            return index.compareTo(o.index);
        }
    }
}
