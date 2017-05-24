

 
/**************************************************/
//Hector Fabio Padilla Vasco        - 201323335
//Juan Daniel Galarza               - 201323966
//Felipe Tellez Acosta              - 201331425
//Angie Paola Medina Cordoba        - 2029233
/**************************************************/

/**************************************************/
package GUI;
/**************************************************/

/**************************************************/
//Declaracion de la clase
public class Gui_otros extends javax.swing.JFrame {

    /**************************************************/
    //Atributos
    private Gui_VentanaPrincipalGerente gui_Menu_Principal_Gerente=null;
    private Gui_VentanaPrincipalMesero gui_Menu_Principal_Mesero=null;
    private Gui_VentanaPrincipalCajero gui_Menu_Principal_Cajero=null;
    
    
    /**************************************************/
    //Constructor
    public Gui_otros(Gui_VentanaPrincipalGerente gui_Menu_Principal) {
        this.initComponents();
        this.gui_Menu_Principal_Gerente = gui_Menu_Principal; 
        this.setLocationRelativeTo(null);
    }
    public Gui_otros(Gui_VentanaPrincipalMesero gui_Menu_Principal) {
        this.initComponents();
        this.gui_Menu_Principal_Mesero = gui_Menu_Principal; 
        this.setLocationRelativeTo(null);
    }
    public Gui_otros(Gui_VentanaPrincipalCajero gui_Menu_Principal) {
        this.initComponents();
        this.gui_Menu_Principal_Cajero = gui_Menu_Principal; 
        this.setLocationRelativeTo(null);
    }
    /**************************************************/

    
    /**************************************************/
    //Metodo que inicia y carga los componentes de manera adecuada
    //  -> initComponents -> void
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panel_Otros = new javax.swing.JPanel();
        label_Titulo = new javax.swing.JLabel();
        label_Seleccione = new javax.swing.JLabel();
        comboBox_Otros = new javax.swing.JComboBox();
        boton_Volver = new javax.swing.JButton();
        boton_Aceptar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        label_Titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono_Sesion.jpg"))); // NOI18N

        label_Seleccione.setFont(new java.awt.Font("Microsoft Sans Serif", 2, 24)); // NOI18N
        label_Seleccione.setForeground(new java.awt.Color(255, 0, 0));
        label_Seleccione.setText("Seleccione");

        comboBox_Otros.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        comboBox_Otros.setForeground(new java.awt.Color(255, 0, 0));
        comboBox_Otros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Iniciar sesión con otra cuenta", "Cerrar sesión y aplicación" }));

        boton_Volver.setBackground(new java.awt.Color(255, 153, 153));
        boton_Volver.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        boton_Volver.setForeground(new java.awt.Color(255, 0, 0));
        boton_Volver.setText("Volver");
        boton_Volver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton_VolverMousePressed(evt);
            }
        });

        boton_Aceptar.setBackground(new java.awt.Color(255, 153, 153));
        boton_Aceptar.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        boton_Aceptar.setForeground(new java.awt.Color(255, 0, 0));
        boton_Aceptar.setText("Aceptar");
        boton_Aceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                boton_AceptarMousePressed(evt);
            }
        });

        javax.swing.GroupLayout panel_OtrosLayout = new javax.swing.GroupLayout(panel_Otros);
        panel_Otros.setLayout(panel_OtrosLayout);
        panel_OtrosLayout.setHorizontalGroup(
            panel_OtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtrosLayout.createSequentialGroup()
                .addGroup(panel_OtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_OtrosLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(panel_OtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_Titulo)
                            .addGroup(panel_OtrosLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(boton_Volver)
                                .addGap(28, 28, 28)
                                .addComponent(boton_Aceptar))
                            .addComponent(comboBox_Otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_OtrosLayout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(label_Seleccione)))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        panel_OtrosLayout.setVerticalGroup(
            panel_OtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OtrosLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(label_Titulo)
                .addGap(66, 66, 66)
                .addComponent(label_Seleccione)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboBox_Otros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_OtrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_Volver)
                    .addComponent(boton_Aceptar))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_Otros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_Otros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**************************************************/
    
    
    /**************************************************/
    //Evento que se encarga de ejecutar la opcion seleccionada por el usuario logueado
    //MouseEvent -> boton_AceptarMousePressed -> void
    private void boton_AceptarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_AceptarMousePressed
        
        this.setVisible(false); 
        
        int opcion = this.comboBox_Otros.getSelectedIndex();
        
        switch( opcion ) {
        
            case 0:
                if (gui_Menu_Principal_Gerente != null) {
                    this.gui_Menu_Principal_Gerente.gui_login.setVisible(true);
                    this.dispose();
                }else if (gui_Menu_Principal_Cajero != null) {
                    this.gui_Menu_Principal_Cajero.gui_login.setVisible(true);
                    this.dispose();
                }else{
                    this.gui_Menu_Principal_Mesero.gui_login.setVisible(true);
                    this.dispose();
                }
                    break;
                
            case 1: 
                System.exit(1);
                    break; 
        
        }
        
    }//GEN-LAST:event_boton_AceptarMousePressed
    /**************************************************/
    
    
    /**************************************************/
    //Evento que se encarga de regresar a la GUI antecesora de esta
    //MouseEvent -> boton_VolverMousePressed -> void
    private void boton_VolverMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boton_VolverMousePressed
  
        if (gui_Menu_Principal_Gerente != null) {
                    this.gui_Menu_Principal_Gerente.setVisible(true);
                    this.dispose();
                }else if (gui_Menu_Principal_Cajero != null) {
                    this.gui_Menu_Principal_Cajero.setVisible(true);
                    this.dispose();
                }else{
                    this.gui_Menu_Principal_Mesero.setVisible(true);
                    this.dispose();
                }
        
    }//GEN-LAST:event_boton_VolverMousePressed
    /**************************************************/
    
    
    /**************************************************/
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Gui_otros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gui_otros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gui_otros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gui_otros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui_otros( new Gui_VentanaPrincipalGerente(new Gui_login()) ).setVisible(true);
            } 
        });
    }
    /**************************************************/
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_Aceptar;
    private javax.swing.JButton boton_Volver;
    private javax.swing.JComboBox comboBox_Otros;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label_Seleccione;
    private javax.swing.JLabel label_Titulo;
    private javax.swing.JPanel panel_Otros;
    // End of variables declaration//GEN-END:variables
    /**************************************************/
    
    
}
/**************************************************/