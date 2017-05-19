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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByIdentificacion", query = "SELECT e FROM Empleado e WHERE e.identificacion = :identificacion"),
    @NamedQuery(name = "Empleado.findByPassword", query = "SELECT e FROM Empleado e WHERE e.password = :password"),
    @NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleado.findByApelllido", query = "SELECT e FROM Empleado e WHERE e.apelllido = :apelllido"),
    @NamedQuery(name = "Empleado.findByEstado", query = "SELECT e FROM Empleado e WHERE e.estado = :estado"),
    @NamedQuery(name = "Empleado.findByHorario", query = "SELECT e FROM Empleado e WHERE e.horario = :horario"),
    @NamedQuery(name = "Empleado.findByDireccion", query = "SELECT e FROM Empleado e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "Empleado.findByTelefonoFijo", query = "SELECT e FROM Empleado e WHERE e.telefonoFijo = :telefonoFijo"),
    @NamedQuery(name = "Empleado.findByTelefonoCelular", query = "SELECT e FROM Empleado e WHERE e.telefonoCelular = :telefonoCelular"),
    @NamedQuery(name = "Empleado.findByEmail", query = "SELECT e FROM Empleado e WHERE e.email = :email"),
    @NamedQuery(name = "Empleado.findByFoto", query = "SELECT e FROM Empleado e WHERE e.foto = :foto")})
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificacion")
    private String identificacion;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apelllido")
    private String apelllido;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "horario")
    private String horario;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "telefono_fijo")
    private String telefonoFijo;
    @Basic(optional = false)
    @Column(name = "telefono_celular")
    private String telefonoCelular;
    @Column(name = "email")
    private String email;
    @Column(name = "foto")
    private String foto;
    @JoinColumn(name = "cargo", referencedColumnName = "id_cargo")
    @ManyToOne(optional = false)
    private CargoEmpleado cargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleado")
    private Collection<HorarioEmpleado> horarioEmpleadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private Collection<Pedido> pedidoCollection;

    public Empleado() {
    }

    public Empleado(String identificacion) {
        this.identificacion = identificacion;
    }

    public Empleado(String identificacion, String password, String nombre, String apelllido, boolean estado, String horario, String telefonoCelular) {
        this.identificacion = identificacion;
        this.password = password;
        this.nombre = nombre;
        this.apelllido = apelllido;
        this.estado = estado;
        this.horario = horario;
        this.telefonoCelular = telefonoCelular;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApelllido() {
        return apelllido;
    }

    public void setApelllido(String apelllido) {
        this.apelllido = apelllido;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public CargoEmpleado getCargo() {
        return cargo;
    }

    public void setCargo(CargoEmpleado cargo) {
        this.cargo = cargo;
    }

    @XmlTransient
    public Collection<HorarioEmpleado> getHorarioEmpleadoCollection() {
        return horarioEmpleadoCollection;
    }

    public void setHorarioEmpleadoCollection(Collection<HorarioEmpleado> horarioEmpleadoCollection) {
        this.horarioEmpleadoCollection = horarioEmpleadoCollection;
    }

    @XmlTransient
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificacion != null ? identificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.identificacion == null && other.identificacion != null) || (this.identificacion != null && !this.identificacion.equals(other.identificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Empleado[ identificacion=" + identificacion + " ]";
    }
    
}
