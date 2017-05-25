/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Clases.Pedido;
import Clases.Producto;
import Clases.ProductoPedido;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paola
 */
public class ReporteCuenta {
    
    public void generarCuenta(List<ProductoPedido> productos,String numCuenta,Pedido pedido) {
        String numeroDelPedido = pedido.getNumPedido().toString();
        String numMesa = pedido.getNumMesa().getMesa().toString();
        String empleadoQueAtendio = pedido.getIdEmpleado().getNombre().toString();
        String estadoPedido = pedido.getIdEstadoPedido().getEstado().toString();
        String horaInicioPedido = pedido.getHoraInicioPedido().toString();
        String horaFinalPedido = pedido.getHoraFinalPedido().toString();

        DecimalFormat formatea = new DecimalFormat("###,###.##");
        try {

            OutputStream archivo = new FileOutputStream(new File("\\Reportes\\" + numCuenta + ".pdf"));
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            //lo que contendra el pdf
            Paragraph titulo = new Paragraph("+------------------------------------------+Cuenta+------------------------------------------+");
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            
            

            doc.add(new Paragraph("Empresa: Crepes & Waffles"));
            doc.add(new Paragraph("Numéro de cuenta: " + numCuenta));
            doc.add(new Paragraph("Numéro de pedido: " + numeroDelPedido));
            doc.add(new Paragraph("Numéro de mesa " + numMesa));
            doc.add(new Paragraph("Nombre del mesero " + empleadoQueAtendio));
            doc.add(new Paragraph("Hora de inicio del pedido: " + horaInicioPedido));
            doc.add(new Paragraph("Hora de fin del pedido: " + horaFinalPedido + "\n" + "\n" + "\n"));
            
            PdfPTable table = new PdfPTable(2);
            
            
            
        } catch (DocumentException ex) {
            Logger.getLogger(ReporteCuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReporteCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
