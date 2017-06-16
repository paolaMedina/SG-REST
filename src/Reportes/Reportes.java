/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Paola
 */
public class Reportes {

    private Connection con = null;
    Conexion conexion = null;

    public void reporte10Mejores(int mes, int año) {

        conexion = new Conexion();
        con = conexion.getConexion();
        try {
            JasperReport masterReport = null;
            File f = new File("Reportes/MasVendidos.jasper");
            try {
                masterReport = (JasperReport) JRLoader.loadObject(f);

            } catch (JRException ex) {
                System.out.println("error cargando reporte " + ex);
            }
            Map<String, Object> parametros = new HashMap();
            parametros.put("año", new Integer(año));
            parametros.put("mes", new Integer(mes));

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, con);

            JasperViewer jViewer = new JasperViewer(jasperPrint, false);
            jViewer.setTitle("10 items mas vendidos");
            jViewer.setVisible(true);
        } catch (JRException ex) {
            System.out.println("error generando reporte " + ex);
        }
    }

    public void menosDeseados(String semestre, int año) {

        conexion = new Conexion();
        con = conexion.getConexion();
        if (semestre.equals("1")) {
            
            try {
                JasperReport masterReport = null;
                File f = new File("Reportes/menosDeseados.jasper");
                try {
                    masterReport = (JasperReport) JRLoader.loadObject(f);

                } catch (JRException ex) {
                    System.out.println("error cargando reporte " + ex);
                }
                Map<String, Object> parametros = new HashMap();
                parametros.put("año", new Integer(año));
                JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, con);

                JasperViewer jViewer = new JasperViewer(jasperPrint, false);
                jViewer.setTitle("10 items menos vendidos");
                jViewer.setVisible(true);
            } catch (JRException ex) {
                System.out.println("error generando reporte " + ex);
            }
        }
        else{
            try {
                JasperReport masterReport = null;
                File f = new File("Reportes/menosDeseados2.jasper");
                try {
                    masterReport = (JasperReport) JRLoader.loadObject(f);

                } catch (JRException ex) {
                    System.out.println("error cargando reporte " + ex);
                }
                Map<String, Object> parametros = new HashMap();
                parametros.put("año",new Integer(año));

                JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, con);

                JasperViewer jViewer = new JasperViewer(jasperPrint, false);
                jViewer.setTitle("10 items menos vendidos");
                jViewer.setVisible(true);
            } catch (JRException ex) {
                System.out.println("error generando reporte " + ex);
            }
        }

    }
    
    public void promAtención(int mes, int año){
        
        int limitMes=0,limitAño=0;
        
        for (int i=0; i<6; i++){
            
            limitMes=mes-1;
        }
        if (limitMes<1){
            limitAño=año-1;
            if (limitMes==0){
                limitMes=12;
            }
            if (limitMes==-1){
                limitMes=11;
            }if (limitMes==-2){
                limitMes=10;
            }if (limitMes==-3){
                limitMes=9;
            }if (limitMes==-4){
                limitMes=8;
            }
            if (limitMes==-7){
                limitMes=12;
            }
            
        }
            
    }
    
    public void reporteCajaMensual(int mes, int año) {

        conexion = new Conexion();
        con = conexion.getConexion();
        try {
            JasperReport masterReport = null;
            File f = new File("Reportes/reporteCaja.jasper");
            try {
                masterReport = (JasperReport) JRLoader.loadObject(f);

            } catch (JRException ex) {
                System.out.println("error cargando reporte " + ex);
            }
            Map<String, Object> parametros = new HashMap();
            parametros.put("año", new Integer(año));
            parametros.put("mes", new Integer(mes));

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, con);

            JasperViewer jViewer = new JasperViewer(jasperPrint, false);
            jViewer.setTitle("Ingresos del mes");
            jViewer.setVisible(true);
        } catch (JRException ex) {
            System.out.println("error generando reporte " + ex);
        }
    }
    
    
    public void reporteEmpleadoMes( int año) {

        conexion = new Conexion();
        con = conexion.getConexion();
        try {
            JasperReport masterReport = null;
            File f = new File("Reportes/EmpleadoMes.jasper");
            try {
                masterReport = (JasperReport) JRLoader.loadObject(f);

            } catch (JRException ex) {
                System.out.println("error cargando reporte " + ex);
            }
            Map<String, Object> parametros = new HashMap();
            parametros.put("año", new Integer(año));

            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, con);

            JasperViewer jViewer = new JasperViewer(jasperPrint, false);
            jViewer.setTitle("Empleado de mes");
            jViewer.setVisible(true);
        } catch (JRException ex) {
            System.out.println("error generando reporte " + ex);
        }
    }

}
