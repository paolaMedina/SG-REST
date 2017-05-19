/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Daniel
 */
@Embeddable
public class HorariosPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "horario_inicio")
    @Temporal(TemporalType.TIME)
    private Date horarioInicio;
    @Basic(optional = false)
    @Column(name = "horario_fin")
    @Temporal(TemporalType.TIME)
    private Date horarioFin;

    public HorariosPK() {
    }

    public HorariosPK(Date horarioInicio, Date horarioFin) {
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }

    public Date getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Date horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Date getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(Date horarioFin) {
        this.horarioFin = horarioFin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horarioInicio != null ? horarioInicio.hashCode() : 0);
        hash += (horarioFin != null ? horarioFin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorariosPK)) {
            return false;
        }
        HorariosPK other = (HorariosPK) object;
        if ((this.horarioInicio == null && other.horarioInicio != null) || (this.horarioInicio != null && !this.horarioInicio.equals(other.horarioInicio))) {
            return false;
        }
        if ((this.horarioFin == null && other.horarioFin != null) || (this.horarioFin != null && !this.horarioFin.equals(other.horarioFin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.HorariosPK[ horarioInicio=" + horarioInicio + ", horarioFin=" + horarioFin + " ]";
    }
    
}
