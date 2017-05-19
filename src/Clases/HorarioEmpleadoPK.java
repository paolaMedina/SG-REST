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
public class HorarioEmpleadoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "empleado_identificacion")
    private String empleadoIdentificacion;
    @Basic(optional = false)
    @Column(name = "horarios_horario_inicio")
    @Temporal(TemporalType.TIME)
    private Date horariosHorarioInicio;
    @Basic(optional = false)
    @Column(name = "horarios_horario_fin")
    @Temporal(TemporalType.TIME)
    private Date horariosHorarioFin;

    public HorarioEmpleadoPK() {
    }

    public HorarioEmpleadoPK(String empleadoIdentificacion, Date horariosHorarioInicio, Date horariosHorarioFin) {
        this.empleadoIdentificacion = empleadoIdentificacion;
        this.horariosHorarioInicio = horariosHorarioInicio;
        this.horariosHorarioFin = horariosHorarioFin;
    }

    public String getEmpleadoIdentificacion() {
        return empleadoIdentificacion;
    }

    public void setEmpleadoIdentificacion(String empleadoIdentificacion) {
        this.empleadoIdentificacion = empleadoIdentificacion;
    }

    public Date getHorariosHorarioInicio() {
        return horariosHorarioInicio;
    }

    public void setHorariosHorarioInicio(Date horariosHorarioInicio) {
        this.horariosHorarioInicio = horariosHorarioInicio;
    }

    public Date getHorariosHorarioFin() {
        return horariosHorarioFin;
    }

    public void setHorariosHorarioFin(Date horariosHorarioFin) {
        this.horariosHorarioFin = horariosHorarioFin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadoIdentificacion != null ? empleadoIdentificacion.hashCode() : 0);
        hash += (horariosHorarioInicio != null ? horariosHorarioInicio.hashCode() : 0);
        hash += (horariosHorarioFin != null ? horariosHorarioFin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioEmpleadoPK)) {
            return false;
        }
        HorarioEmpleadoPK other = (HorarioEmpleadoPK) object;
        if ((this.empleadoIdentificacion == null && other.empleadoIdentificacion != null) || (this.empleadoIdentificacion != null && !this.empleadoIdentificacion.equals(other.empleadoIdentificacion))) {
            return false;
        }
        if ((this.horariosHorarioInicio == null && other.horariosHorarioInicio != null) || (this.horariosHorarioInicio != null && !this.horariosHorarioInicio.equals(other.horariosHorarioInicio))) {
            return false;
        }
        if ((this.horariosHorarioFin == null && other.horariosHorarioFin != null) || (this.horariosHorarioFin != null && !this.horariosHorarioFin.equals(other.horariosHorarioFin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.HorarioEmpleadoPK[ empleadoIdentificacion=" + empleadoIdentificacion + ", horariosHorarioInicio=" + horariosHorarioInicio + ", horariosHorarioFin=" + horariosHorarioFin + " ]";
    }
    
}
