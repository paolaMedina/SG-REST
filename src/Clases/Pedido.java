/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findByNumPedido", query = "SELECT p FROM Pedido p WHERE p.numPedido = :numPedido"),
    @NamedQuery(name = "Pedido.findByHoraInicioPedido", query = "SELECT p FROM Pedido p WHERE p.horaInicioPedido = :horaInicioPedido"),
    @NamedQuery(name = "Pedido.findByHoraFinalPedido", query = "SELECT p FROM Pedido p WHERE p.horaFinalPedido = :horaFinalPedido")})
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "num_pedido")
    private Integer numPedido;
    @Basic(optional = false)
    @Column(name = "hora_inicio_pedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicioPedido;
    @Basic(optional = false)
    @Column(name = "hora_final_pedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFinalPedido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numPedido")
    private Collection<Factura> facturaCollection;
    @JoinColumn(name = "num_mesa", referencedColumnName = "num_mesa")
    @ManyToOne(optional = false)
    private Mesa numMesa;
    @JoinColumn(name = "id_estado_pedido", referencedColumnName = "id_estado_pedido")
    @ManyToOne(optional = false)
    private EstadoPedido idEstadoPedido;
    @JoinColumn(name = "id_empleado", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Collection<ProductoPedido> productoPedidoCollection;

    public Pedido() {
    }

    public Pedido(Integer numPedido) {
        this.numPedido = numPedido;
    }

    public Pedido(Integer numPedido, Date horaInicioPedido, Date horaFinalPedido) {
        this.numPedido = numPedido;
        this.horaInicioPedido = horaInicioPedido;
        this.horaFinalPedido = horaFinalPedido;
    }

    public Integer getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(Integer numPedido) {
        this.numPedido = numPedido;
    }

    public Date getHoraInicioPedido() {
        return horaInicioPedido;
    }

    public void setHoraInicioPedido(Date horaInicioPedido) {
        this.horaInicioPedido = horaInicioPedido;
    }

    public Date getHoraFinalPedido() {
        return horaFinalPedido;
    }

    public void setHoraFinalPedido(Date horaFinalPedido) {
        this.horaFinalPedido = horaFinalPedido;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public Mesa getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(Mesa numMesa) {
        this.numMesa = numMesa;
    }

    public EstadoPedido getIdEstadoPedido() {
        return idEstadoPedido;
    }

    public void setIdEstadoPedido(EstadoPedido idEstadoPedido) {
        this.idEstadoPedido = idEstadoPedido;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @XmlTransient
    public Collection<ProductoPedido> getProductoPedidoCollection() {
        return productoPedidoCollection;
    }

    public void setProductoPedidoCollection(Collection<ProductoPedido> productoPedidoCollection) {
        this.productoPedidoCollection = productoPedidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numPedido != null ? numPedido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.numPedido == null && other.numPedido != null) || (this.numPedido != null && !this.numPedido.equals(other.numPedido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Pedido[ numPedido=" + numPedido + " ]";
    }
    
}
