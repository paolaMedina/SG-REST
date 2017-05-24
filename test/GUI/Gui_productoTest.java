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
 * @author Paola
 */
public class Gui_productoTest {
    
    public Gui_productoTest() {
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
     * Test of crearProducto method, of class Gui_producto.
     */
    @Test
    public void testCrearProducto1() {
        System.out.println("crearProducto");
        String id = "";
        String nombre = "";
        String precio = "";
        String idCategoria = "";
        String estado = "";
        String descripcion = "";
        Gui_producto instance = new Gui_producto();
        String expResult = "Llene los datos obligatorios";
        String result = instance.crearProducto(id, nombre, precio, idCategoria, estado, descripcion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    

    @Test
    public void testCrearProducto2() {
        System.out.println("crearProducto");
        String id = "10";
        String nombre = "jugo de fresa";
        String precio = "aa";
        String idCategoria = "3";
        String estado = "Activo";
        String descripcion = "jugo de fresa en agua";
        Gui_producto instance = new Gui_producto();
        String expResult = "El campo id, precio, idCategoria deben ser numéricos";
        String result = instance.crearProducto(id, nombre, precio, idCategoria, estado, descripcion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
      @Test
    public void testCrearProducto3() {
        System.out.println("crearProducto");
        String id = "10";
        String nombre = "jugo de fresa";
        String precio = "2000";
        String idCategoria = "3";
        String estado = "Inactivo";
        String descripcion = "jugo de fresa en agua";
        Gui_producto instance = new Gui_producto();
        String expResult = "El producto se agrego exitosamente";
        String result = instance.crearProducto(id, nombre, precio, idCategoria, estado, descripcion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
       @Test
    public void testCrearProducto4() {
        System.out.println("crearProducto");
        String id = "10";
        String nombre = "jugo de fresa";
        String precio = "2000";
        String idCategoria = "3";
        String estado = "Inactivo";
        String descripcion = "jugo de fresa en agua";
        Gui_producto instance = new Gui_producto();
        String expResult = "El producto ya existe";
        String result = instance.crearProducto(id, nombre, precio, idCategoria, estado, descripcion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
