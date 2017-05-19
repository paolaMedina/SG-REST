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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "horario_empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HorarioEmpleado.findAll", query = "SELECT h FROM HorarioEmpleado h"),
    @NamedQuery(name = "HorarioEmpleado.findByEmpleadoIdentificacion", query = "SELECT h FROM HorarioEmpleado h WHERE h.horarioEmpleadoPK.empleadoIdentificacion = :empleadoIdentificacion"),
    @NamedQuery(name = "HorarioEmpleado.findByHorariosHorarioInicio", query = "SELECT h FROM HorarioEmpleado h WHERE h.horarioEmpleadoPK.horariosHorarioInicio = :horariosHorarioInicio"),
    @NamedQuery(name = "HorarioEmpleado.findByHorariosHorarioFin", query = "SELECT h FROM HorarioEmpleado h WHERE h.horarioEmpleadoPK.horariosHorarioFin = :horariosHorarioFin"),
    @NamedQuery(name = "HorarioEmpleado.findByFechaInicio", query = "SELECT h FROM HorarioEmpleado h WHERE h.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "HorarioEmpleado.findByFechaFin", query = "SELECT h FROM HorarioEmpleado h WHERE h.fechaFin = :fechaFin")})
public class HorarioEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HorarioEmpleadoPK horarioEmpleadoPK;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumns({
        @JoinColumn(name = "horarios_horario_inicio", referencedColumnName = "horario_inicio", insertable = false, updatable = false),
        @JoinColumn(name = "horarios_horario_fin", referencedColumnName = "horario_fin", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Horarios horarios;
    @JoinColumn(name = "empleado_identificacion", referencedColumnName = "identificacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empleado empleado;

    public HorarioEmpleado() {
    }

    public HorarioEmpleado(HorarioEmpleadoPK horarioEmpleadoPK) {
        this.horarioEmpleadoPK = horarioEmpleadoPK;
    }

    public HorarioEmpleado(HorarioEmpleadoPK horarioEmpleadoPK, Date fechaInicio, Date fechaFin) {
        this.horarioEmpleadoPK = horarioEmpleadoPK;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public HorarioEmpleado(String empleadoIdentificacion, Date horariosHorarioInicio, Date horariosHorarioFin) {
        this.horarioEmpleadoPK = new HorarioEmpleadoPK(empleadoIdentificacion, horariosHorarioInicio, horariosHorarioFin);
    }

    public HorarioEmpleadoPK getHorarioEmpleadoPK() {
        return horarioEmpleadoPK;
    }

    public void setHorarioEmpleadoPK(HorarioEmpleadoPK horarioEmpleadoPK) {
        this.horarioEmpleadoPK = horarioEmpleadoPK;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (horarioEmpleadoPK != null ? horarioEmpleadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorarioEmpleado)) {
            return false;
        }
        HorarioEmpleado other = (HorarioEmpleado) object;
        if ((this.horarioEmpleadoPK == null && other.horarioEmpleadoPK != null) || (this.horarioEmpleadoPK != null && !this.horarioEmpleadoPK.equals(other.horarioEmpleadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.HorarioEmpleado[ horarioEmpleadoPK=" + horarioEmpleadoPK + " ]";
    }
    
}
