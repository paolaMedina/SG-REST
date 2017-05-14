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
 * @author Daniel Galarza
 * @author Felipe Tellez
 * @author Paola Medina
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByIdFactura", query = "SELECT f FROM Factura f WHERE f.idFactura = :idFactura"),
    @NamedQuery(name = "Factura.findByValorTotal", query = "SELECT f FROM Factura f WHERE f.valorTotal = :valorTotal"),
    @NamedQuery(name = "Factura.findByIdEmpleado", query = "SELECT f FROM Factura f WHERE f.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "Factura.findByFechaHora", query = "SELECT f FROM Factura f WHERE f.fechaHora = :fechaHora"),
    @NamedQuery(name = "Factura.findByDescuento", query = "SELECT f FROM Factura f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "Factura.findByPropina", query = "SELECT f FROM Factura f WHERE f.propina = :propina"),
    @NamedQuery(name = "Factura.findByImpuestos", query = "SELECT f FROM Factura f WHERE f.impuestos = :impuestos"),
    @NamedQuery(name = "Factura.findByCedulaCliente", query = "SELECT f FROM Factura f WHERE f.cedulaCliente = :cedulaCliente")})
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura")
    private Integer idFactura;
    @Basic(optional = false)
    @Column(name = "valor_total")
    private long valorTotal;
    @Basic(optional = false)
    @Column(name = "id_empleado")
    private String idEmpleado;
    @Basic(optional = false)
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Column(name = "descuento")
    private long descuento;
    @Basic(optional = false)
    @Column(name = "propina")
    private long propina;
    @Basic(optional = false)
    @Column(name = "impuestos")
    private long impuestos;
    @Basic(optional = false)
    @Column(name = "cedula_cliente")
    private String cedulaCliente;
    @JoinColumn(name = "num_pedido", referencedColumnName = "num_pedido")
    @ManyToOne(optional = false)
    private Pedido numPedido;
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    @ManyToOne(optional = false)
    private Pago idPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factura")
    private Collection<ProductoFactura> productoFacturaCollection;

    public Factura() {
    }

    public Factura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Factura(Integer idFactura, long valorTotal, String idEmpleado, Date fechaHora, long descuento, long propina, long impuestos, String cedulaCliente) {
        this.idFactura = idFactura;
        this.valorTotal = valorTotal;
        this.idEmpleado = idEmpleado;
        this.fechaHora = fechaHora;
        this.descuento = descuento;
        this.propina = propina;
        this.impuestos = impuestos;
        this.cedulaCliente = cedulaCliente;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public long getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(long valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public long getDescuento() {
        return descuento;
    }

    public void setDescuento(long descuento) {
        this.descuento = descuento;
    }

    public long getPropina() {
        return propina;
    }

    public void setPropina(long propina) {
        this.propina = propina;
    }

    public long getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(long impuestos) {
        this.impuestos = impuestos;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public Pedido getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(Pedido numPedido) {
        this.numPedido = numPedido;
    }

    public Pago getIdPago() {
        return idPago;
    }

    public void setIdPago(Pago idPago) {
        this.idPago = idPago;
    }

    @XmlTransient
    public Collection<ProductoFactura> getProductoFacturaCollection() {
        return productoFacturaCollection;
    }

    public void setProductoFacturaCollection(Collection<ProductoFactura> productoFacturaCollection) {
        this.productoFacturaCollection = productoFacturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.idFactura == null && other.idFactura != null) || (this.idFactura != null && !this.idFactura.equals(other.idFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Factura[ idFactura=" + idFactura + " ]";
    }
    
}
