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
import Clases.CargoEmpleado;
import Clases.Empleado;
import Clases.Pedido;
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
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        if (empleado.getPedidoCollection() == null) {
            empleado.setPedidoCollection(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CargoEmpleado cargo = empleado.getCargo();
            if (cargo != null) {
                cargo = em.getReference(cargo.getClass(), cargo.getIdCargo());
                empleado.setCargo(cargo);
            }
            Collection<Pedido> attachedPedidoCollection = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionPedidoToAttach : empleado.getPedidoCollection()) {
                pedidoCollectionPedidoToAttach = em.getReference(pedidoCollectionPedidoToAttach.getClass(), pedidoCollectionPedidoToAttach.getNumPedido());
                attachedPedidoCollection.add(pedidoCollectionPedidoToAttach);
            }
            empleado.setPedidoCollection(attachedPedidoCollection);
            em.persist(empleado);
            if (cargo != null) {
                cargo.getEmpleadoCollection().add(empleado);
                cargo = em.merge(cargo);
            }
            for (Pedido pedidoCollectionPedido : empleado.getPedidoCollection()) {
                Empleado oldIdEmpleadoOfPedidoCollectionPedido = pedidoCollectionPedido.getIdEmpleado();
                pedidoCollectionPedido.setIdEmpleado(empleado);
                pedidoCollectionPedido = em.merge(pedidoCollectionPedido);
                if (oldIdEmpleadoOfPedidoCollectionPedido != null) {
                    oldIdEmpleadoOfPedidoCollectionPedido.getPedidoCollection().remove(pedidoCollectionPedido);
                    oldIdEmpleadoOfPedidoCollectionPedido = em.merge(oldIdEmpleadoOfPedidoCollectionPedido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getIdentificacion()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdentificacion());
            CargoEmpleado cargoOld = persistentEmpleado.getCargo();
            CargoEmpleado cargoNew = empleado.getCargo();
            Collection<Pedido> pedidoCollectionOld = persistentEmpleado.getPedidoCollection();
            Collection<Pedido> pedidoCollectionNew = empleado.getPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoCollectionOldPedido : pedidoCollectionOld) {
                if (!pedidoCollectionNew.contains(pedidoCollectionOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoCollectionOldPedido + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cargoNew != null) {
                cargoNew = em.getReference(cargoNew.getClass(), cargoNew.getIdCargo());
                empleado.setCargo(cargoNew);
            }
            Collection<Pedido> attachedPedidoCollectionNew = new ArrayList<Pedido>();
            //comentario
            /*for (Pedido pedidoCollectionNewPedidoToAttach : pedidoCollectionNew) {
                pedidoCollectionNewPedidoToAttach = em.getReference(pedidoCollectionNewPedidoToAttach.getClass(), pedidoCollectionNewPedidoToAttach.getNumPedido());
                attachedPedidoCollectionNew.add(pedidoCollectionNewPedidoToAttach);
            }*/
            pedidoCollectionNew = attachedPedidoCollectionNew;
            empleado.setPedidoCollection(pedidoCollectionNew);
            empleado = em.merge(empleado);
            if (cargoOld != null && !cargoOld.equals(cargoNew)) {
                cargoOld.getEmpleadoCollection().remove(empleado);
                cargoOld = em.merge(cargoOld);
            }
            if (cargoNew != null && !cargoNew.equals(cargoOld)) {
                cargoNew.getEmpleadoCollection().add(empleado);
                cargoNew = em.merge(cargoNew);
            }
            for (Pedido pedidoCollectionNewPedido : pedidoCollectionNew) {
                if (!pedidoCollectionOld.contains(pedidoCollectionNewPedido)) {
                    Empleado oldIdEmpleadoOfPedidoCollectionNewPedido = pedidoCollectionNewPedido.getIdEmpleado();
                    pedidoCollectionNewPedido.setIdEmpleado(empleado);
                    pedidoCollectionNewPedido = em.merge(pedidoCollectionNewPedido);
                    if (oldIdEmpleadoOfPedidoCollectionNewPedido != null && !oldIdEmpleadoOfPedidoCollectionNewPedido.equals(empleado)) {
                        oldIdEmpleadoOfPedidoCollectionNewPedido.getPedidoCollection().remove(pedidoCollectionNewPedido);
                        oldIdEmpleadoOfPedidoCollectionNewPedido = em.merge(oldIdEmpleadoOfPedidoCollectionNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = empleado.getIdentificacion();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedido> pedidoCollectionOrphanCheck = empleado.getPedidoCollection();
            for (Pedido pedidoCollectionOrphanCheckPedido : pedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Pedido " + pedidoCollectionOrphanCheckPedido + " in its pedidoCollection field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CargoEmpleado cargo = empleado.getCargo();
            if (cargo != null) {
                cargo.getEmpleadoCollection().remove(empleado);
                cargo = em.merge(cargo);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
