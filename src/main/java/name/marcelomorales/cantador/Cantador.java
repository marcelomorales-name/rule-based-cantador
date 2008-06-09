package name.marcelomorales.cantador;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Rule-based formatter.
 *
 * <p>This was inspired by the IBM ICU one. But I could not make it work properly in spanish. So I did my own.</p>
 * <p>The new Version uses the Java standard interface in addition to the original one.</p>
 *
 * @author Marcelo Morales
 */
public class Cantador extends NumberFormat {

    private static final String[][] cardinals = {{"es_BO", "x"}};

    /*
     * TODO: http://en.wikipedia.org/wiki/Names_of_numbers_in_English
     */
    /*
     * TODO: in spanish: female form (doscientas instead of doscientos)
     * TODO: spanish: ordinal (male/female/"apocopado")
     */
    public static Cantador newCardinalInstance() {
        return newCardinalInstance(Locale.getDefault());
    }

    public static Cantador newCardinalInstance(Locale locale) {
        for (int i = 0; i < cardinals.length; i++) {
            if (cardinals[i][0].equals(locale.toString())) {
                return new Cantador(cardinals[i][1]);
            }
        }
        throw new UnsupportedOperationException("Locale " + locale.toString() + " not supported yet.");
    }

    public static Cantador newInstance(String rulespec) {
        return new Cantador(rulespec);
    }

    /**
     * Default constructor.
     *
     * @param rulesSpec curently unused
     */
    private Cantador(String rulesSpec) {
        /*
         * TODO: parsear rulesSpec segun un lenguaje a definirse.
         *
         * Inicial:
         *
         * 0 => cero
         * 1 => un(o)
         * ...
         * 21 => veinti(uno|ún)
         * 29 => veintinueve
         * 30 => treinta[treinta y {0}]
         */
        rules = new LinkedList<Rule>();
        apokoptos = new TreeSet<Apokoptos>();
        rules.add(new Rule(new BigInteger("0"), "cero"));
        rules.add(new Rule(new BigInteger("1"), "uno"));
        apokoptos.add(new Apokoptos("uno", "un"));
        rules.add(new Rule(new BigInteger("2"), "dos"));
        rules.add(new Rule(new BigInteger("3"), "tres"));
        rules.add(new Rule(new BigInteger("4"), "cuatro"));
        rules.add(new Rule(new BigInteger("5"), "cinco"));
        rules.add(new Rule(new BigInteger("6"), "seis"));
        rules.add(new Rule(new BigInteger("7"), "siete"));
        rules.add(new Rule(new BigInteger("8"), "ocho"));
        rules.add(new Rule(new BigInteger("9"), "nueve"));
        rules.add(new Rule(new BigInteger("10"), "diez"));
        rules.add(new Rule(new BigInteger("11"), "once"));
        rules.add(new Rule(new BigInteger("12"), "doce"));
        rules.add(new Rule(new BigInteger("13"), "trece"));
        rules.add(new Rule(new BigInteger("14"), "catorce"));
        rules.add(new Rule(new BigInteger("15"), "quince"));
        rules.add(new Rule(new BigInteger("16"), "dieciséis"));
        rules.add(new Rule(new BigInteger("17"), "diecisiete"));
        rules.add(new Rule(new BigInteger("18"), "dieciocho"));
        rules.add(new Rule(new BigInteger("19"), "diecinueve"));
        rules.add(new Rule(new BigInteger("20"), "veinte"));
        rules.add(new Rule(new BigInteger("21"), "veintiuno"));
        apokoptos.add(new Apokoptos("veintiuno", "veintiún"));
        rules.add(new Rule(new BigInteger("22"), "veintidos"));
        rules.add(new Rule(new BigInteger("23"), "veintitrés"));
        rules.add(new Rule(new BigInteger("24"), "veinticuatro"));
        rules.add(new Rule(new BigInteger("25"), "veinticinco"));
        rules.add(new Rule(new BigInteger("26"), "veintiséis"));
        rules.add(new Rule(new BigInteger("27"), "veintisiete"));
        rules.add(new Rule(new BigInteger("28"), "veintiocho"));
        rules.add(new Rule(new BigInteger("29"), "veintinueve"));
        rules.add(new Rule(new BigInteger("30"), "treinta", "treinta y {0}"));
        rules.add(new Rule(new BigInteger("40"), "cuarenta", "cuarenta y {0}"));
        rules.add(new Rule(new BigInteger("50"), "cincuenta", "cincuenta y {0}"));
        rules.add(new Rule(new BigInteger("60"), "sesenta", "sesenta y {0}"));
        rules.add(new Rule(new BigInteger("70"), "setenta", "setenta y {0}"));
        rules.add(new Rule(new BigInteger("80"), "ochenta", "ochenta y {0}"));
        rules.add(new Rule(new BigInteger("90"), "noventa", "noventa y {0}"));
        rules.add(new Rule(new BigInteger("100"), "cien", "ciento {0}"));
        rules.add(new Rule(new BigInteger("200"), "doscientos", "doscientos {0}"));
        rules.add(new Rule(new BigInteger("300"), "trescientos", "trescientos {0}"));
        rules.add(new Rule(new BigInteger("400"), "cuatrocientos", "cuatrocientos {0}"));
        rules.add(new Rule(new BigInteger("500"), "quinientos", "quinientos {0}"));
        rules.add(new Rule(new BigInteger("600"), "seiscientos", "seiscientos {0}"));
        rules.add(new Rule(new BigInteger("700"), "setecientos", "setecientos {0}"));
        rules.add(new Rule(new BigInteger("800"), "ochocientos", "ochocientos {0}"));
        rules.add(new Rule(new BigInteger("900"), "novecientos", "novecientos {0}"));
        rules.add(new Rule(new BigInteger("1000"), "un mil", "{1} mil{ 0}"));
        rules.add(new Rule(new BigInteger("1000000"), "un millón", "un millón {0}"));
        rules.add(new Rule(new BigInteger("2000000"), "millones", "{1} millones{ 0}"));
    }

