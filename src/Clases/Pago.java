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
@Table(name = "pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
    @NamedQuery(name = "Pago.findByIdPago", query = "SELECT p FROM Pago p WHERE p.idPago = :idPago"),
    @NamedQuery(name = "Pago.findByNumTarjetas", query = "SELECT p FROM Pago p WHERE p.numTarjetas = :numTarjetas"),
    @NamedQuery(name = "Pago.findByDineroEfectivo", query = "SELECT p FROM Pago p WHERE p.dineroEfectivo = :dineroEfectivo"),
    @NamedQuery(name = "Pago.findByDineroTarjetas", query = "SELECT p FROM Pago p WHERE p.dineroTarjetas = :dineroTarjetas")})
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pago")
    private Integer idPago;
    @Basic(optional = false)
    @Column(name = "num_tarjetas")
    private int numTarjetas;
    @Basic(optional = false)
    @Column(name = "dinero_efectivo")
    private long dineroEfectivo;
    @Basic(optional = false)
    @Column(name = "dinero_tarjetas")
    private long dineroTarjetas;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo")
    @ManyToOne(optional = false)
    private TipoPago idTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPago")
    private Collection<Factura> facturaCollection;

    public Pago() {
    }

    public Pago(Integer idPago) {
        this.idPago = idPago;
    }

    public Pago(Integer idPago, int numTarjetas, long dineroEfectivo, long dineroTarjetas) {
        this.idPago = idPago;
        this.numTarjetas = numTarjetas;
        this.dineroEfectivo = dineroEfectivo;
        this.dineroTarjetas = dineroTarjetas;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public int getNumTarjetas() {
        return numTarjetas;
    }

    public void setNumTarjetas(int numTarjetas) {
        this.numTarjetas = numTarjetas;
    }

    public long getDineroEfectivo() {
        return dineroEfectivo;
    }

    public void setDineroEfectivo(long dineroEfectivo) {
        this.dineroEfectivo = dineroEfectivo;
    }

    public long getDineroTarjetas() {
        return dineroTarjetas;
    }

    public void setDineroTarjetas(long dineroTarjetas) {
        this.dineroTarjetas = dineroTarjetas;
    }

    public TipoPago getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(TipoPago idTipo) {
        this.idTipo = idTipo;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPago != null ? idPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idPago == null && other.idPago != null) || (this.idPago != null && !this.idPago.equals(other.idPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Pago[ idPago=" + idPago + " ]";
    }
    
}
