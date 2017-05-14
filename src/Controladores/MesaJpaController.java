/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Mesa;
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
public class MesaJpaController implements Serializable {

    public MesaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mesa mesa) {
        if (mesa.getPedidoCollection() == null) {
            mesa.setPedidoCollection(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pedido> attachedPedidoCollection = new ArrayList<Pedido>();
            for (Pedido pedidoCollectionPedidoToAttach : mesa.getPedidoCollection()) {
                pedidoCollectionPedidoToAttach = em.getReference(pedidoCollectionPedidoToAttach.getClass(), pedidoCollectionPedidoToAttach.getNumPedido());
                attachedPedidoCollection.add(pedidoCollectionPedidoToAttach);
            }
            mesa.setPedidoCollection(attachedPedidoCollection);
            em.persist(mesa);
            for (Pedido pedidoCollectionPedido : mesa.getPedidoCollection()) {
                Mesa oldNumMesaOfPedidoCollectionPedido = pedidoCollectionPedido.getNumMesa();
                pedidoCollectionPedido.setNumMesa(mesa);
                pedidoCollectionPedido = em.merge(pedidoCollectionPedido);
                if (oldNumMesaOfPedidoCollectionPedido != null) {
                    oldNumMesaOfPedidoCollectionPedido.getPedidoCollection().remove(pedidoCollectionPedido);
                    oldNumMesaOfPedidoCollectionPedido = em.merge(oldNumMesaOfPedidoCollectionPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mesa mesa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mesa persistentMesa = em.find(Mesa.class, mesa.getNumMesa());
            Collection<Pedido> pedidoCollectionOld = persistentMesa.getPedidoCollection();
            Collection<Pedido> pedidoCollectionNew = mesa.getPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoCollectionOldPedido : pedidoCollectionOld) {
                if (!pedidoCollectionNew.contains(pedidoCollectionOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoCollectionOldPedido + " since its numMesa field is not nullable.");
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
            mesa.setPedidoCollection(pedidoCollectionNew);
            mesa = em.merge(mesa);
            for (Pedido pedidoCollectionNewPedido : pedidoCollectionNew) {
                if (!pedidoCollectionOld.contains(pedidoCollectionNewPedido)) {
                    Mesa oldNumMesaOfPedidoCollectionNewPedido = pedidoCollectionNewPedido.getNumMesa();
                    pedidoCollectionNewPedido.setNumMesa(mesa);
                    pedidoCollectionNewPedido = em.merge(pedidoCollectionNewPedido);
                    if (oldNumMesaOfPedidoCollectionNewPedido != null && !oldNumMesaOfPedidoCollectionNewPedido.equals(mesa)) {
                        oldNumMesaOfPedidoCollectionNewPedido.getPedidoCollection().remove(pedidoCollectionNewPedido);
                        oldNumMesaOfPedidoCollectionNewPedido = em.merge(oldNumMesaOfPedidoCollectionNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mesa.getNumMesa();
                if (findMesa(id) == null) {
                    throw new NonexistentEntityException("The mesa with id " + id + " no longer exists.");
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
            Mesa mesa;
            try {
                mesa = em.getReference(Mesa.class, id);
                mesa.getNumMesa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mesa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedido> pedidoCollectionOrphanCheck = mesa.getPedidoCollection();
            for (Pedido pedidoCollectionOrphanCheckPedido : pedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Mesa (" + mesa + ") cannot be destroyed since the Pedido " + pedidoCollectionOrphanCheckPedido + " in its pedidoCollection field has a non-nullable numMesa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mesa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mesa> findMesaEntities() {
        return findMesaEntities(true, -1, -1);
    }

    public List<Mesa> findMesaEntities(int maxResults, int firstResult) {
        return findMesaEntities(false, maxResults, firstResult);
    }

    private List<Mesa> findMesaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mesa.class));
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

    public Mesa findMesa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mesa.class, id);
        } finally {
            em.close();
        }
    }

    public int getMesaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mesa> rt = cq.from(Mesa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
