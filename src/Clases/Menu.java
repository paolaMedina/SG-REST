/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Controladores.ProductoJpaController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Daniel
 */
public class Menu implements Observer{
    
    private static Menu instancia = null;
    private ArrayList<Producto> productosMenu = new ArrayList();

    private Menu() {
        this.llenarProductos();
    }

    public static Menu getInstancia() {
        if (Menu.instancia == null) {
            Menu.instancia = new Menu();
        }
        
        return instancia;
    }

    public ArrayList<Producto> getProductosMenu() {
        return productosMenu;
    }
    
    public void llenarProductos(){
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");

        //se crea el controlador del producto
        ProductoJpaController daoProducto = new ProductoJpaController(emf);

        List<Producto> aux = daoProducto.findProductoEntities();

        for (int i = 0; i < aux.size(); i++) {
            this.productosMenu.add(aux.get(i));
        }
    }
    
    
    public Iterator getIterator() {
       return new MenuIterator();
   }

    @Override
    public void update(Observable o, Object o1) {
        this.instancia = null;
    }
    
    //clase interna que implementa la interfaz iterator que permitira recorrer la coleccion
    //de prodouctos del menu.
    private class MenuIterator implements Iterator{

        int index = 0;
        
        @Override
        public boolean hasNext() {
            if(index < productosMenu.size()){
                return true;
             }
             return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
            return productosMenu.get(index++);
         }
         return null;
        }

        @Override
        public void remove() {
            productosMenu.remove(index);
        }
        
        
    }
}
