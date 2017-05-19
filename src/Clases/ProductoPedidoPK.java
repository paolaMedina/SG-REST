/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Daniel
 */
@Embeddable
public class ProductoPedidoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "pedido_num_pedido")
    private int pedidoNumPedido;
    @Basic(optional = false)
    @Column(name = "producto_id")
    private int productoId;
    @Basic(optional = false)
    @Column(name = "producto_nombre")
    private String productoNombre;

    public ProductoPedidoPK() {
    }

    public ProductoPedidoPK(int pedidoNumPedido, int productoId, String productoNombre) {
        this.pedidoNumPedido = pedidoNumPedido;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
    }

    public int getPedidoNumPedido() {
        return pedidoNumPedido;
    }

    public void setPedidoNumPedido(int pedidoNumPedido) {
        this.pedidoNumPedido = pedidoNumPedido;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) pedidoNumPedido;
        hash += (int) productoId;
        hash += (productoNombre != null ? productoNombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPedidoPK)) {
            return false;
        }
        ProductoPedidoPK other = (ProductoPedidoPK) object;
        if (this.pedidoNumPedido != other.pedidoNumPedido) {
            return false;
        }
        if (this.productoId != other.productoId) {
            return false;
        }
        if ((this.productoNombre == null && other.productoNombre != null) || (this.productoNombre != null && !this.productoNombre.equals(other.productoNombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.ProductoPedidoPK[ pedidoNumPedido=" + pedidoNumPedido + ", productoId=" + productoId + ", productoNombre=" + productoNombre + " ]";
    }
    
}
