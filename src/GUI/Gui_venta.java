/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Clases.Empleado;
import Clases.Factura;
import Clases.Factura_;
import Clases.Pago;
import Clases.Pedido;
import Clases.Producto;
import Clases.ProductoFactura;
import Clases.ProductoFacturaPK;
import Clases.ProductoPedido;
import Clases.TipoPago;
import Controladores.FacturaJpaController;
import Controladores.PagoJpaController;
import Controladores.PedidoJpaController;
import Controladores.ProductoFacturaJpaController;
import Controladores.ProductoJpaController;
import Controladores.ProductoPedidoJpaController;
import Controladores.TipoPagoJpaController;
import Reportes.ReporteCuenta;
import java.awt.Image;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Paola
 */
public class Gui_venta extends javax.swing.JFrame {
    DefaultTableModel modeloTablaProductos = new DefaultTableModel();
    ArrayList<Long> pagosTarjetas = new ArrayList<Long>();
    List<ProductoPedido> productosPedido = null;
    Pedido pedido=null;
    
    
    long subtotal = 0;
    String tipoPago ="";
    String idEmpleado = "";
    int numTargetas=0;
    /**
     * Creates new form empleado
     */
    public Gui_venta(Gui_VentanaPrincipalCajero principal) {
        initComponents();
        
        deshabilitar();
        this.principal = principal;
        
    }
    
    public Pago generarPago(Long dineroEfectivo, Long dineroTargetas){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
        PagoJpaController daoPago = new PagoJpaController(emf);
        TipoPagoJpaController daoTipoPago = new TipoPagoJpaController(emf);
        
        //tipo de pago*********************************************************
        int tipo=1;
        if (this.tipoPago.equalsIgnoreCase("Efectivo")) {
            tipo = 1;
        }if (this.tipoPago.equalsIgnoreCase("Tarjeta")) {
            tipo = 2;
        }else{
            tipo = 3;
        }
            
        TipoPago tipoPago = daoTipoPago.findTipoPago(tipo);
        
        
        //pago*********************************************************
        Pago pago = new Pago();
        pago.setNumTarjetas(this.numTargetas);
        pago.setDineroEfectivo(dineroEfectivo);
        pago.setDineroTarjetas(dineroTargetas);
        pago.setIdTipo(tipoPago);
        
        daoPago.create(pago);
        return pago;
    }
    
