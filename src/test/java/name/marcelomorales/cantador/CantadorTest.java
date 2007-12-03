package name.marcelomorales.cantador;

import java.math.BigDecimal;
import junit.framework.TestCase;

/**
 *
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
        Cantador instance = new Cantador("xxx");
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
        assertEquals("veintitrés", instance.cantar(new BigDecimal("23")));
        assertEquals("treinta", instance.cantar(new BigDecimal("30")));
        assertEquals("treinta y tres", instance.cantar(new BigDecimal("33")));
        assertEquals("cincuenta y dos", instance.cantar(new BigDecimal("52")));
        assertEquals("noventa y nueve", instance.cantar(new BigDecimal("99")));
        assertEquals("cien", instance.cantar(new BigDecimal("100")));
        assertEquals("ciento uno", instance.cantar(new BigDecimal("101")));
        assertEquals("ciento quince", instance.cantar(new BigDecimal("115")));
        assertEquals("ciento noventa y nueve", instance.cantar(new BigDecimal("199")));
        assertEquals("doscientos", instance.cantar(new BigDecimal("200")));
        assertEquals("cuatrocientos cuarenta y dos", instance.cantar(new BigDecimal("442")));
        assertEquals("quinientos diez", instance.cantar(new BigDecimal("510")));
        assertEquals("novecientos noventa y nueve", instance.cantar(new BigDecimal("999")));
        assertEquals("un mil", instance.cantar(new BigDecimal("1000")));
        assertEquals("un mil uno", instance.cantar(new BigDecimal("1001")));
        assertEquals("un mil novecientos noventa y nueve", instance.cantar(new BigDecimal("1999")));
        assertEquals("dos mil", instance.cantar(new BigDecimal("2000")));
        assertEquals("dos mil uno", instance.cantar(new BigDecimal("2001")));
        assertEquals("tres mil", instance.cantar(new BigDecimal("3000")));
        assertEquals("nueve mil quinientos diez", instance.cantar(new BigDecimal("9510")));
        assertEquals("quince mil ochocientos veinticinco", instance.cantar(new BigDecimal("15825")));
        assertEquals("cien mil veinticinco", instance.cantar(new BigDecimal("100025")));
        assertEquals("trescientos un mil uno", instance.cantar(new BigDecimal("301001")));
        assertEquals("novecientos noventa y nueve mil novecientos noventa y nueve",
                instance.cantar(new BigDecimal("999999")));
        assertEquals("un millón", instance.cantar(new BigDecimal("1000000")));
        assertEquals("un millón trescientos un mil ciento dieciséis", instance.cantar(new BigDecimal("1301116")));
        assertEquals("veintiún millones", instance.cantar(new BigDecimal("21000000")));
        assertEquals("ciento un millones", instance.cantar(new BigDecimal("101000000")));   
    }
    
    public void testCantarDecimales() {
        Cantador instance = new Cantador("xxx");
        assertEquals("uno 00/100", instance.cantar(new BigDecimal("1.00")));
        assertEquals("uno 50/100", instance.cantar(new BigDecimal("1.5")));
        assertEquals("tres 50/100", instance.cantar(new BigDecimal("3.50000001")));
        assertEquals("uno 94/100", instance.cantar(new BigDecimal("1.94")));
    }
}
