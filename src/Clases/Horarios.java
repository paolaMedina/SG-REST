/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "horarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horarios.findAll", query = "SELECT h FROM Horarios h"),
    @NamedQuery(name = "Horarios.findByHorarioInicio", query = "SELECT h FROM Horarios h WHERE h.horariosPK.horarioInicio = :horarioInicio"),
    @NamedQuery(name = "Horarios.findByHorarioFin", query = "SELECT h FROM Horarios h WHERE h.horariosPK.horarioFin = :horarioFin")})
public class Horarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HorariosPK horariosPK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "horarios")
    private Collection<HorarioEmpleado> horarioEmpleadoCollection;

    public Horarios() {
    }

    public Horarios(HorariosPK horariosPK) {
        this.horariosPK = horariosPK;
    }

    public Horarios(Date horarioInicio, Date horarioFin) {
        this.horariosPK = new HorariosPK(horarioInicio, horarioFin);
    }

    public HorariosPK getHorariosPK() {
        return horariosPK;
    }

    public void setHorariosPK(HorariosPK horariosPK) {
        this.horariosPK = horariosPK;
    }

    @XmlTransient
    public Collection<HorarioEmpleado> getHorarioEmpleadoCollection() {
        return horarioEmpleadoCollection;
    }

    public void setHorarioEmpleadoCollection(Collection<HorarioEmpleado> horarioEmpleadoCollection) {
        this.horarioEmpleadoCollection = horarioEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horariosPK != null ? horariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horarios)) {
            return false;
        }
        Horarios other = (Horarios) object;
        if ((this.horariosPK == null && other.horariosPK != null) || (this.horariosPK != null && !this.horariosPK.equals(other.horariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.Horarios[ horariosPK=" + horariosPK + " ]";
    }
    
}
