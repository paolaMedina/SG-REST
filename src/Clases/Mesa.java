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
 * @author Daniel
 */
@Entity
@Table(name = "mesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mesa.findAll", query = "SELECT m FROM Mesa m"),
    @NamedQuery(name = "Mesa.findByNumMesa", query = "SELECT m FROM Mesa m WHERE m.numMesa = :numMesa"),
    @NamedQuery(name = "Mesa.findByMesa", query = "SELECT m FROM Mesa m WHERE m.mesa = :mesa")})
public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "num_mesa")
    private Integer numMesa;
    @Basic(optional = false)
    @Column(name = "mesa")
    private String mesa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numMesa")
    private Collection<Pedido> pedidoCollection;

    public Mesa() {
    }

    public Mesa(Integer numMesa) {
        this.numMesa = numMesa;
    }

    public Mesa(Integer numMesa, String mesa) {
        this.numMesa = numMesa;
        this.mesa = mesa;
    }

    public Integer getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(Integer numMesa) {
        this.numMesa = numMesa;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
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
        hash += (numMesa != null ? numMesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesa)) {
            return false;
        }
        Mesa other = (Mesa) object;
        if ((this.numMesa == null && other.numMesa != null) || (this.numMesa != null && !this.numMesa.equals(other.numMesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Mesa[ numMesa=" + numMesa + " ]";
    }
    
}
