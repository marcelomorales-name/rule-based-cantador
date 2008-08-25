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

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import junit.framework.TestCase;
import name.marcelomorales.cantador.parser.CantadorSpec;
import name.marcelomorales.cantador.parser.ParseException;

/**
 * Test for CantadorBase
 * @see name.marcelomorales.cantador.Cantador
 * @author Marcelo Morales
 */
public class CantadorTest extends TestCase {

    public CantadorTest(String testName) {
        super(testName);
    }

    /**
     * Test of cantar method, of class CantadorBase.
     */
    public void testCantar() {
        Cantador instance = Cantador.newCardinalInstance(new Locale("es", "BO"));
        assertEquals("cero", instance.cantar(BigDecimal.ZERO));
        assertEquals("uno", instance.cantar(new BigDecimal("1")));
        assertEquals("dos", instance.cantar(new BigDecimal("2")));
        assertEquals("tres", instance.cantar(new BigDecimal("3")));
        assertEquals("cuatro", instance.cantar(new BigDecimal("4")));
        assertEquals("cinco", instance.cantar(new BigDecimal("5")));
        assertEquals("seis", instance.cantar(new BigDecimal("6")));
        assertEquals("siete", instance.cantar(new BigDecimal("7")));
        assertEquals("ocho", instance.cantar(new BigDecimal("8")));
        assertEquals("nueve", instance.cantar(new BigDecimal("9")));
        assertEquals("diez", instance.cantar(new BigDecimal("10")));
        assertEquals("once", instance.cantar(new BigDecimal("11")));
        assertEquals("doce", instance.cantar(new BigDecimal("12")));
        assertEquals("trece", instance.cantar(new BigDecimal("13")));
        assertEquals("catorce", instance.cantar(new BigDecimal("14")));
        assertEquals("quince", instance.cantar(new BigDecimal("15")));
        assertEquals("dieciséis", instance.cantar(new BigDecimal("16")));
        assertEquals("diecisiete", instance.cantar(new BigDecimal("17")));
        assertEquals("dieciocho", instance.cantar(new BigDecimal("18")));
        assertEquals("diecinueve", instance.cantar(new BigDecimal("19")));
        assertEquals("veinte", instance.cantar(new BigDecimal("20")));
        assertEquals("veintiuno", instance.cantar(new BigDecimal("21")));
        assertEquals("veintitrés", instance.cantar(new BigDecimal("23")));
        assertEquals("treinta", instance.cantar(new BigDecimal("30")));
        assertEquals("treinta y tres", instance.cantar(new BigDecimal("33")));
        assertEquals("cincuenta y dos", instance.cantar(new BigDecimal("52")));
        assertEquals("noventa y nueve", instance.cantar(new BigDecimal("99")));
        assertEquals("cien", instance.cantar(new BigDecimal("100")));
        assertEquals("ciento uno", instance.cantar(new BigDecimal("101")));
        assertEquals("ciento dos", instance.cantar(new BigDecimal("102")));
        assertEquals("ciento quince", instance.cantar(new BigDecimal("115")));
        assertEquals("ciento noventa y nueve", instance.cantar(new BigDecimal("199")));
        assertEquals("doscientos", instance.cantar(new BigDecimal("200")));
        assertEquals("cuatrocientos cuarenta y dos", instance.cantar(new BigDecimal("442")));
        assertEquals("quinientos diez", instance.cantar(new BigDecimal("510")));
        assertEquals("novecientos noventa y nueve", instance.cantar(new BigDecimal("999")));
        assertEquals("un mil", instance.cantar(new BigDecimal("1000")));
        assertEquals("un mil uno", instance.cantar(new BigDecimal("1001")));
        assertEquals("un mil veintitrés", instance.cantar(new BigDecimal("1023")));
        assertEquals("un mil ochocientos veintidos", instance.cantar(new BigDecimal("1822")));
        assertEquals("un mil novecientos noventa y nueve", instance.cantar(new BigDecimal("1999")));
        assertEquals("dos mil", instance.cantar(new BigDecimal("2000")));
        assertEquals("dos mil uno", instance.cantar(new BigDecimal("2001")));
        assertEquals("tres mil", instance.cantar(new BigDecimal("3000")));
        assertEquals("nueve mil quinientos diez", instance.cantar(new BigDecimal("9510")));
        assertEquals("quince mil ochocientos veinticinco", instance.cantar(new BigDecimal("15825")));
        assertEquals("cien mil veinticinco", instance.cantar(new BigDecimal("100025")));
        assertEquals("ciento veinticinco mil", instance.cantar(new BigDecimal("125000")));
        assertEquals("trescientos un mil uno", instance.cantar(new BigDecimal("301001")));
        assertEquals("novecientos noventa y nueve mil novecientos noventa y nueve",
                instance.cantar(new BigDecimal("999999")));
        assertEquals("un millón", instance.cantar(new BigDecimal("1000000")));
        assertEquals("un millón trescientos un mil ciento dieciséis", instance.cantar(new BigDecimal("1301116")));
        assertEquals("veintiún millones", instance.cantar(new BigDecimal("21000000")));
        assertEquals("veintiún millones uno", instance.cantar(new BigDecimal("21000001")));
        assertEquals("ciento un millones diez", instance.cantar(new BigDecimal("101000010")));
        assertEquals("un mil millones diez", instance.cantar(new BigDecimal("1000000010")));
        assertEquals("diez mil millones diez", instance.cantar(new BigDecimal("10000000010")));
        assertEquals("diez mil cien millones ciento diez", instance.cantar(new BigDecimal("10100000110")));
        assertEquals("ciento once mil ciento once millones ciento once mil ciento once",
                instance.cantar(new BigDecimal("111111111111")));
    }

