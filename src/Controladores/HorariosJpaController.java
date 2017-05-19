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
import Clases.HorarioEmpleado;
import Clases.Horarios;
import Clases.HorariosPK;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class HorariosJpaController implements Serializable {

    public HorariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horarios horarios) throws PreexistingEntityException, Exception {
        if (horarios.getHorariosPK() == null) {
            horarios.setHorariosPK(new HorariosPK());
        }
        if (horarios.getHorarioEmpleadoCollection() == null) {
            horarios.setHorarioEmpleadoCollection(new ArrayList<HorarioEmpleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<HorarioEmpleado> attachedHorarioEmpleadoCollection = new ArrayList<HorarioEmpleado>();
            for (HorarioEmpleado horarioEmpleadoCollectionHorarioEmpleadoToAttach : horarios.getHorarioEmpleadoCollection()) {
                horarioEmpleadoCollectionHorarioEmpleadoToAttach = em.getReference(horarioEmpleadoCollectionHorarioEmpleadoToAttach.getClass(), horarioEmpleadoCollectionHorarioEmpleadoToAttach.getHorarioEmpleadoPK());
                attachedHorarioEmpleadoCollection.add(horarioEmpleadoCollectionHorarioEmpleadoToAttach);
            }
            horarios.setHorarioEmpleadoCollection(attachedHorarioEmpleadoCollection);
            em.persist(horarios);
            for (HorarioEmpleado horarioEmpleadoCollectionHorarioEmpleado : horarios.getHorarioEmpleadoCollection()) {
                Horarios oldHorariosOfHorarioEmpleadoCollectionHorarioEmpleado = horarioEmpleadoCollectionHorarioEmpleado.getHorarios();
                horarioEmpleadoCollectionHorarioEmpleado.setHorarios(horarios);
                horarioEmpleadoCollectionHorarioEmpleado = em.merge(horarioEmpleadoCollectionHorarioEmpleado);
                if (oldHorariosOfHorarioEmpleadoCollectionHorarioEmpleado != null) {
                    oldHorariosOfHorarioEmpleadoCollectionHorarioEmpleado.getHorarioEmpleadoCollection().remove(horarioEmpleadoCollectionHorarioEmpleado);
                    oldHorariosOfHorarioEmpleadoCollectionHorarioEmpleado = em.merge(oldHorariosOfHorarioEmpleadoCollectionHorarioEmpleado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHorarios(horarios.getHorariosPK()) != null) {
                throw new PreexistingEntityException("Horarios " + horarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Horarios horarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Horarios persistentHorarios = em.find(Horarios.class, horarios.getHorariosPK());
            Collection<HorarioEmpleado> horarioEmpleadoCollectionOld = persistentHorarios.getHorarioEmpleadoCollection();
            Collection<HorarioEmpleado> horarioEmpleadoCollectionNew = horarios.getHorarioEmpleadoCollection();
            List<String> illegalOrphanMessages = null;
            for (HorarioEmpleado horarioEmpleadoCollectionOldHorarioEmpleado : horarioEmpleadoCollectionOld) {
                if (!horarioEmpleadoCollectionNew.contains(horarioEmpleadoCollectionOldHorarioEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HorarioEmpleado " + horarioEmpleadoCollectionOldHorarioEmpleado + " since its horarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<HorarioEmpleado> attachedHorarioEmpleadoCollectionNew = new ArrayList<HorarioEmpleado>();
            for (HorarioEmpleado horarioEmpleadoCollectionNewHorarioEmpleadoToAttach : horarioEmpleadoCollectionNew) {
                horarioEmpleadoCollectionNewHorarioEmpleadoToAttach = em.getReference(horarioEmpleadoCollectionNewHorarioEmpleadoToAttach.getClass(), horarioEmpleadoCollectionNewHorarioEmpleadoToAttach.getHorarioEmpleadoPK());
                attachedHorarioEmpleadoCollectionNew.add(horarioEmpleadoCollectionNewHorarioEmpleadoToAttach);
            }
            horarioEmpleadoCollectionNew = attachedHorarioEmpleadoCollectionNew;
            horarios.setHorarioEmpleadoCollection(horarioEmpleadoCollectionNew);
            horarios = em.merge(horarios);
            for (HorarioEmpleado horarioEmpleadoCollectionNewHorarioEmpleado : horarioEmpleadoCollectionNew) {
                if (!horarioEmpleadoCollectionOld.contains(horarioEmpleadoCollectionNewHorarioEmpleado)) {
                    Horarios oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado = horarioEmpleadoCollectionNewHorarioEmpleado.getHorarios();
                    horarioEmpleadoCollectionNewHorarioEmpleado.setHorarios(horarios);
                    horarioEmpleadoCollectionNewHorarioEmpleado = em.merge(horarioEmpleadoCollectionNewHorarioEmpleado);
                    if (oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado != null && !oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado.equals(horarios)) {
                        oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado.getHorarioEmpleadoCollection().remove(horarioEmpleadoCollectionNewHorarioEmpleado);
                        oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado = em.merge(oldHorariosOfHorarioEmpleadoCollectionNewHorarioEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                HorariosPK id = horarios.getHorariosPK();
                if (findHorarios(id) == null) {
                    throw new NonexistentEntityException("The horarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HorariosPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Horarios horarios;
            try {
                horarios = em.getReference(Horarios.class, id);
                horarios.getHorariosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<HorarioEmpleado> horarioEmpleadoCollectionOrphanCheck = horarios.getHorarioEmpleadoCollection();
            for (HorarioEmpleado horarioEmpleadoCollectionOrphanCheckHorarioEmpleado : horarioEmpleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Horarios (" + horarios + ") cannot be destroyed since the HorarioEmpleado " + horarioEmpleadoCollectionOrphanCheckHorarioEmpleado + " in its horarioEmpleadoCollection field has a non-nullable horarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(horarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Horarios> findHorariosEntities() {
        return findHorariosEntities(true, -1, -1);
    }

    public List<Horarios> findHorariosEntities(int maxResults, int firstResult) {
        return findHorariosEntities(false, maxResults, firstResult);
    }

    private List<Horarios> findHorariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horarios.class));
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

    public Horarios findHorarios(HorariosPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horarios> rt = cq.from(Horarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
