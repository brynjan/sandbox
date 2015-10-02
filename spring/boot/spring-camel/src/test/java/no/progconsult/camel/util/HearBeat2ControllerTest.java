package no.progconsult.camel.util;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * @author bno
 */
public class HearBeat2ControllerTest {

    @Test(groups = "unittest1")
    public void testEn(){
        System.out.println("2");
        assertTrue(true);
    }

    @Test(groups = "functest")
    public void testTo(){
        assertTrue(true);
    }

}