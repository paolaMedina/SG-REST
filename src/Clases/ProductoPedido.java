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
import javax.persistence.JoinColumns;
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
@Table(name = "producto_pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoPedido.findAll", query = "SELECT p FROM ProductoPedido p"),
    @NamedQuery(name = "ProductoPedido.findByPedidoNumPedido", query = "SELECT p FROM ProductoPedido p WHERE p.productoPedidoPK.pedidoNumPedido = :pedidoNumPedido"),
    @NamedQuery(name = "ProductoPedido.findByProductoId", query = "SELECT p FROM ProductoPedido p WHERE p.productoPedidoPK.productoId = :productoId"),
    @NamedQuery(name = "ProductoPedido.findByProductoNombre", query = "SELECT p FROM ProductoPedido p WHERE p.productoPedidoPK.productoNombre = :productoNombre"),
    @NamedQuery(name = "ProductoPedido.findByCatidad", query = "SELECT p FROM ProductoPedido p WHERE p.catidad = :catidad")})
public class ProductoPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductoPedidoPK productoPedidoPK;
    @Basic(optional = false)
    @Column(name = "catidad")
    private int catidad;
    @JoinColumns({
        @JoinColumn(name = "producto_id", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "producto_nombre", referencedColumnName = "nombre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "pedido_num_pedido", referencedColumnName = "num_pedido", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;

    public ProductoPedido() {
    }

    public ProductoPedido(ProductoPedidoPK productoPedidoPK) {
        this.productoPedidoPK = productoPedidoPK;
    }

    public ProductoPedido(ProductoPedidoPK productoPedidoPK, int catidad) {
        this.productoPedidoPK = productoPedidoPK;
        this.catidad = catidad;
    }

    public ProductoPedido(int pedidoNumPedido, int productoId, String productoNombre) {
        this.productoPedidoPK = new ProductoPedidoPK(pedidoNumPedido, productoId, productoNombre);
    }

    public ProductoPedidoPK getProductoPedidoPK() {
        return productoPedidoPK;
    }

    public void setProductoPedidoPK(ProductoPedidoPK productoPedidoPK) {
        this.productoPedidoPK = productoPedidoPK;
    }

    public int getCatidad() {
        return catidad;
    }

    public void setCatidad(int catidad) {
        this.catidad = catidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoPedidoPK != null ? productoPedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPedido)) {
            return false;
        }
        ProductoPedido other = (ProductoPedido) object;
        if ((this.productoPedidoPK == null && other.productoPedidoPK != null) || (this.productoPedidoPK != null && !this.productoPedidoPK.equals(other.productoPedidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.ProductoPedido[ productoPedidoPK=" + productoPedidoPK + " ]";
    }
    
}
