/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * PLEASE KEEP UTF-8 ENCODING
 *
 * Copyright (C) 2008 Marcelo Morales (marcelomorales.name@gmail.com)
 *
 *   This file is part of Rulebased Cantador.
 *
 *   Rulebased Cantador is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Rule-based formatter.
 *
 * <h1>History</h1>
 * <p>This was inspired by the IBM ICU one. But I could not make it work properly in spanish. So I did my own.
 * This software was based on an article by IBM, but has no resemblance of the ICU software itself.</p>
 *
 * <p>I started using the binary search algorithm because I did not like using arithmetics. I really didn't know
 * (I still don't) if it is the best aproach, but it seemed a little more elegant to me. This became the 0.001 release
 * that I used in some of my projects. This was also released under a proprietary licence.</p>
 *
 * <p>For the 0.002 release, I implemented the standard NumberFormat Java interface. Cleaned up the algorithm a
 * little bit. I changed the license to GPL v3. I externalized the construction of the formatter. Then there were two
 * methods for defining the format ruleset: chaining and the language (a very simple grammar).</p>
 *
 * <p>I needed an ordinal female, so I tagged the 0.003</p>
 *
 * @author Marcelo Morales
 */
public class CantadorBase extends NumberFormat {

    static final long serialVersionUID = 8973982744L;

    private List<Rule> rules;

    private SortedSet<Apokoptos> apokoptos;

    private boolean finished;

    protected final void addRule(String number, String shortLiteral) {
        if (finished) {
            throw new IllegalStateException("This one is already finished, can't add more rules");
        }
        Rule r = new Rule(new BigInteger(number), shortLiteral);
        if (rules.contains(r)) {
            rules.remove(r);
        }
        rules.add(r);
    }

    protected final void addRule(String number, String shortLiteral, String longLiteral) {
        if (finished) {
            throw new IllegalStateException("This one is already finished, can't add more rules");
        }
        Rule r = new Rule(new BigInteger(number), shortLiteral, longLiteral);
        if (rules.contains(r)) {
            rules.remove(r);
        }
        rules.add(r);
    }

    protected final void addApokoptos(String haystack, String needle) {
        if (finished) {
            throw new IllegalStateException("This one is already finished, can't add more rules");
        }
        Apokoptos a = new Apokoptos(haystack, needle);
        if (apokoptos.contains(a)) {
            apokoptos.remove(a);
        }
        apokoptos.add(a);
    }

    protected CantadorBase() {
        rules = new LinkedList<Rule>();
        apokoptos = new TreeSet<Apokoptos>();
        finished = false;
    }

    public CantadorBase(CantadorBase other) {
        rules = other.rules;
        apokoptos = other.apokoptos;
        finished = false;
    }

    protected void customRules() {
    }

    /**
     * Sings the decimal.
     * @param bigDecimal the decimal.
     * @return a song representing the decimal.
     */
    public String cantar(BigDecimal bigDecimal) {
        if (!finished) {
            customRules();
            Collections.sort(rules);
            finished = true;
        }
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
        if (i == -1) {
            return "<invalid rules applied>";
        }
        if (rules.get(i).recursegreater) {
            int howManyDigits = rules.get(i).index.toString().length() - 1;
            BigDecimal mayor = new BigDecimal(integerPart).movePointLeft(howManyDigits).setScale(0, RoundingMode.FLOOR);
            BigDecimal menor = new BigDecimal(integerPart).subtract(mayor.movePointRight(howManyDigits));
            String cantadoMayor = cantarParteEntera(mayor);
            for (Apokoptos a : apokoptos) { // HAS to be sorted!
                if (cantadoMayor.endsWith(a.haystack)) {
                    cantadoMayor = cantadoMayor.substring(0, cantadoMayor.length() - a.haystack.length()).concat(
                            a.needle);
                }
            }
            if (menor.equals(BigDecimal.ZERO)) {
                return MessageFormat.format(rules.get(i).literalcompleto, "", cantadoMayor);
            } else {
                return MessageFormat.format(rules.get(i).literalcompletoWithSpace, cantarParteEntera(menor), cantadoMayor);
            }
        }
        integerPart = integerPart.subtract(rules.get(i).index);
        if (integerPart.signum() > 0) {
            String parteSiguiente = cantarParteEntera(new BigDecimal(integerPart));
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

    private static class Rule implements Comparable<Rule> {

        public Rule(BigInteger index, String literal) {
            this.index = index;
            this.literal = literal;
            this.literalcompleto = "";
            this.recursegreater = false;
            this.literalcompletoWithSpace = "";
        }

        public Rule(BigInteger index, String literal, String completo) {
            this.index = index;
            this.literal = literal;
            if (completo == null) {
                this.recursegreater = false;
                this.literalcompletoWithSpace = "";
                this.literalcompleto = "";
                return;
            }
            this.recursegreater = completo.contains("{1}");
            if (completo.contains("_")) {
                this.literalcompleto = completo.replace("_", "");
                this.literalcompletoWithSpace = completo.replace("_", " ");
                return;
            }
            this.literalcompleto = completo;
            this.literalcompletoWithSpace = completo;
        }

        BigInteger index;

        String literal;

        String literalcompleto;

        boolean recursegreater;

        String literalcompletoWithSpace;

        public int compareTo(CantadorBase.Rule o) {
            return index.compareTo(o.index);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Rule) {
                return index.equals(((Rule) obj).index);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + (this.index != null ? this.index.hashCode() : 0);
            return hash;
        }
    }

    private static class Apokoptos implements Comparable<Apokoptos> {

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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Apokoptos) {
                return len.equals(((Apokoptos) obj).len);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.len != null ? this.len.hashCode() : 0);
            return hash;
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
