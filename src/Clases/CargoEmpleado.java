/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel Galarza
 * @author Felipe Tellez
 * @author Paola Medina
 * 
 */
@Entity
@Table(name = "cargo_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargoEmpleado.findAll", query = "SELECT c FROM CargoEmpleado c"),
    @NamedQuery(name = "CargoEmpleado.findByIdCargo", query = "SELECT c FROM CargoEmpleado c WHERE c.idCargo = :idCargo"),
    @NamedQuery(name = "CargoEmpleado.findByCargo", query = "SELECT c FROM CargoEmpleado c WHERE c.cargo = :cargo")})
public class CargoEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cargo")
    private Integer idCargo;
    @Basic(optional = false)
    @Column(name = "cargo")
    private String cargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargo")
    private Collection<Empleado> empleadoCollection;

    public CargoEmpleado() {
    }

    public CargoEmpleado(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public CargoEmpleado(Integer idCargo, String cargo) {
        this.idCargo = idCargo;
        this.cargo = cargo;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @XmlTransient
    public Collection<Empleado> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<Empleado> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCargo != null ? idCargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargoEmpleado)) {
            return false;
        }
        CargoEmpleado other = (CargoEmpleado) object;
        if ((this.idCargo == null && other.idCargo != null) || (this.idCargo != null && !this.idCargo.equals(other.idCargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.CargoEmpleado[ idCargo=" + idCargo + " ]";
    }
    
}
