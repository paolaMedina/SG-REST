/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class Gui_loginTest {
    
    public Gui_loginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loguear method, of class Gui_login.
     */
    @Test
    public void testLoguear1() {

        String usuario = "";
        String password = "";
        Gui_login instance = new Gui_login();
        String expResult = "Ingrese los datos completos";
        String result = instance.loguear(usuario, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testLoguear2() {

        String usuario = "1111";
        String password = "12345";
        Gui_login instance = new Gui_login();
        String expResult = "Contraseña incorrecta";
        String result = instance.loguear(usuario, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testLoguear3() {

        String usuario = "1111";
        String password = "1111";
        Gui_login instance = new Gui_login();
        String expResult = "El gerente ingreso exitosamente";
        String result = instance.loguear(usuario, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testLoguear4() {

        String usuario = "3333";
        String password = "3333";
        Gui_login instance = new Gui_login();
        String expResult = "El mesero ingreso exitosamente";
        String result = instance.loguear(usuario, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void testLoguear5() {

        String usuario = "4444";
        String password = "4444";
        Gui_login instance = new Gui_login();
        String expResult = "El cajero ingreso exitosamente";
        String result = instance.loguear(usuario, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    
    
}
