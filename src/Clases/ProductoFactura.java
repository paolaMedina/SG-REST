/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "producto_factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoFactura.findAll", query = "SELECT p FROM ProductoFactura p"),
    @NamedQuery(name = "ProductoFactura.findByIdFactura", query = "SELECT p FROM ProductoFactura p WHERE p.productoFacturaPK.idFactura = :idFactura"),
    @NamedQuery(name = "ProductoFactura.findByIdProducto", query = "SELECT p FROM ProductoFactura p WHERE p.productoFacturaPK.idProducto = :idProducto"),
    @NamedQuery(name = "ProductoFactura.findByNombreProducto", query = "SELECT p FROM ProductoFactura p WHERE p.nombreProducto = :nombreProducto"),
    @NamedQuery(name = "ProductoFactura.findByPrecio", query = "SELECT p FROM ProductoFactura p WHERE p.precio = :precio"),
    @NamedQuery(name = "ProductoFactura.findByCantidad", query = "SELECT p FROM ProductoFactura p WHERE p.cantidad = :cantidad")})
public class ProductoFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoFacturaPK productoFacturaPK;
    @Basic(optional = false)
    @Column(name = "nombre_producto")
    private String nombreProducto;
    @Basic(optional = false)
    @Column(name = "precio")
    private long precio;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Factura factura;

    public ProductoFactura() {
    }

    public ProductoFactura(ProductoFacturaPK productoFacturaPK) {
        this.productoFacturaPK = productoFacturaPK;
    }

    public ProductoFactura(ProductoFacturaPK productoFacturaPK, String nombreProducto, long precio, int cantidad) {
        this.productoFacturaPK = productoFacturaPK;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public ProductoFactura(int idFactura, int idProducto) {
        this.productoFacturaPK = new ProductoFacturaPK(idFactura, idProducto);
    }

    public ProductoFacturaPK getProductoFacturaPK() {
        return productoFacturaPK;
    }

    public void setProductoFacturaPK(ProductoFacturaPK productoFacturaPK) {
        this.productoFacturaPK = productoFacturaPK;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoFacturaPK != null ? productoFacturaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoFactura)) {
            return false;
        }
        ProductoFactura other = (ProductoFactura) object;
        if ((this.productoFacturaPK == null && other.productoFacturaPK != null) || (this.productoFacturaPK != null && !this.productoFacturaPK.equals(other.productoFacturaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.ProductoFactura[ productoFacturaPK=" + productoFacturaPK + " ]";
    }
    
}