    private List<Rule> rules;

    private SortedSet<Apokoptos> apokoptos;

    /**
     * Sings the decimal.
     * @param bigDecimal the decimal.
     * @return a song representing the decimal.
     */
    public String cantar(BigDecimal bigDecimal) {
        if (bigDecimal.scale() == 0) {
            return cantarParteEntera(bigDecimal);
        } else {
            return cantarParteEntera(bigDecimal) + " " + cantarParteDecimal(bigDecimal);
        }
    }

    private String cantarParteEntera(BigDecimal bigDecimal) {
        BigInteger integerPart = bigDecimal.toBigInteger();

        int i = Collections.binarySearch(rules, new Rule(integerPart, null));
        if (i < 0) {
            i = -i - 2;
        }
        if (rules.get(i).recursegreater) {
            int howManyDigits = rules.get(i).index.toString().length() - 1;
            BigDecimal mayor = new BigDecimal(integerPart).movePointLeft(howManyDigits).setScale(0, RoundingMode.FLOOR);
            BigDecimal menor = new BigDecimal(integerPart).subtract(mayor.movePointRight(howManyDigits));
            String cantadoMayor = cantar(mayor);
            for (Apokoptos a : apokoptos) { // HAS to be sorted!
                if (cantadoMayor.endsWith(a.haystack)) {
                    cantadoMayor = cantadoMayor.substring(0, cantadoMayor.length() - a.haystack.length()).concat(a.needle);
                }
            }
            if (menor.equals(BigDecimal.ZERO)) {
                return MessageFormat.format(rules.get(i).literalcompleto, "", cantadoMayor);
            } else {
                return MessageFormat.format(rules.get(i).literalcompleto, cantar(menor), cantadoMayor);
            }
        }
        integerPart = integerPart.subtract(rules.get(i).index);
        if (integerPart.signum() > 0) {
            String parteSiguiente = cantar(new BigDecimal(integerPart));
            return MessageFormat.format(rules.get(i).literalcompleto, parteSiguiente);
        }
        return rules.get(i).literal;
    }

    private String cantarParteDecimal(BigDecimal bigDecimal) {
        /*
         * TODO: no internacionalizado y no sigue las reglas. como hacer reglas para el decimal?
         */
        bigDecimal = bigDecimal.subtract(new BigDecimal(bigDecimal.toBigInteger()));
        BigInteger bi = bigDecimal.movePointRight(2).toBigInteger();
        String s = bi.toString();
        return "0".substring(0, 2 - s.length()) + s + "/100";
    }

    private class Rule implements Comparable<Rule> {

        public Rule(BigInteger index, String literal) {
            this.index = index;
            this.literal = literal;
            this.literalcompleto = "";
            this.recursegreater = false;
        }

        public Rule(BigInteger index, String literal, String completo) {
            this.index = index;
            this.literal = literal;
            this.literalcompleto = completo;
            if (completo != null) {
                this.recursegreater = completo.contains("{1}");
            } else {
                this.recursegreater = false;
            }
        }

        BigInteger index;

        String literal;

        String literalcompleto;

        boolean recursegreater;

        public int compareTo(Cantador.Rule o) {
            return index.compareTo(o.index);
        }
    }

    private class Apokoptos implements Comparable<Apokoptos> {

        public Apokoptos(String haystack, String needle) {
            this.haystack = haystack;
            this.needle = needle;
            this.len = needle.length();
        }

        Integer len;

        String haystack;

        String needle;

        public int compareTo(Apokoptos o) {
            return o.len.compareTo(len);
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.002
     */
    @Override
    public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
        if (number instanceof BigDecimal) {
            /*
             * TODO: por que ignoramos pos? por lo menos deberia verificar si mostar decimales o no?
             */
            toAppendTo.append(this.cantar((BigDecimal) number));
            return toAppendTo;
        } else {
            return super.format(number, toAppendTo, pos);
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.002
     */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(new BigDecimal(number), toAppendTo, pos);
    }

    /**
     * {@inheritDoc}
     * @since 0.002
     */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(new BigDecimal(number), toAppendTo, pos);
    }

    /**
     * {@inheritDoc}
     * @since 0.002
     */
    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
