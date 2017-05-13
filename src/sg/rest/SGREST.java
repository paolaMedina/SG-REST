/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.rest;

import Clases.CargoEmpleado;
import Clases.Empleado;
import Controladores.CargoEmpleadoJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class SGREST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SG-RESTPU");
        CargoEmpleadoJpaController dao = new CargoEmpleadoJpaController(emf);
        
        CargoEmpleado empleado = dao.findCargoEmpleado(1);
        empleado.getIdCargo();
        System.err.println(empleado.getIdCargo());
        
    }
    
}