    public void testSubclass() throws Exception {
        class MiCantador extends CantadorBase {
            MiCantador() {
                super.addRule("1", "primero");
                super.addApokoptos("primero", "primer");
                super.addRule("2", "segundo");
                super.addRule("100", "centEsimo", "centEsimo {0}");
                super.addRule("1000", "milEsimo", "{1} milEsimo {0}");
            }
        }
        NumberFormat format = new MiCantador();
        assertEquals("primero", format.format(1));
        assertEquals("segundo", format.format(2));
        assertEquals("centEsimo", format.format(100));
        assertEquals("centEsimo segundo", format.format(102));
        assertEquals("primer milEsimo segundo", format.format(1002));
    }

    /**
     * Gracias a Sergio Criales por dar el requerimiento
     */
    public void testSergio_1() {
        CantadorBase instance = new CantadorBase(Cantador.newCardinalInstance(new Locale("es", "BO"))) {

            @Override
            protected void customRules() {
                super.addRule("1", "ún");
            }

            @Override
            public String cantar(BigDecimal bigDecimal) {
                return super.cantar(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)) + " BOLIVIANOS";
            }
            
        };
        assertEquals("ún 00/100 BOLIVIANOS", instance.cantar(new BigDecimal("1.00")));
        assertEquals("ún 50/100 BOLIVIANOS", instance.cantar(new BigDecimal("1.5")));
        assertEquals("tres 50/100 BOLIVIANOS", instance.cantar(new BigDecimal("3.50000001")));
        assertEquals("ciento ún millones diez 00/100 BOLIVIANOS", instance.cantar(new BigDecimal("101000010")));
    }

    public void testCantarDecimales() throws ParseException {
        CantadorBase instance = Cantador.newCardinalInstance(new Locale("es", "BO"));
        assertEquals("uno 00/100", instance.cantar(new BigDecimal("1.00")));
        assertEquals("uno 50/100", instance.cantar(new BigDecimal("1.5")));
        assertEquals("tres 50/100", instance.cantar(new BigDecimal("3.50000001")));
        assertEquals("uno 94/100", instance.cantar(new BigDecimal("1.94")));
    }

    public void testCantarComoFormat() throws ParseException {
        try {
            Cantador.newCardinalInstance();
        } catch (Exception e) {
        }
        CantadorBase instance = Cantador.newCardinalInstance(new Locale("es", "BO"));
        assertEquals("treinta y dos", instance.format(32));
    }

    public void testParser() throws ParseException {
        StringReader reader = new StringReader("0 => <cero>");
        new CantadorSpec(reader).Input();
        reader = new StringReader(
                "0 => <cero>" +
                "1 => <un(o)>" +
                "2 => <do(sitos|s)>" +
                "21 => <veinti(uno|\u00fan)>" +
                "29 => <veintinueve>" +
                "30 => <treinta[treinta y {0}]>" +
                "1000 => <[{1} mil_{0}]>");
        CantadorSpec sp = new CantadorSpec(reader);
        sp.Input();
        assertEquals("cero", sp.cantar(new BigDecimal("0")));
        assertEquals("uno", sp.cantar(new BigDecimal("1")));
        assertEquals("dositos", sp.cantar(new BigDecimal("2")));
        assertEquals("veintiuno", sp.cantar(new BigDecimal("21")));
        assertEquals("treinta", sp.cantar(new BigDecimal("30")));
        assertEquals("treinta y uno", sp.cantar(new BigDecimal("31")));
        assertEquals("un mil", sp.cantar(new BigDecimal("1000")));
        assertEquals("dos mil uno", sp.cantar(new BigDecimal("2001")));
    }

    public void testNumbersInEnglish() throws Exception {
        Cantador instance = Cantador.newInstance("0 => <zero>" +
                "1 => <one>" +
                "2 => <two>" +
                "3 => <three>" +
                "4 => <four>" +
                "5 => <five>" +
                "6 => <six>" +
                "7 => <seven>" +
                "8 => <eight>" +
                "9 => <nine>" +
                "10 => <ten>" +
                "11 => <eleven>" +
                "12 => <twelve>" +
                "13 => <thirteen>" +
                "14 => <fourteen>" +
                "15 => <fifteen>" +
                "16 => <sixteen>" +
                "17 => <seventeen>" +
                "18 => <eighteen>" +
                "19 => <nineteen>" +
                "20 => <twenty[twenty-{0}]>" +
                "30 => <thirty[thirty-{0}]>" +
                "40 => <forty[forty-{0}]>" +
                "50 => <fifty[fifty-{0}]>" +
                "60 => <sixty[sixty-{0}]>" +
                "70 => <seventy[seventy-{0}]>" +
                "80 => <eighty[eighty-{0}]>" +
                "90 => <ninety[ninety-{0}]>");
        assertEquals("thirty-two", instance.cantar(new BigDecimal("32")));
    }

    public void testCommonAmericanVernacular() throws Exception {
        /*
        Cantador instance = Cantador.newInstance("x");
        assertEquals("one-oh-one", instance.cantar(new BigDecimal("101")));
        assertEquals("one-ten", instance.cantar(new BigDecimal("110")));
        assertEquals("two-oh-eight", instance.cantar(new BigDecimal("208")));
         */
    }

    public void testMostSignificative() throws Exception {
        /*
        Cantador instance = Cantador.newInstance("x");
        assertEquals("one point two million", instance.cantar(new BigDecimal("1200000")));
        assertEquals("three million", instance.cantar(new BigDecimal("3000000")));
        assertEquals("six point four billion", instance.cantar(new BigDecimal("6400000013")));
         */
    }

    public void testNoThousand() throws Exception {
        /*
        Cantador instance = Cantador.newInstance("x");
        assertEquals("twelve hundred", instance.cantar(new BigDecimal("1200")));
         */
    }

    public void testGerman() throws Exception {
        /*
        Cantador instance = Cantador.newInstance("0 => <null>" +
                "1=><eins> 2=><zwei> 3=><drei> 4=><vier> 5=><fünf> 6=><sechs> 7=><sieben> 8=><acht> 9=><neun>" +
                "10=><zehn> 11=><elf> 12=><zwölf[{0}zehn]>");
        assertEquals("fünfzehn", instance.cantar(new BigDecimal("15")));
         */
    }

    public void testFemaleOrdinal() throws Exception {
        Cantador instance = Cantador.newFemaleOrdinalInstance(new Locale("es", "BO"));
        assertEquals("vigesimosegunda", instance.cantar(new BigDecimal("22")));
    }
}
