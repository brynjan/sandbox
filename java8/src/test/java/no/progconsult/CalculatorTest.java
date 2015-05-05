package no.progconsult;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author bno.
 */
public class CalculatorTest {

    @Test
    public void testCalculate() throws Exception {
        Calculator calculator = new Calculator();
        assertEquals(calculator.calculate(Calculator.Operation.PLUS, 1 ,2 ), 3.0);
    }
}