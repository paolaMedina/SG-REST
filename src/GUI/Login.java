/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Clases.Pedido;
import Clases.Producto;
import Clases.ProductoPedido;
import Clases.ProductoPedidoPK;
import Controladores.PedidoJpaController;
import Controladores.ProductoJpaController;
import Controladores.ProductoPedidoJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 *
 * @author Paola
 */
public class Login {
    
    
    public static void v (){
        
  
                
        
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG_RESTPU");
        
        //se crea el controlador del producto y del ususario asociaro
        PedidoJpaController pedido= new PedidoJpaController(emf);
        ProductoJpaController producto = new ProductoJpaController(emf);
        ProductoPedidoJpaController daoPedidoProducto = new ProductoPedidoJpaController(emf);
        //obtener objetos
         Pedido pe=pedido.findPedido(1);
        Producto pr=producto.findProducto("1");
        
        
        //se crea un objeto producto y se le asignan sus atributos
       
        ProductoPedido productopedido = new ProductoPedido();
        productopedido.setPedido(pe);
        productopedido.setProducto(pr);
        productopedido.setCantProductos(1);
       
        try {
                daoPedidoProducto.create(productopedido);
                JOptionPane.showMessageDialog(null,"registro correcto");
                
                
            }
            
         catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "Llene los datos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception ex) {
            Logger.getLogger(Gui_empleado.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
     v();
        
    }
    
}
