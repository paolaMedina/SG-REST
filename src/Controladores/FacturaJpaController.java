/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Factura;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Clases.Pedido;
import Clases.Pago;
import Clases.ProductoFactura;
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
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) {
        if (factura.getProductoFacturaCollection() == null) {
            factura.setProductoFacturaCollection(new ArrayList<ProductoFactura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido numPedido = factura.getNumPedido();
            if (numPedido != null) {
                numPedido = em.getReference(numPedido.getClass(), numPedido.getNumPedido());
                factura.setNumPedido(numPedido);
            }
            Pago idPago = factura.getIdPago();
            if (idPago != null) {
                idPago = em.getReference(idPago.getClass(), idPago.getIdPago());
                factura.setIdPago(idPago);
            }
            Collection<ProductoFactura> attachedProductoFacturaCollection = new ArrayList<ProductoFactura>();
            for (ProductoFactura productoFacturaCollectionProductoFacturaToAttach : factura.getProductoFacturaCollection()) {
                productoFacturaCollectionProductoFacturaToAttach = em.getReference(productoFacturaCollectionProductoFacturaToAttach.getClass(), productoFacturaCollectionProductoFacturaToAttach.getProductoFacturaPK());
                attachedProductoFacturaCollection.add(productoFacturaCollectionProductoFacturaToAttach);
            }
            factura.setProductoFacturaCollection(attachedProductoFacturaCollection);
            em.persist(factura);
            if (numPedido != null) {
                numPedido.getFacturaCollection().add(factura);
                numPedido = em.merge(numPedido);
            }
            if (idPago != null) {
                idPago.getFacturaCollection().add(factura);
                idPago = em.merge(idPago);
            }
            for (ProductoFactura productoFacturaCollectionProductoFactura : factura.getProductoFacturaCollection()) {
                Factura oldFacturaOfProductoFacturaCollectionProductoFactura = productoFacturaCollectionProductoFactura.getFactura();
                productoFacturaCollectionProductoFactura.setFactura(factura);
                productoFacturaCollectionProductoFactura = em.merge(productoFacturaCollectionProductoFactura);
                if (oldFacturaOfProductoFacturaCollectionProductoFactura != null) {
                    oldFacturaOfProductoFacturaCollectionProductoFactura.getProductoFacturaCollection().remove(productoFacturaCollectionProductoFactura);
                    oldFacturaOfProductoFacturaCollectionProductoFactura = em.merge(oldFacturaOfProductoFacturaCollectionProductoFactura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactura());
            Pedido numPedidoOld = persistentFactura.getNumPedido();
            Pedido numPedidoNew = factura.getNumPedido();
            Pago idPagoOld = persistentFactura.getIdPago();
            Pago idPagoNew = factura.getIdPago();
            Collection<ProductoFactura> productoFacturaCollectionOld = persistentFactura.getProductoFacturaCollection();
            Collection<ProductoFactura> productoFacturaCollectionNew = factura.getProductoFacturaCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductoFactura productoFacturaCollectionOldProductoFactura : productoFacturaCollectionOld) {
                if (!productoFacturaCollectionNew.contains(productoFacturaCollectionOldProductoFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoFactura " + productoFacturaCollectionOldProductoFactura + " since its factura field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (numPedidoNew != null) {
                numPedidoNew = em.getReference(numPedidoNew.getClass(), numPedidoNew.getNumPedido());
                factura.setNumPedido(numPedidoNew);
            }
            if (idPagoNew != null) {
                idPagoNew = em.getReference(idPagoNew.getClass(), idPagoNew.getIdPago());
                factura.setIdPago(idPagoNew);
            }
            Collection<ProductoFactura> attachedProductoFacturaCollectionNew = new ArrayList<ProductoFactura>();
            for (ProductoFactura productoFacturaCollectionNewProductoFacturaToAttach : productoFacturaCollectionNew) {
                productoFacturaCollectionNewProductoFacturaToAttach = em.getReference(productoFacturaCollectionNewProductoFacturaToAttach.getClass(), productoFacturaCollectionNewProductoFacturaToAttach.getProductoFacturaPK());
                attachedProductoFacturaCollectionNew.add(productoFacturaCollectionNewProductoFacturaToAttach);
            }
            productoFacturaCollectionNew = attachedProductoFacturaCollectionNew;
            factura.setProductoFacturaCollection(productoFacturaCollectionNew);
            factura = em.merge(factura);
            if (numPedidoOld != null && !numPedidoOld.equals(numPedidoNew)) {
                numPedidoOld.getFacturaCollection().remove(factura);
                numPedidoOld = em.merge(numPedidoOld);
            }
            if (numPedidoNew != null && !numPedidoNew.equals(numPedidoOld)) {
                numPedidoNew.getFacturaCollection().add(factura);
                numPedidoNew = em.merge(numPedidoNew);
            }
            if (idPagoOld != null && !idPagoOld.equals(idPagoNew)) {
                idPagoOld.getFacturaCollection().remove(factura);
                idPagoOld = em.merge(idPagoOld);
            }
            if (idPagoNew != null && !idPagoNew.equals(idPagoOld)) {
                idPagoNew.getFacturaCollection().add(factura);
                idPagoNew = em.merge(idPagoNew);
            }
            for (ProductoFactura productoFacturaCollectionNewProductoFactura : productoFacturaCollectionNew) {
                if (!productoFacturaCollectionOld.contains(productoFacturaCollectionNewProductoFactura)) {
                    Factura oldFacturaOfProductoFacturaCollectionNewProductoFactura = productoFacturaCollectionNewProductoFactura.getFactura();
                    productoFacturaCollectionNewProductoFactura.setFactura(factura);
                    productoFacturaCollectionNewProductoFactura = em.merge(productoFacturaCollectionNewProductoFactura);
                    if (oldFacturaOfProductoFacturaCollectionNewProductoFactura != null && !oldFacturaOfProductoFacturaCollectionNewProductoFactura.equals(factura)) {
                        oldFacturaOfProductoFacturaCollectionNewProductoFactura.getProductoFacturaCollection().remove(productoFacturaCollectionNewProductoFactura);
                        oldFacturaOfProductoFacturaCollectionNewProductoFactura = em.merge(oldFacturaOfProductoFacturaCollectionNewProductoFactura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductoFactura> productoFacturaCollectionOrphanCheck = factura.getProductoFacturaCollection();
            for (ProductoFactura productoFacturaCollectionOrphanCheckProductoFactura : productoFacturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the ProductoFactura " + productoFacturaCollectionOrphanCheckProductoFactura + " in its productoFacturaCollection field has a non-nullable factura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pedido numPedido = factura.getNumPedido();
            if (numPedido != null) {
                numPedido.getFacturaCollection().remove(factura);
                numPedido = em.merge(numPedido);
            }
            Pago idPago = factura.getIdPago();
            if (idPago != null) {
                idPago.getFacturaCollection().remove(factura);
                idPago = em.merge(idPago);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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
    
    public List<Factura> findFacturaEntitiesPorMesyAño(String mes, String año) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("SELECT * FROM Factura "
                    + "WHERE (EXTRACT (YEAR FROM (fecha_hora)) = '"
                    + año
                    + "') AND (EXTRACT (MONTH FROM (fecha_hora)) = '"
                    + mes
                    + "');");
            
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
