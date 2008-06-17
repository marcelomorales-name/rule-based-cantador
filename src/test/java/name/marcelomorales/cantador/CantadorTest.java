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
import java.util.Locale;
import junit.framework.TestCase;
import name.marcelomorales.cantador.parser.CantadorSpec;
import name.marcelomorales.cantador.parser.ParseException;

/**
 * Test for Cantador
 * @see name.marcelomorales.cantador.Cantador
 * @author Marcelo Morales
 */
public class CantadorTest extends TestCase {

    public CantadorTest(String testName) {
        super(testName);
    }

    /**
     * Test of cantar method, of class Cantador.
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
        assertEquals("ciento un millones", instance.cantar(new BigDecimal("101000000")));
    }

    public void testCantarDecimales() {
        Cantador instance = Cantador.newInstance("xxx");
        assertEquals("uno 00/100", instance.cantar(new BigDecimal("1.00")));
        assertEquals("uno 50/100", instance.cantar(new BigDecimal("1.5")));
        assertEquals("tres 50/100", instance.cantar(new BigDecimal("3.50000001")));
        assertEquals("uno 94/100", instance.cantar(new BigDecimal("1.94")));
    }

    public void testCantarComoFormat() {
        try {
            Cantador.newCardinalInstance();
        } catch (Exception e) {
        }
        Cantador instance = Cantador.newInstance("xxx");
        assertEquals("treinta y dos", instance.format(32));
    }

    public void testParser() throws ParseException {
        StringReader reader = new StringReader("0 => <cero>");
        new CantadorSpec(reader).Input();
        reader = new StringReader(
                "0 => <cero>\n" +
                "1 => <un(o)>\n" +
                "2 => <do(s|sitos)>\n" +
                "21 => <veinti(uno|\u00fan)>\n" +
                "29 => <veintinueve>\n" +
                "30 => <treinta[treinta y {0}]>\n" +
                "1000 => <[{1} mil_{0}]>\n");
        new CantadorSpec(reader).Input();
    }
}
