/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.EstadoPedido;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Pedido;
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
public class EstadoPedidoJpaController implements Serializable {

    public EstadoPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoPedido estadoPedido) {
        if (estadoPedido.getPedidoCollection() == null) {
            estadoPedido.setPedidoCollection(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pedido> attachedPedidoCollection = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionPedidoToAttach : estadoPedido.getPedidoCollection()) {
                pedidoCollectionPedidoToAttach = em.getReference(pedidoCollectionPedidoToAttach.getClass(), pedidoCollectionPedidoToAttach.getNumPedido());
                attachedPedidoCollection.add(pedidoCollectionPedidoToAttach);
            }
            estadoPedido.setPedidoCollection(attachedPedidoCollection);
            em.persist(estadoPedido);
            for (Pedido pedidoCollectionPedido : estadoPedido.getPedidoCollection()) {
                EstadoPedido oldIdEstadoPedidoOfPedidoCollectionPedido = pedidoCollectionPedido.getIdEstadoPedido();
                pedidoCollectionPedido.setIdEstadoPedido(estadoPedido);
                pedidoCollectionPedido = em.merge(pedidoCollectionPedido);
                if (oldIdEstadoPedidoOfPedidoCollectionPedido != null) {
                    oldIdEstadoPedidoOfPedidoCollectionPedido.getPedidoCollection().remove(pedidoCollectionPedido);
                    oldIdEstadoPedidoOfPedidoCollectionPedido = em.merge(oldIdEstadoPedidoOfPedidoCollectionPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoPedido estadoPedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoPedido persistentEstadoPedido = em.find(EstadoPedido.class, estadoPedido.getIdEstadoPedido());
            Collection<Pedido> pedidoCollectionOld = persistentEstadoPedido.getPedidoCollection();
            Collection<Pedido> pedidoCollectionNew = estadoPedido.getPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoCollectionOldPedido : pedidoCollectionOld) {
                if (!pedidoCollectionNew.contains(pedidoCollectionOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoCollectionOldPedido + " since its idEstadoPedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pedido> attachedPedidoCollectionNew = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionNewPedidoToAttach : pedidoCollectionNew) {
                pedidoCollectionNewPedidoToAttach = em.getReference(pedidoCollectionNewPedidoToAttach.getClass(), pedidoCollectionNewPedidoToAttach.getNumPedido());
                attachedPedidoCollectionNew.add(pedidoCollectionNewPedidoToAttach);
            }
            pedidoCollectionNew = attachedPedidoCollectionNew;
            estadoPedido.setPedidoCollection(pedidoCollectionNew);
            estadoPedido = em.merge(estadoPedido);
            for (Pedido pedidoCollectionNewPedido : pedidoCollectionNew) {
                if (!pedidoCollectionOld.contains(pedidoCollectionNewPedido)) {
                    EstadoPedido oldIdEstadoPedidoOfPedidoCollectionNewPedido = pedidoCollectionNewPedido.getIdEstadoPedido();
                    pedidoCollectionNewPedido.setIdEstadoPedido(estadoPedido);
                    pedidoCollectionNewPedido = em.merge(pedidoCollectionNewPedido);
                    if (oldIdEstadoPedidoOfPedidoCollectionNewPedido != null && !oldIdEstadoPedidoOfPedidoCollectionNewPedido.equals(estadoPedido)) {
                        oldIdEstadoPedidoOfPedidoCollectionNewPedido.getPedidoCollection().remove(pedidoCollectionNewPedido);
                        oldIdEstadoPedidoOfPedidoCollectionNewPedido = em.merge(oldIdEstadoPedidoOfPedidoCollectionNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoPedido.getIdEstadoPedido();
                if (findEstadoPedido(id) == null) {
                    throw new NonexistentEntityException("The estadoPedido with id " + id + " no longer exists.");
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
            EstadoPedido estadoPedido;
            try {
                estadoPedido = em.getReference(EstadoPedido.class, id);
                estadoPedido.getIdEstadoPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoPedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedido> pedidoCollectionOrphanCheck = estadoPedido.getPedidoCollection();
            for (Pedido pedidoCollectionOrphanCheckPedido : pedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoPedido (" + estadoPedido + ") cannot be destroyed since the Pedido " + pedidoCollectionOrphanCheckPedido + " in its pedidoCollection field has a non-nullable idEstadoPedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoPedido> findEstadoPedidoEntities() {
        return findEstadoPedidoEntities(true, -1, -1);
    }

    public List<EstadoPedido> findEstadoPedidoEntities(int maxResults, int firstResult) {
        return findEstadoPedidoEntities(false, maxResults, firstResult);
    }

    private List<EstadoPedido> findEstadoPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoPedido.class));
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

    public EstadoPedido findEstadoPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoPedido> rt = cq.from(EstadoPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
