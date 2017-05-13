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
import Clases.Pago;
import Clases.TipoPago;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class TipoPagoJpaController implements Serializable {

    public TipoPagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPago tipoPago) {
        if (tipoPago.getPagoCollection() == null) {
            tipoPago.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : tipoPago.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            tipoPago.setPagoCollection(attachedPagoCollection);
            em.persist(tipoPago);
            for (Pago pagoCollectionPago : tipoPago.getPagoCollection()) {
                TipoPago oldIdTipoOfPagoCollectionPago = pagoCollectionPago.getIdTipo();
                pagoCollectionPago.setIdTipo(tipoPago);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldIdTipoOfPagoCollectionPago != null) {
                    oldIdTipoOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldIdTipoOfPagoCollectionPago = em.merge(oldIdTipoOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPago tipoPago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPago persistentTipoPago = em.find(TipoPago.class, tipoPago.getIdTipo());
            Collection<Pago> pagoCollectionOld = persistentTipoPago.getPagoCollection();
            Collection<Pago> pagoCollectionNew = tipoPago.getPagoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its idTipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            tipoPago.setPagoCollection(pagoCollectionNew);
            tipoPago = em.merge(tipoPago);
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    TipoPago oldIdTipoOfPagoCollectionNewPago = pagoCollectionNewPago.getIdTipo();
                    pagoCollectionNewPago.setIdTipo(tipoPago);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldIdTipoOfPagoCollectionNewPago != null && !oldIdTipoOfPagoCollectionNewPago.equals(tipoPago)) {
                        oldIdTipoOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldIdTipoOfPagoCollectionNewPago = em.merge(oldIdTipoOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPago.getIdTipo();
                if (findTipoPago(id) == null) {
                    throw new NonexistentEntityException("The tipoPago with id " + id + " no longer exists.");
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
            TipoPago tipoPago;
            try {
                tipoPago = em.getReference(TipoPago.class, id);
                tipoPago.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pago> pagoCollectionOrphanCheck = tipoPago.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoPago (" + tipoPago + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable idTipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPago> findTipoPagoEntities() {
        return findTipoPagoEntities(true, -1, -1);
    }

    public List<TipoPago> findTipoPagoEntities(int maxResults, int firstResult) {
        return findTipoPagoEntities(false, maxResults, firstResult);
    }

    private List<TipoPago> findTipoPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPago.class));
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

    public TipoPago findTipoPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPago> rt = cq.from(TipoPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
