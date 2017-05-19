/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Horarios;
import Clases.Empleado;
import Clases.HorarioEmpleado;
import Clases.HorarioEmpleadoPK;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class HorarioEmpleadoJpaController implements Serializable {

    public HorarioEmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HorarioEmpleado horarioEmpleado) throws PreexistingEntityException, Exception {
        if (horarioEmpleado.getHorarioEmpleadoPK() == null) {
            horarioEmpleado.setHorarioEmpleadoPK(new HorarioEmpleadoPK());
        }
        horarioEmpleado.getHorarioEmpleadoPK().setHorariosHorarioFin(horarioEmpleado.getHorarios().getHorariosPK().getHorarioFin());
        horarioEmpleado.getHorarioEmpleadoPK().setHorariosHorarioInicio(horarioEmpleado.getHorarios().getHorariosPK().getHorarioInicio());
        horarioEmpleado.getHorarioEmpleadoPK().setEmpleadoIdentificacion(horarioEmpleado.getEmpleado().getIdentificacion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Horarios horarios = horarioEmpleado.getHorarios();
            if (horarios != null) {
                horarios = em.getReference(horarios.getClass(), horarios.getHorariosPK());
                horarioEmpleado.setHorarios(horarios);
            }
            Empleado empleado = horarioEmpleado.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getIdentificacion());
                horarioEmpleado.setEmpleado(empleado);
            }
            em.persist(horarioEmpleado);
            if (horarios != null) {
                horarios.getHorarioEmpleadoCollection().add(horarioEmpleado);
                horarios = em.merge(horarios);
            }
            if (empleado != null) {
                empleado.getHorarioEmpleadoCollection().add(horarioEmpleado);
                empleado = em.merge(empleado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHorarioEmpleado(horarioEmpleado.getHorarioEmpleadoPK()) != null) {
                throw new PreexistingEntityException("HorarioEmpleado " + horarioEmpleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HorarioEmpleado horarioEmpleado) throws NonexistentEntityException, Exception {
        horarioEmpleado.getHorarioEmpleadoPK().setHorariosHorarioFin(horarioEmpleado.getHorarios().getHorariosPK().getHorarioFin());
        horarioEmpleado.getHorarioEmpleadoPK().setHorariosHorarioInicio(horarioEmpleado.getHorarios().getHorariosPK().getHorarioInicio());
        horarioEmpleado.getHorarioEmpleadoPK().setEmpleadoIdentificacion(horarioEmpleado.getEmpleado().getIdentificacion());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HorarioEmpleado persistentHorarioEmpleado = em.find(HorarioEmpleado.class, horarioEmpleado.getHorarioEmpleadoPK());
            Horarios horariosOld = persistentHorarioEmpleado.getHorarios();
            Horarios horariosNew = horarioEmpleado.getHorarios();
            Empleado empleadoOld = persistentHorarioEmpleado.getEmpleado();
            Empleado empleadoNew = horarioEmpleado.getEmpleado();
            if (horariosNew != null) {
                horariosNew = em.getReference(horariosNew.getClass(), horariosNew.getHorariosPK());
                horarioEmpleado.setHorarios(horariosNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getIdentificacion());
                horarioEmpleado.setEmpleado(empleadoNew);
            }
            horarioEmpleado = em.merge(horarioEmpleado);
            if (horariosOld != null && !horariosOld.equals(horariosNew)) {
                horariosOld.getHorarioEmpleadoCollection().remove(horarioEmpleado);
                horariosOld = em.merge(horariosOld);
            }
            if (horariosNew != null && !horariosNew.equals(horariosOld)) {
                horariosNew.getHorarioEmpleadoCollection().add(horarioEmpleado);
                horariosNew = em.merge(horariosNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getHorarioEmpleadoCollection().remove(horarioEmpleado);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getHorarioEmpleadoCollection().add(horarioEmpleado);
                empleadoNew = em.merge(empleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                HorarioEmpleadoPK id = horarioEmpleado.getHorarioEmpleadoPK();
                if (findHorarioEmpleado(id) == null) {
                    throw new NonexistentEntityException("The horarioEmpleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HorarioEmpleadoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HorarioEmpleado horarioEmpleado;
            try {
                horarioEmpleado = em.getReference(HorarioEmpleado.class, id);
                horarioEmpleado.getHorarioEmpleadoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horarioEmpleado with id " + id + " no longer exists.", enfe);
            }
            Horarios horarios = horarioEmpleado.getHorarios();
            if (horarios != null) {
                horarios.getHorarioEmpleadoCollection().remove(horarioEmpleado);
                horarios = em.merge(horarios);
            }
            Empleado empleado = horarioEmpleado.getEmpleado();
            if (empleado != null) {
                empleado.getHorarioEmpleadoCollection().remove(horarioEmpleado);
                empleado = em.merge(empleado);
            }
            em.remove(horarioEmpleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HorarioEmpleado> findHorarioEmpleadoEntities() {
        return findHorarioEmpleadoEntities(true, -1, -1);
    }

    public List<HorarioEmpleado> findHorarioEmpleadoEntities(int maxResults, int firstResult) {
        return findHorarioEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<HorarioEmpleado> findHorarioEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HorarioEmpleado.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public HorarioEmpleado findHorarioEmpleado(HorarioEmpleadoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HorarioEmpleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HorarioEmpleado> rt = cq.from(HorarioEmpleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
