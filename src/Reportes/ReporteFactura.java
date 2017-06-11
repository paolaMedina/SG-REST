/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Clases.Factura;
import Clases.Pedido;
import Clases.Producto;
import Clases.ProductoPedido;
import Controladores.EmpleadoJpaController;
import Controladores.FacturaJpaController;
import Controladores.PedidoJpaController;
import Controladores.ProductoJpaController;
import Controladores.ProductoPedidoJpaController;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 *
 * @author Paola
 */
public class ReporteFactura {
    
    public void imprimirFactura(List<ProductoPedido> productos, int idFactura, Pedido pedido,long descuento, long propina, long impuestos, 
            String idEmpleado, String ccCliente, Date fechaHora, String formaPago) {
        
        String numeroDelPedido = pedido.getNumPedido().toString();
        String numMesa = pedido.getNumMesa().getMesa().toString();
        String empleadoQueAtendio = pedido.getIdEmpleado().getNombre().toString();
        String estadoPedido = pedido.getIdEstadoPedido().getEstado().toString();
        String horaInicioPedido = pedido.getHoraInicioPedido().toString();
        String horaFinalPedido = pedido.getHoraFinalPedido().toString();
        String cajero=nombreCajero(idEmpleado);
        long totalPagarProductos=0;

        DecimalFormat formatea = new DecimalFormat("###,###.##");
        try {
            String url="Reportes/factura"+idFactura+".pdf";
          
            OutputStream archivo = new FileOutputStream(new File(url));
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            //lo que contendra el pdf
            Paragraph titulo = new Paragraph("+------------------------------------------+Factura "+idFactura+"+------------------------------------------+");
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            doc.add(new Paragraph("Empresa: Crepes & Waffles"));
            doc.add(new Paragraph("Numéro de factura: " + idFactura));
            doc.add(new Paragraph("Numéro de pedido: " + numeroDelPedido));
            doc.add(new Paragraph("Numéro de mesa " + numMesa));
            doc.add(new Paragraph("Mesero: " + empleadoQueAtendio));
            doc.add(new Paragraph("Cajero: " + cajero));
            doc.add(new Paragraph("Hora de inicio del pedido: " + horaInicioPedido));
            doc.add(new Paragraph("Hora de fin del pedido: " + horaFinalPedido ));
            doc.add(new Paragraph("Fecha y hora de pago: " + fechaHora ));
            doc.add(new Paragraph("Cedula cliente: " + ccCliente + "\n" + "\n" + "\n"));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio");
            for (int i = 0; i < productos.size(); i++) {

                String nombreProducto = productos.get(i).getProductoPedidoPK().getProductoNombre();
                int cantidadProducto = productos.get(i).getCatidad();
                Long precio = valorProducto(nombreProducto, cantidadProducto);

                table.addCell("" + nombreProducto + "");
                table.addCell("" + cantidadProducto + "");
                table.addCell(formatea.format(precio));
                totalPagarProductos += precio;
            }
            
            doc.add(table);
            doc.add(new Paragraph("SUBTOTAL: " + totalPagarProductos ));
            doc.add(new Paragraph ("Propina: "+  propina));
            doc.add(new Paragraph ("Descuentos: "+  descuento));
            doc.add(new Paragraph ("Descuentos: "+ impuestos)); 
            
            doc.add(new Paragraph ("Total Pagado: "+  propina+ "\n" + "\n" + "\n"));
            
            doc.add(new Paragraph ("Forma de pago: "+  formaPago));
            
            
            
            doc.close(); 

        } catch (DocumentException ex) {
            
            Logger.getLogger(ReporteCuenta.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("1");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteCuenta.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("2");
        }

    


    }
        public Long valorProducto(String nombreProducto, int cantidadProducto) {
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");

        //Se crean los controladores que se usaran en el metodo
        ProductoPedidoJpaController daoProductoPedido = new ProductoPedidoJpaController(emf);
        PedidoJpaController daoPedido = new PedidoJpaController(emf);
        ProductoJpaController daoProducto = new ProductoJpaController(emf);
        List<Producto> consulta = daoProducto.findProducto(nombreProducto);
        Long precioUnitario = consulta.get(0).getPrecio();
        Long precioProductos = precioUnitario * cantidadProducto;

        return precioProductos;
    }
        
        public String nombreCajero(String idEmpleado){
  
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");

            //Se crean los controladores que se usaran en el metodo
            ProductoPedidoJpaController daoProductoPedido = new ProductoPedidoJpaController(emf);
            //Se crean los controladores que se usaran en el metodo
            EmpleadoJpaController daoEmpleado = new EmpleadoJpaController(emf);
            String nombre=daoEmpleado.findEmpleado(idEmpleado).getNombre();
            String apellido=daoEmpleado.findEmpleado(idEmpleado).getApelllido();
            
            return nombre+" "+apellido;
        }
    
}
