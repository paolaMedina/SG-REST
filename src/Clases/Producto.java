/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findById", query = "SELECT p FROM Producto p WHERE p.productoPK.id = :id"),
    @NamedQuery(name = "Producto.findByNombre", query = "SELECT p FROM Producto p WHERE p.productoPK.nombre = :nombre"),
    @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio"),
    @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Producto.findByEstado", query = "SELECT p FROM Producto p WHERE p.estado = :estado"),
    @NamedQuery(name = "Producto.findByFotografia", query = "SELECT p FROM Producto p WHERE p.fotografia = :fotografia")})
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoPK productoPK;
    @Basic(optional = false)
    @Column(name = "precio")
    private long precio;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "fotografia")
    private String fotografia;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false)
    private CategoriaProducto idCategoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private Collection<ProductoPedido> productoPedidoCollection;

    public Producto() {
    }

    public Producto(ProductoPK productoPK) {
        this.productoPK = productoPK;
    }

    public Producto(ProductoPK productoPK, long precio, boolean estado) {
        this.productoPK = productoPK;
        this.precio = precio;
        this.estado = estado;
    }

    public Producto(int id, String nombre) {
        this.productoPK = new ProductoPK(id, nombre);
    }

    public ProductoPK getProductoPK() {
        return productoPK;
    }

    public void setProductoPK(ProductoPK productoPK) {
        this.productoPK = productoPK;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public CategoriaProducto getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(CategoriaProducto idCategoria) {
        this.idCategoria = idCategoria;
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
        hash += (productoPK != null ? productoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.productoPK == null && other.productoPK != null) || (this.productoPK != null && !this.productoPK.equals(other.productoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Producto[ productoPK=" + productoPK + " ]";
    }
    
}
