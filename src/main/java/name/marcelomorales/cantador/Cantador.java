/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * PLEASE KEEP UTF-8 ENCODING
 *
 * Copyright (C) 2008-2011 Marcelo Morales (marcelomorales.name@gmail.com)
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

import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import name.marcelomorales.cantador.parser.CantadorSpec;
import name.marcelomorales.cantador.parser.ParseException;

/**
 *
 * @author Marcelo Morales
 */
public class Cantador extends CantadorSpec {

    static final long serialVersionUID = -876876876877L;

    private static final String[][] cardinals = {{"es_BO",
            "0 => <cero>" +
            "1 => <un(o)>" +
            "2 => <dos>" +
            "3 => <tres>" +
            "4 => <cuatro>" +
            "5 => <cinco>" +
            "6 => <seis>" +
            "7 => <siete>" +
            "8 => <ocho>" +
            "9 => <nueve>" +
            "10 => <diez>" +
            "11 => <once>" +
            "12 => <doce>" +
            "13 => <trece>" +
            "14 => <catorce>" +
            "15 => <quince>" +
            "16 => <dieciséis>" +
            "17 => <diecisiete>" +
            "18 => <dieciocho>" +
            "19 => <diecinueve>" +
            "20 => <veinte>" +
            "21 => <veinti(uno|ún)>" +
            "22 => <veintidos>" +
            "23 => <veintitrés>" +
            "24 => <veinticuatro>" +
            "25 => <veinticinco>" +
            "26 => <veintiséis>" +
            "27 => <veintisiete>" +
            "28 => <veintiocho>" +
            "29 => <veintinueve>" +
            "30 => <treinta[treinta y {0}]>" +
            "40 => <cuarenta[cuarenta y {0}]>" +
            "50 => <cincuenta[cincuenta y {0}]>" +
            "60 => <sesenta[sesenta y {0}]>" +
            "70 => <setenta[setenta y {0}]>" +
            "80 => <ochenta[ochenta y {0}]>" +
            "90 => <noventa[noventa y {0}]>" +
            "100 => <cien[ciento {0}]>" +
            "200 => <doscientos[doscientos {0}]>" +
            "300 => <trescientos[trescientos {0}]>" +
            "400 => <cuatrocientos[cuatrocientos {0}]>" +
            "500 => <quinientos[quinientos {0}]>" +
            "600 => <seiscientos[seiscientos {0}]>" +
            "700 => <setecientos[setecientos {0}]>" +
            "800 => <ochocientos[ochocientos {0}]>" +
            "900 => <novecientos[novecientos {0}]>" +
            "1000 => <[{1} mil_{0}]>" +
            "1000000 => <un millón[un millón {0}]>" +
            "2000000 => <millones[{1} millones_{0}]>"
        }};

    private static final String[][] ordinalesFemeninos = {{"es_BO",
            "1 => <primera>" +
            "2 => <segunda>" +
            "3 => <tercera>" +
            "4 => <cuarta>" +
            "5 => <quinta>" +
            "6 => <sexta>" +
            "7 => <séptima>" +
            "8 => <octava>" +
            "9 => <novena>" +
            "10 => <décima>" +
            "11 => <undécima>" +
            "12 => <duodécima>" +
            "13 => <decimotercera>" +
            "14 => <decimocuarta>" +
            "15 => <decimoquinta>" +
            "16 => <decimosexta>" +
            "17 => <decimoséptima>" +
            "18 => <decimoctava>" +
            "19 => <decimonovena>" +
            "20 => <vigésimo[vigesimo{0}]>" +
            "30 => <trigésimo[trigesimo{0}]>" +
            "40 => <cuadragésimo[cuadragesimo{0}]>" +
            "50 => <quincuagésimo[quincuagesimo{0}]>" +
            "60 => <sexagésimo[sexagesimo{0}]>" +
            "70 => <septuagésimo[septuagesimo{0}]>" +
            "80 => <octogésimo[octogesimo{0}]>" +
            "90 => <nonagésimo[nonagesimo{0}]>"
        /*
         * TODO: 100 => centesimo, 101 => centesimoprimera, 111 => centesimo undecima ??
         */
        }};
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

    /**
     * @since 0.003
     * @return Female ordinal form in default locale.
     */
    public static Cantador newFemaleOrdinalInstance() {
        return newCardinalInstance(Locale.getDefault());
    }

    public static Cantador newCardinalInstance(Locale locale) {
        return fromVariables(cardinals, locale);
    }

    /**
     * Female ordinal form.
     * @since 0.003
     * @param locale currently only "ES_BO".
     * @return a new instance for the bolivian spanish ordinal female.
     * <p>Examples: </p>
     * <ul>
     *   <li>1 = primera.</li>
     *   <li>11 = und&eacute;cima.</li>
     * </ul>
     */
    public static Cantador newFemaleOrdinalInstance(Locale locale) {
        return fromVariables(ordinalesFemeninos, locale);
    }

    private static final Cantador fromVariables(String[][] var, Locale locale) {
        for (int i = 0; i < var.length; i++) {
            if (var[i][0].equals(locale.toString())) {
                try {
                    return newInstance(var[i][1]);
                } catch (ParseException ex) {
                    throw new UnsupportedOperationException("Locale " + locale.toString() + " bugged.", ex);
                }
            }
        }
        throw new UnsupportedOperationException("Locale " + locale.toString() + " not supported yet.");
    }

    public static Cantador newInstance(String rulespec) throws ParseException {
        StringReader reader = new StringReader(rulespec);
        return new Cantador(reader);
    }

    /**
     * Default constructor.
     *
     * @param reader curently unused
     */
    private Cantador(Reader reader) throws ParseException {
        super(reader);
        Input();
    }
}
