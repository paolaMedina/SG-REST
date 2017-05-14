/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.CargoEmpleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Empleado;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel Galarza
 * @author Felipe Tellez
 * @author Paola Medina
 */
public class CargoEmpleadoJpaController implements Serializable {

    public CargoEmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CargoEmpleado cargoEmpleado) {
        if (cargoEmpleado.getEmpleadoCollection() == null) {
            cargoEmpleado.setEmpleadoCollection(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Empleado> attachedEmpleadoCollection = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionEmpleadoToAttach : cargoEmpleado.getEmpleadoCollection()) {
                empleadoCollectionEmpleadoToAttach = em.getReference(empleadoCollectionEmpleadoToAttach.getClass(), empleadoCollectionEmpleadoToAttach.getIdentificacion());
                attachedEmpleadoCollection.add(empleadoCollectionEmpleadoToAttach);
            }
            cargoEmpleado.setEmpleadoCollection(attachedEmpleadoCollection);
            em.persist(cargoEmpleado);
            for (Empleado empleadoCollectionEmpleado : cargoEmpleado.getEmpleadoCollection()) {
                CargoEmpleado oldCargoOfEmpleadoCollectionEmpleado = empleadoCollectionEmpleado.getCargo();
                empleadoCollectionEmpleado.setCargo(cargoEmpleado);
                empleadoCollectionEmpleado = em.merge(empleadoCollectionEmpleado);
                if (oldCargoOfEmpleadoCollectionEmpleado != null) {
                    oldCargoOfEmpleadoCollectionEmpleado.getEmpleadoCollection().remove(empleadoCollectionEmpleado);
                    oldCargoOfEmpleadoCollectionEmpleado = em.merge(oldCargoOfEmpleadoCollectionEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CargoEmpleado cargoEmpleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CargoEmpleado persistentCargoEmpleado = em.find(CargoEmpleado.class, cargoEmpleado.getIdCargo());
            Collection<Empleado> empleadoCollectionOld = persistentCargoEmpleado.getEmpleadoCollection();
            Collection<Empleado> empleadoCollectionNew = cargoEmpleado.getEmpleadoCollection();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoCollectionOldEmpleado : empleadoCollectionOld) {
                if (!empleadoCollectionNew.contains(empleadoCollectionOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoCollectionOldEmpleado + " since its cargo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Empleado> attachedEmpleadoCollectionNew = new ArrayList<Empleado>();
            for (Empleado empleadoCollectionNewEmpleadoToAttach : empleadoCollectionNew) {
                empleadoCollectionNewEmpleadoToAttach = em.getReference(empleadoCollectionNewEmpleadoToAttach.getClass(), empleadoCollectionNewEmpleadoToAttach.getIdentificacion());
                attachedEmpleadoCollectionNew.add(empleadoCollectionNewEmpleadoToAttach);
            }
            empleadoCollectionNew = attachedEmpleadoCollectionNew;
            cargoEmpleado.setEmpleadoCollection(empleadoCollectionNew);
            cargoEmpleado = em.merge(cargoEmpleado);
            for (Empleado empleadoCollectionNewEmpleado : empleadoCollectionNew) {
                if (!empleadoCollectionOld.contains(empleadoCollectionNewEmpleado)) {
                    CargoEmpleado oldCargoOfEmpleadoCollectionNewEmpleado = empleadoCollectionNewEmpleado.getCargo();
                    empleadoCollectionNewEmpleado.setCargo(cargoEmpleado);
                    empleadoCollectionNewEmpleado = em.merge(empleadoCollectionNewEmpleado);
                    if (oldCargoOfEmpleadoCollectionNewEmpleado != null && !oldCargoOfEmpleadoCollectionNewEmpleado.equals(cargoEmpleado)) {
                        oldCargoOfEmpleadoCollectionNewEmpleado.getEmpleadoCollection().remove(empleadoCollectionNewEmpleado);
                        oldCargoOfEmpleadoCollectionNewEmpleado = em.merge(oldCargoOfEmpleadoCollectionNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cargoEmpleado.getIdCargo();
                if (findCargoEmpleado(id) == null) {
                    throw new NonexistentEntityException("The cargoEmpleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CargoEmpleado cargoEmpleado;
            try {
                cargoEmpleado = em.getReference(CargoEmpleado.class, id);
                cargoEmpleado.getIdCargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargoEmpleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Empleado> empleadoCollectionOrphanCheck = cargoEmpleado.getEmpleadoCollection();
            for (Empleado empleadoCollectionOrphanCheckEmpleado : empleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CargoEmpleado (" + cargoEmpleado + ") cannot be destroyed since the Empleado " + empleadoCollectionOrphanCheckEmpleado + " in its empleadoCollection field has a non-nullable cargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargoEmpleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CargoEmpleado> findCargoEmpleadoEntities() {
        return findCargoEmpleadoEntities(true, -1, -1);
    }

    public List<CargoEmpleado> findCargoEmpleadoEntities(int maxResults, int firstResult) {
        return findCargoEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<CargoEmpleado> findCargoEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CargoEmpleado.class));
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

    public CargoEmpleado findCargoEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CargoEmpleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CargoEmpleado> rt = cq.from(CargoEmpleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