    public void generarProductoFactura(){
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
        ProductoFacturaJpaController daoProductoFactura = new ProductoFacturaJpaController(emf);
        ProductoJpaController daoProducto = new ProductoJpaController(emf);
        FacturaJpaController daoFactura = new FacturaJpaController(emf);
        
        for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
            
        
            String nombreProducto = modeloTablaProductos.getValueAt(i, 0).toString();
            int cantidadProducto = Integer.parseInt(modeloTablaProductos.getValueAt(i, 1).toString());
            List<Producto> productos = daoProducto.findProducto(nombreProducto);
            int numVenta = Integer.parseInt(jLabelNumVenta.getText());

            Producto producto = productos.get(0);
            long precioUnitarioProducto = producto.getPrecio();
            int idProducto = producto.getProductoPK().getId();

            ProductoFactura productoFactura = new ProductoFactura();
                    
            ProductoFacturaPK productoFacturaPK = new ProductoFacturaPK(numVenta, idProducto);
            Factura factura = daoFactura.findFactura(Integer.parseInt(jLabelNumVenta.getText()));
            
            productoFactura.setFactura(factura);
            productoFactura.setNombreProducto(nombreProducto);
            productoFactura.setCantidad(cantidadProducto);
            productoFactura.setPrecio(precioUnitarioProducto);
            productoFactura.setProductoFacturaPK(productoFacturaPK);

            try {

                daoProductoFactura.create(productoFactura);
            } catch (NullPointerException ex) {

                JOptionPane.showMessageDialog(null, "Uno de los productos no se agrego satisfactoriamente a la factura", "Alerta!", JOptionPane.ERROR_MESSAGE);
                limpiar();
            } catch (Exception ex) {
                Logger.getLogger(Gui_empleado.class.getName()).log(Level.SEVERE, null, ex);
            }     

        }
                
    }

    public void generarFactura(Long dineroEfectivo, Long dineroTargetas, Long valorTotal){
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
        FacturaJpaController daoFactura = new FacturaJpaController(emf);
        PedidoJpaController daoPedido = new PedidoJpaController(emf);
        
        //datos de la factura
        String idCliente = this.jTextFieldIdCliente.getText();
        String tipoPago=this.tipoPago;
        int idFactura = Integer.parseInt(this.jLabelNumVenta.getText());
        int numeroPedido = Integer.parseInt(this.jLabelNumPedido.getText());
        Long valorT = valorTotal;
        
        String idEmpleado = this.principal.gui_login.usuario;
        Date fechaHora = new Date();
        long descuento =  Long.valueOf(jTextFieldDescuentos.getText());
        long propina =  Long.valueOf(jTextFieldPRopina.getText());
        long impuestos = 0;/*******************************************/
        String cedulaCliente = jTextFieldIdCliente.getText();
        
        Factura factura = new Factura(idFactura, valorT, idEmpleado, fechaHora, descuento, propina, impuestos, cedulaCliente);

        Pedido numPedido = daoPedido.findPedido(numeroPedido);
        factura.setNumPedido(numPedido);
        factura.setIdPago(this.generarPago(dineroEfectivo, dineroTargetas));
        
        try {
            daoFactura.create(factura);
            JOptionPane.showMessageDialog(null, "La factura se genero exitosamente", "Exito!", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se pudo generar la factura", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelPedido = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonTarjeta = new javax.swing.JRadioButton();
        jRadioButtonEfectivo = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldPagoTarjeta = new javax.swing.JTextField();
        jComboBoxTarjetas = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jButtonAgregarTarjetas = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPagoEfectivo = new javax.swing.JTextField();
        jButtonRealizarVenta = new javax.swing.JButton();
        jTextFieldNumTarjetas = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCheckBoxFijarNumTarjetas = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jButtonBuscar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButtonNuevo = new javax.swing.JButton();
        jButtonCuenta = new javax.swing.JButton();
        jLabelNumVenta = new javax.swing.JLabel();
        jPanelPedido2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableProductos2 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaInfoPedido2 = new javax.swing.JTextArea();
        jLabelSubTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldDescuentos = new javax.swing.JTextField();
        jCheckBoxPropina = new javax.swing.JCheckBox();
        jTextFieldPRopina = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jTextFieldIdCliente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelNumPedido = new javax.swing.JLabel();

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(51, 0, 255))); // NOI18N

        jLabel1.setText("Numero de la venta:");

        jPanelPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion del pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel3.setText("Forma de pago");

        jRadioButtonTarjeta.setText("Tarjeta");
        jRadioButtonTarjeta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonTarjetaItemStateChanged(evt);
            }
        });
        jRadioButtonTarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButtonTarjetaMouseClicked(evt);
            }
        });
        jRadioButtonTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonTarjetaActionPerformed(evt);
            }
        });

        jRadioButtonEfectivo.setText("Efectivo");
        jRadioButtonEfectivo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonEfectivoItemStateChanged(evt);
            }
        });
        jRadioButtonEfectivo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jRadioButtonEfectivoMouseClicked(evt);
            }
        });
        jRadioButtonEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonEfectivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jRadioButtonEfectivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonTarjeta)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonTarjeta)
                    .addComponent(jRadioButtonEfectivo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Pago con tarjeta"));
        jPanel5.setToolTipText("");

        jLabel5.setText("Cantidad de dinero:");

        jTextFieldPagoTarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPagoTarjetaKeyTyped(evt);
            }
        });

        jComboBoxTarjetas.setToolTipText("");

        jLabel8.setText("Tarjeta No:");

        jButtonAgregarTarjetas.setText("Agregar");
        jButtonAgregarTarjetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarTarjetasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldPagoTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAgregarTarjetas))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxTarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jComboBoxTarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldPagoTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAgregarTarjetas))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Pago en efectivo"));

        jLabel4.setText("Cantidad de dinero:");

        jTextFieldPagoEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPagoEfectivoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPagoEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPagoEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jButtonRealizarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/16 (Save).jpg"))); // NOI18N
        jButtonRealizarVenta.setText("Realizar venta");
        jButtonRealizarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRealizarVentaActionPerformed(evt);
            }
        });

        jLabel6.setText("Numero de tarjetas:");

        jCheckBoxFijarNumTarjetas.setText("Fijar Numero de Tarjetas");
        jCheckBoxFijarNumTarjetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxFijarNumTarjetasMouseClicked(evt);
            }
        });
        jCheckBoxFijarNumTarjetas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxFijarNumTarjetasStateChanged(evt);
            }
        });
        jCheckBoxFijarNumTarjetas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxFijarNumTarjetasItemStateChanged(evt);
            }
        });
        jCheckBoxFijarNumTarjetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFijarNumTarjetasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPedidoLayout = new javax.swing.GroupLayout(jPanelPedido);
        jPanelPedido.setLayout(jPanelPedidoLayout);
        jPanelPedidoLayout.setHorizontalGroup(
            jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPedidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPedidoLayout.createSequentialGroup()
                        .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelPedidoLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPedidoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jCheckBoxFijarNumTarjetas)
                            .addGroup(jPanelPedidoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldNumTarjetas)))
                        .addGap(60, 60, 60))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPedidoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRealizarVenta)
                .addGap(65, 65, 65))
        );
        jPanelPedidoLayout.setVerticalGroup(
            jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPedidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNumTarjetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGroup(jPanelPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPedidoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanelPedidoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxFijarNumTarjetas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRealizarVenta)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButtonBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/16 (Search).jpg"))); // NOI18N
        jButtonBuscar.setText("Buscar venta");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jButtonSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/16 (Delete).jpg"))); // NOI18N
        jButtonSalir.setText("salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Wzdelete.jpg"))); // NOI18N
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/3 anadir.png"))); // NOI18N
        jButtonNuevo.setText("nueva venta");
        jButtonNuevo.setToolTipText("");
        jButtonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoActionPerformed(evt);
            }
        });

        jButtonCuenta.setText("Generar Cuenta");
        jButtonCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCuentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCuenta)
                .addGap(120, 120, 120))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNuevo)
                    .addComponent(jButtonBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButtonCuenta)
                .addContainerGap())
        );

        jPanelPedido2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informacion de los productos de la venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        jTableProductos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTableProductos2);

        jTextAreaInfoPedido2.setEditable(false);
        jTextAreaInfoPedido2.setColumns(20);
        jTextAreaInfoPedido2.setRows(5);
        jScrollPane6.setViewportView(jTextAreaInfoPedido2);

        javax.swing.GroupLayout jPanelPedido2Layout = new javax.swing.GroupLayout(jPanelPedido2);
        jPanelPedido2.setLayout(jPanelPedido2Layout);
        jPanelPedido2Layout.setHorizontalGroup(
            jPanelPedido2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanelPedido2Layout.setVerticalGroup(
            jPanelPedido2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPedido2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel10.setText("Subtotal:");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion Venta"));

        jLabel7.setText("Descuentos:");

        jTextFieldDescuentos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDescuentosKeyTyped(evt);
            }
        });

        jCheckBoxPropina.setText("Propina");
        jCheckBoxPropina.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxPropinaMouseClicked(evt);
            }
        });

        jTextFieldPRopina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldPRopinaKeyTyped(evt);
            }
        });

        jLabel9.setText("TOTAL A PAGAR:");

        jTextFieldIdCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldIdClienteKeyTyped(evt);
            }
        });

        jLabel2.setText("Identificacion cliente:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCheckBoxPropina)
                            .addComponent(jLabel7))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPRopina, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxPropina, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldPRopina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        jLabel11.setText("Numero del pedido a facturar:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(155, 155, 155)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelPedido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelNumPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelNumVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelNumVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabelNumPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanelPedido2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelSubTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelNumVenta.getAccessibleContext().setAccessibleName("jLabelNumPedido");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(462, 462, 462))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        char car=evt.getKeyChar();
        if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>' ')) evt.consume();

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        // TODO add your handling code here:
        limpiar();
        
        //Se pide el numero del pedido del cual se quiere realizar la venta
        String numeroPedido = JOptionPane.showInputDialog(null, "Ingrese el numero del pedido del cual quiere realizar la venta", "Información", JOptionPane.QUESTION_MESSAGE);
        int numPedido = Integer.parseInt(numeroPedido);
        habilitar();
        
        
        
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
        
        //Se crean los controladores que se usaran en el metodo
        ProductoPedidoJpaController daoProductoPedido = new ProductoPedidoJpaController(emf);
        PedidoJpaController daoPedido = new PedidoJpaController(emf);
        ProductoJpaController daoProducto = new ProductoJpaController(emf);
        this.jLabelNumPedido.setText(numeroPedido);
        FacturaJpaController daoFactura = new FacturaJpaController(emf);
        String numVenta = Integer.toString(daoFactura.getFacturaCount() + 1);
            
        jLabelNumVenta.setText(numVenta);
        productosPedido = daoProductoPedido.findProductoPedidoEntities(numPedido);
        
      
        modeloTablaProductos.addColumn("Producto");
        modeloTablaProductos.addColumn("Cantidad");
        modeloTablaProductos.addColumn("Precio");
                 
        Object[] fila = null;      
               
        for (int i = 0; i < productosPedido.size(); i++) {
           
            String nombreProducto = productosPedido.get(i).getProductoPedidoPK().getProductoNombre();
            int cantidadProducto = productosPedido.get(i).getCatidad();
            
            modeloTablaProductos.addRow(fila);
            modeloTablaProductos.setValueAt(nombreProducto, i, 0);
            modeloTablaProductos.setValueAt(cantidadProducto, i, 1);
            
            List<Producto> consulta = daoProducto.findProducto(nombreProducto);
            Long precioUnitario = consulta.get(0).getPrecio();
            Long precioProductos = precioUnitario * cantidadProducto;
            
            modeloTablaProductos.setValueAt(precioProductos, i, 2);
            
            this.subtotal += precioProductos;
        }
        
        this.jTableProductos2.setModel(modeloTablaProductos);
      
        this.jLabelSubTotal.setText(String.valueOf(this.subtotal));
        
        pedido = daoPedido.findPedido(numPedido);
        
        String numeroDelPedido = pedido.getNumPedido().toString();
        String numMesa = pedido.getNumMesa().getMesa().toString();
        String empleadoQueAtendio = pedido.getIdEmpleado().getNombre().toString();
        String estadoPedido = pedido.getIdEstadoPedido().getEstado().toString();
        String horaInicioPedido = pedido.getHoraInicioPedido().toString();
        String horaFinalPedido = pedido.getHoraFinalPedido().toString();
        
        this.jTextAreaInfoPedido2.setText("INFORMACION DEL PEDIDO" + "\n" + "\n" +
                                            "Número Del Pedido: " + numeroDelPedido + "\n" + 
                                            "Número de mesa: " + numMesa +  "\n" +
                                            "Empleado que atendió el pedido: " + empleadoQueAtendio +  "\n" +
                                            "Hora de inicio del pedido: " + horaInicioPedido +  "\n" +
                                            "Hora de finalización del pedido: " + horaFinalPedido +  "\n" );  
        

    }//GEN-LAST:event_jButtonNuevoActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        botones();
        deshabilitar();
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        // TODO add your handling code here:
         try{
    
         principal.setVisible(true);
         this.dispose();
       }catch(Exception e){}
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        
        habilitar();
        this.jTextFieldPRopina.setEnabled(true);
        while(modeloTablaProductos.getColumnCount()== 0){  
            modeloTablaProductos.addColumn("Producto");
            modeloTablaProductos.addColumn("Cantidad");
            modeloTablaProductos.addColumn("Precio");
        }
        
        while(modeloTablaProductos.getRowCount()>0)modeloTablaProductos.removeRow(0);
        
        String numeroVenta = JOptionPane.showInputDialog(null, "Ingrese el numero de la venta que desea buscar", "Buscar", JOptionPane.QUESTION_MESSAGE);
        this.jLabelNumVenta.setText(numeroVenta);
        int numVenta = Integer.parseInt(numeroVenta);
        
        //Se crea en EntityManagerFactory con el nombre de nuestra unidad de persistencia
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
               
        PagoJpaController daoPago = new PagoJpaController(emf);
        FacturaJpaController daoFactura = new FacturaJpaController(emf);
        ProductoJpaController daoProducto = new ProductoJpaController(emf);
        
        ProductoFacturaJpaController daoProductoFactura = new ProductoFacturaJpaController(emf);
        
        try{
            List<ProductoFactura> productosFactura = daoProductoFactura.findProductoFacturaEntities();
            
            this.modeloTablaProductos = (DefaultTableModel) jTableProductos2.getModel();
            
            for (int i = 0; i < productosFactura.size(); i++) {
                
                int idProducto = productosFactura.get(i).getProductoFacturaPK().getIdProducto();
                String nombreProducto = daoProducto.findProducto(idProducto).get(0).getProductoPK().getNombre();
                
               
                Vector<String> vector = new Vector<String>();
                               
                vector.add(nombreProducto);
                String cantidad = Integer.toString(productosFactura.get(i).getCantidad());
                vector.add(cantidad);
                String precioUnitario = Long.toString(productosFactura.get(i).getPrecio());
                
                this.modeloTablaProductos.addRow(vector);
            }
            
            Pago pago = daoPago.findPago(numVenta);
            
            if(pago.getIdTipo().getNombreTipo().equalsIgnoreCase("Efectivo")){
                this.jRadioButtonEfectivo.doClick();
                this.jTextFieldPagoEfectivo.setEnabled(true);
                this.jTextFieldPagoEfectivo.setText(Long.toString(pago.getDineroEfectivo()));
            }else if(pago.getIdTipo().getNombreTipo().equalsIgnoreCase("Tarjeta")){
                this.jRadioButtonTarjeta.doClick();
                this.jTextFieldNumTarjetas.setEnabled(true);
                this.jTextFieldNumTarjetas.setText(Integer.toString(pago.getNumTarjetas()));
                this.jTextFieldPagoTarjeta.setEnabled(true);
                this.jTextFieldPagoTarjeta.setText(Long.toString(pago.getDineroTarjetas()));
            }else if(pago.getIdTipo().getNombreTipo().equalsIgnoreCase("Mixto")){
                this.jRadioButtonEfectivo.doClick();
                this.jRadioButtonTarjeta.doClick();
                this.jTextFieldNumTarjetas.setEnabled(true);
                this.jTextFieldNumTarjetas.setText(Integer.toString(pago.getNumTarjetas()));
                this.jTextFieldPagoEfectivo.setEnabled(true);
                this.jTextFieldPagoEfectivo.setText(Long.toString(pago.getDineroEfectivo()));
                this.jTextFieldPagoTarjeta.setEnabled(true);
                this.jTextFieldPagoTarjeta.setText(Long.toString(pago.getDineroTarjetas()));
            }
            
            Factura factura = daoFactura.findFactura(numVenta);
            
            this.jTextFieldPRopina.setText(Long.toString(factura.getPropina()));
            this.jTextFieldDescuentos.setText(Long.toString(factura.getDescuento()));
            this.jTextFieldIdCliente.setText(factura.getCedulaCliente());
            this.jLabelTotal.setText(Long.toString(factura.getValorTotal()));
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "La factura de la venta no existe", "Error", JOptionPane.ERROR_MESSAGE);

        }
        
        
        

        
        
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jCheckBoxFijarNumTarjetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFijarNumTarjetasActionPerformed

    }//GEN-LAST:event_jCheckBoxFijarNumTarjetasActionPerformed

    private void jCheckBoxFijarNumTarjetasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxFijarNumTarjetasItemStateChanged

    }//GEN-LAST:event_jCheckBoxFijarNumTarjetasItemStateChanged

    private void jCheckBoxFijarNumTarjetasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxFijarNumTarjetasStateChanged

    }//GEN-LAST:event_jCheckBoxFijarNumTarjetasStateChanged

    private void jCheckBoxFijarNumTarjetasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxFijarNumTarjetasMouseClicked

        jButtonAgregarTarjetas.setEnabled(true);

        if(jCheckBoxFijarNumTarjetas.isSelected()){
            int numeroTarjetas = Integer.parseInt(this.jTextFieldNumTarjetas.getText());
            this.numTargetas = numeroTarjetas;
            this.jTextFieldNumTarjetas.setEnabled(false);
            this.jCheckBoxFijarNumTarjetas.setEnabled(false);

            for (int i = 0; i < numeroTarjetas; i++) {
                int index = i+1;
                this.jComboBoxTarjetas.addItem("Tarjeta No."+ index );
                pagosTarjetas.add(Long.valueOf(0));
            }

        }else{
            this.jTextFieldNumTarjetas.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckBoxFijarNumTarjetasMouseClicked

    private void jButtonRealizarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRealizarVentaActionPerformed

        try{
            if (verificarCamposVacios() == false) {

                //Creacion de la persistencia
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
                String idCliente = this.jTextFieldIdCliente.getText();
                String tipoPago=null;
                int numeroTarjetas = 0;
                long cantidadDineroEfectivo = 0;
                long cantidadDineroTarjetas = 0;
                long propina = Integer.parseInt(jTextFieldPRopina.getText());
                long descuentos  = Integer.parseInt(jTextFieldDescuentos.getText());
                long dineroTotalUsuario = 0;
                long totalPagar = 0;

                JOptionPane.showMessageDialog(null, this.jRadioButtonEfectivo.isSelected());

                if(this.jRadioButtonEfectivo.isSelected() && this.jRadioButtonTarjeta.isSelected()){
                    tipoPago = "Mixto";
                    numeroTarjetas = Integer.parseInt(this.jTextFieldNumTarjetas.getText());
                    cantidadDineroEfectivo = Long.parseLong(jTextFieldPagoEfectivo.getText());

                    for (int i = 0; i < this.pagosTarjetas.size(); i++) {
                        cantidadDineroTarjetas += pagosTarjetas.get(i);
                    }

                    dineroTotalUsuario = cantidadDineroEfectivo + cantidadDineroTarjetas;

                    totalPagar = this.subtotal + propina - descuentos;
                   
                    if(!jTextFieldPRopina.getText().equalsIgnoreCase("") && !jTextFieldDescuentos.getText().equalsIgnoreCase("")){
                        this.jLabelTotal.setText(Long.toString(totalPagar));
                    }

                    if (dineroTotalUsuario < totalPagar) {
                        JOptionPane.showMessageDialog(null, "La cantidad de dinero ingresada es menor que el total a pagar", "Error!", JOptionPane.ERROR_MESSAGE);
                        limpiar();
                    }else{
                        generarFactura(cantidadDineroEfectivo, cantidadDineroTarjetas, totalPagar);
                        generarProductoFactura();
                        limpiar();
                        deshabilitar();
                    }

                }
                else if(this.jRadioButtonEfectivo.isSelected()){
                    tipoPago = "Efectivo";
                    numeroTarjetas = 0;
                    cantidadDineroEfectivo = Long.parseLong(jTextFieldPagoEfectivo.getText());
                    dineroTotalUsuario = cantidadDineroEfectivo;
                    totalPagar = this.subtotal + propina - descuentos;
                    if(!jTextFieldPRopina.getText().equalsIgnoreCase("") && !jTextFieldDescuentos.getText().equalsIgnoreCase("")){
                        this.jLabelTotal.setText(Long.toString(totalPagar));
                    }


                    if (dineroTotalUsuario < totalPagar) {
                        JOptionPane.showMessageDialog(null, "La cantidad de dinero ingresada es menor que el total a pagar", "Error!", JOptionPane.ERROR_MESSAGE);
                        limpiar();
                    }else{
                        generarFactura(cantidadDineroEfectivo, cantidadDineroTarjetas, totalPagar);
                        generarProductoFactura();
                        limpiar();
                        deshabilitar();
                    }

                }else if(this.jRadioButtonTarjeta.isSelected()){
                    tipoPago = "Tarjeta";
                    numeroTarjetas = Integer.parseInt(this.jTextFieldNumTarjetas.getText());

                    for (int i = 0; i < this.pagosTarjetas.size(); i++) {
                        cantidadDineroTarjetas += pagosTarjetas.get(i);
                    }

                    dineroTotalUsuario = cantidadDineroTarjetas;
                    totalPagar = this.subtotal + propina - descuentos;
                    if(!jTextFieldPRopina.getText().equalsIgnoreCase("") && !jTextFieldDescuentos.getText().equalsIgnoreCase("")){
                        this.jLabelTotal.setText(Long.toString(totalPagar));
                    }


                    if (dineroTotalUsuario < totalPagar) {
                        JOptionPane.showMessageDialog(null, "La cantidad de dinero ingresada es menor que el total a pagar", "Error!", JOptionPane.ERROR_MESSAGE);
                        limpiar();
                    }else{
                        generarFactura(cantidadDineroEfectivo, cantidadDineroTarjetas, totalPagar);
                        generarProductoFactura();
                        limpiar();
                        deshabilitar();
                    }
                }
            }
        }finally{

        }
    }//GEN-LAST:event_jButtonRealizarVentaActionPerformed

    private void jCheckBoxPropinaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxPropinaMouseClicked
        if(jCheckBoxPropina.isSelected()){
            this.jTextFieldPRopina.setEnabled(true);
        }else{
            this.jTextFieldPRopina.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxPropinaMouseClicked

    private void jButtonAgregarTarjetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarTarjetasActionPerformed

        Long pagoTarjeta = Long.parseLong(this.jTextFieldPagoTarjeta.getText());
        int index = this.jComboBoxTarjetas.getSelectedIndex();

        pagosTarjetas.set(index, pagoTarjeta);
        
        this.jTextFieldPagoTarjeta.setText("");

    }//GEN-LAST:event_jButtonAgregarTarjetasActionPerformed

    private void jRadioButtonEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEfectivoActionPerformed

    }//GEN-LAST:event_jRadioButtonEfectivoActionPerformed

    private void jRadioButtonEfectivoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButtonEfectivoMouseClicked
        if(this.jRadioButtonEfectivo.isSelected()){
            this.jTextFieldPagoEfectivo.setEnabled(true);
           
        }else{
            this.jTextFieldPagoEfectivo.setEnabled(false);
        }

       
    }//GEN-LAST:event_jRadioButtonEfectivoMouseClicked

    private void jRadioButtonEfectivoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonEfectivoItemStateChanged

    }//GEN-LAST:event_jRadioButtonEfectivoItemStateChanged

    private void jRadioButtonTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonTarjetaActionPerformed

    }//GEN-LAST:event_jRadioButtonTarjetaActionPerformed

    private void jRadioButtonTarjetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButtonTarjetaMouseClicked
        if(this.jRadioButtonTarjeta.isSelected()){
            this.jTextFieldPagoTarjeta.setEnabled(true);
            this.jTextFieldNumTarjetas.setEnabled(true);
            this.jCheckBoxFijarNumTarjetas.setEnabled(true);
            this.jComboBoxTarjetas.setEnabled(true);
                     

        }else{
            this.jTextFieldPagoTarjeta.setEnabled(false);
            this.jTextFieldNumTarjetas.setEnabled(false);
            this.jCheckBoxFijarNumTarjetas.setEnabled(false);
            this.jComboBoxTarjetas.setEnabled(false);
        }

      

    }//GEN-LAST:event_jRadioButtonTarjetaMouseClicked

    private void jRadioButtonTarjetaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonTarjetaItemStateChanged

    }//GEN-LAST:event_jRadioButtonTarjetaItemStateChanged

    private void jTextFieldPagoEfectivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPagoEfectivoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPagoEfectivoKeyTyped

    private void jTextFieldPagoTarjetaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPagoTarjetaKeyTyped
        char c = evt.getKeyChar();
                  
        if(Character.isLetter(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPagoTarjetaKeyTyped

    private void jTextFieldIdClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldIdClienteKeyTyped
        char c = evt.getKeyChar();
                  
        if(Character.isLetter(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldIdClienteKeyTyped

    private void jTextFieldPRopinaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPRopinaKeyTyped
        char c = evt.getKeyChar();
                  
        if(Character.isLetter(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldPRopinaKeyTyped

    private void jTextFieldDescuentosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDescuentosKeyTyped
        char c = evt.getKeyChar();
                  
        if(Character.isLetter(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldDescuentosKeyTyped

    private void jButtonCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCuentaActionPerformed
        // TODO add your handling code here:
        ReporteCuenta cuenta= new ReporteCuenta();
        cuenta.generarCuenta(productosPedido, this.jLabelNumVenta.getText(), pedido);
        
        
        
    }//GEN-LAST:event_jButtonCuentaActionPerformed

    public void botones(){
        this.jButtonRealizarVenta.setEnabled(false);
        this.jButtonNuevo.setEnabled(true);
        this.jButtonBuscar.setEnabled(true);
        //this.jButtonRealizarVenta.setEnabled(false);
      
    }
    
    public void limpiar(){
        this.jTextFieldIdCliente.setText("");
        this.jTextFieldPagoEfectivo.setText("");
        this.jTextFieldPagoTarjeta.setText("");
        this.jTextFieldDescuentos.setText("");
        this.jTextFieldNumTarjetas.setText("");
        this.jTextAreaInfoPedido2.setText("");
        this.jLabelNumVenta.setText("");
        this.jTextFieldPRopina.setText("");
        while(modeloTablaProductos.getRowCount()>0)modeloTablaProductos.removeRow(0);
        
        this.jCheckBoxFijarNumTarjetas.setSelected(false);
        this.jRadioButtonTarjeta.setSelected(false);
        this.jRadioButtonEfectivo.setSelected(false);
        this.jCheckBoxPropina.setSelected(false);
        
    }
    
    
     public void habilitar(){
         
        this.jTextFieldIdCliente.setEnabled(true);
        this.jRadioButtonTarjeta.setEnabled(true);
        this.jRadioButtonEfectivo.setEnabled(true);
        this.jCheckBoxPropina.setEnabled(true);
        this.jLabelNumVenta.setEnabled(true);
        
        this.jTextFieldDescuentos.setEnabled(true);
           
    }
     
    public void deshabilitar(){
        this.jLabelNumVenta.setEnabled(false);
        this.jTextFieldIdCliente.setEnabled(false);
        this.jTextFieldDescuentos.setEnabled(false);
        this.jTextFieldNumTarjetas.setEnabled(false);
        this.jCheckBoxFijarNumTarjetas.setEnabled(false);
        this.jComboBoxTarjetas.setEnabled(false);
        this.jButtonAgregarTarjetas.setEnabled(false);
        this.jTextFieldPRopina.setEnabled(false);
        
        this.jRadioButtonTarjeta.setEnabled(false);
        this.jRadioButtonEfectivo.setEnabled(false);
        this.jCheckBoxPropina.setEnabled(false);
        jTextFieldPagoEfectivo.setEnabled(false);
        jTextFieldPagoTarjeta.setEnabled(false);
    }
    public boolean verificarCamposVacios(){
        boolean var = false;
        if (jTextFieldIdCliente.getText().equalsIgnoreCase("")) {
            var = true;             
        }
         
        return var;
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui_venta(new Gui_VentanaPrincipalCajero (new Gui_login())).setVisible(true);
            }
        });
    }

    private Gui_VentanaPrincipalCajero principal;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonAgregarTarjetas;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonCuenta;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonRealizarVenta;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JCheckBox jCheckBoxFijarNumTarjetas;
    private javax.swing.JCheckBox jCheckBoxPropina;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBoxTarjetas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelNumPedido;
    private javax.swing.JLabel jLabelNumVenta;
    private javax.swing.JLabel jLabelSubTotal;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelPedido;
    private javax.swing.JPanel jPanelPedido2;
    private javax.swing.JRadioButton jRadioButtonEfectivo;
    private javax.swing.JRadioButton jRadioButtonTarjeta;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableProductos2;
    private javax.swing.JTextArea jTextAreaInfoPedido2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextFieldDescuentos;
    private javax.swing.JTextField jTextFieldIdCliente;
    private javax.swing.JTextField jTextFieldNumTarjetas;
    private javax.swing.JTextField jTextFieldPRopina;
    private javax.swing.JTextField jTextFieldPagoEfectivo;
    private javax.swing.JTextField jTextFieldPagoTarjeta;
    // End of variables declaration//GEN-END:variables
}
