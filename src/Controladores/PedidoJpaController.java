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
import Clases.Mesa;
import Clases.EstadoPedido;
import Clases.Empleado;
import Clases.Factura;
import Clases.Pedido;
import java.util.ArrayList;
import java.util.Collection;
import Clases.ProductoPedido;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getFacturaCollection() == null) {
            pedido.setFacturaCollection(new ArrayList<Factura>());
        }
        if (pedido.getProductoPedidoCollection() == null) {
            pedido.setProductoPedidoCollection(new ArrayList<ProductoPedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mesa numMesa = pedido.getNumMesa();
            if (numMesa != null) {
                numMesa = em.getReference(numMesa.getClass(), numMesa.getNumMesa());
                pedido.setNumMesa(numMesa);
            }
            EstadoPedido idEstadoPedido = pedido.getIdEstadoPedido();
            if (idEstadoPedido != null) {
                idEstadoPedido = em.getReference(idEstadoPedido.getClass(), idEstadoPedido.getIdEstadoPedido());
                pedido.setIdEstadoPedido(idEstadoPedido);
            }
            Empleado idEmpleado = pedido.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdentificacion());
                pedido.setIdEmpleado(idEmpleado);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : pedido.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            pedido.setFacturaCollection(attachedFacturaCollection);
            Collection<ProductoPedido> attachedProductoPedidoCollection = new ArrayList<ProductoPedido>();
            for (ProductoPedido productoPedidoCollectionProductoPedidoToAttach : pedido.getProductoPedidoCollection()) {
                productoPedidoCollectionProductoPedidoToAttach = em.getReference(productoPedidoCollectionProductoPedidoToAttach.getClass(), productoPedidoCollectionProductoPedidoToAttach.getProductoPedidoPK());
                attachedProductoPedidoCollection.add(productoPedidoCollectionProductoPedidoToAttach);
            }
            pedido.setProductoPedidoCollection(attachedProductoPedidoCollection);
            em.persist(pedido);
            if (numMesa != null) {
                numMesa.getPedidoCollection().add(pedido);
                numMesa = em.merge(numMesa);
            }
            if (idEstadoPedido != null) {
                idEstadoPedido.getPedidoCollection().add(pedido);
                idEstadoPedido = em.merge(idEstadoPedido);
            }
            if (idEmpleado != null) {
                idEmpleado.getPedidoCollection().add(pedido);
                idEmpleado = em.merge(idEmpleado);
            }
            for (Factura facturaCollectionFactura : pedido.getFacturaCollection()) {
                Pedido oldNumPedidoOfFacturaCollectionFactura = facturaCollectionFactura.getNumPedido();
                facturaCollectionFactura.setNumPedido(pedido);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldNumPedidoOfFacturaCollectionFactura != null) {
                    oldNumPedidoOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldNumPedidoOfFacturaCollectionFactura = em.merge(oldNumPedidoOfFacturaCollectionFactura);
                }
            }
            for (ProductoPedido productoPedidoCollectionProductoPedido : pedido.getProductoPedidoCollection()) {
                Pedido oldPedidoOfProductoPedidoCollectionProductoPedido = productoPedidoCollectionProductoPedido.getPedido();
                productoPedidoCollectionProductoPedido.setPedido(pedido);
                productoPedidoCollectionProductoPedido = em.merge(productoPedidoCollectionProductoPedido);
                if (oldPedidoOfProductoPedidoCollectionProductoPedido != null) {
                    oldPedidoOfProductoPedidoCollectionProductoPedido.getProductoPedidoCollection().remove(productoPedidoCollectionProductoPedido);
                    oldPedidoOfProductoPedidoCollectionProductoPedido = em.merge(oldPedidoOfProductoPedidoCollectionProductoPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getNumPedido());
            Mesa numMesaOld = persistentPedido.getNumMesa();
            Mesa numMesaNew = pedido.getNumMesa();
            EstadoPedido idEstadoPedidoOld = persistentPedido.getIdEstadoPedido();
            EstadoPedido idEstadoPedidoNew = pedido.getIdEstadoPedido();
            Empleado idEmpleadoOld = persistentPedido.getIdEmpleado();
            Empleado idEmpleadoNew = pedido.getIdEmpleado();
            Collection<Factura> facturaCollectionOld = persistentPedido.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = pedido.getFacturaCollection();
            Collection<ProductoPedido> productoPedidoCollectionOld = persistentPedido.getProductoPedidoCollection();
            Collection<ProductoPedido> productoPedidoCollectionNew = pedido.getProductoPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its numPedido field is not nullable.");
                }
            }
            for (ProductoPedido productoPedidoCollectionOldProductoPedido : productoPedidoCollectionOld) {
                if (!productoPedidoCollectionNew.contains(productoPedidoCollectionOldProductoPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoPedido " + productoPedidoCollectionOldProductoPedido + " since its pedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (numMesaNew != null) {
                numMesaNew = em.getReference(numMesaNew.getClass(), numMesaNew.getNumMesa());
                pedido.setNumMesa(numMesaNew);
            }
            if (idEstadoPedidoNew != null) {
                idEstadoPedidoNew = em.getReference(idEstadoPedidoNew.getClass(), idEstadoPedidoNew.getIdEstadoPedido());
                pedido.setIdEstadoPedido(idEstadoPedidoNew);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdentificacion());
                pedido.setIdEmpleado(idEmpleadoNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            pedido.setFacturaCollection(facturaCollectionNew);
            Collection<ProductoPedido> attachedProductoPedidoCollectionNew = new ArrayList<ProductoPedido>();
            for (ProductoPedido productoPedidoCollectionNewProductoPedidoToAttach : productoPedidoCollectionNew) {
                productoPedidoCollectionNewProductoPedidoToAttach = em.getReference(productoPedidoCollectionNewProductoPedidoToAttach.getClass(), productoPedidoCollectionNewProductoPedidoToAttach.getProductoPedidoPK());
                attachedProductoPedidoCollectionNew.add(productoPedidoCollectionNewProductoPedidoToAttach);
            }
            productoPedidoCollectionNew = attachedProductoPedidoCollectionNew;
            pedido.setProductoPedidoCollection(productoPedidoCollectionNew);
            pedido = em.merge(pedido);
            if (numMesaOld != null && !numMesaOld.equals(numMesaNew)) {
                numMesaOld.getPedidoCollection().remove(pedido);
                numMesaOld = em.merge(numMesaOld);
            }
            if (numMesaNew != null && !numMesaNew.equals(numMesaOld)) {
                numMesaNew.getPedidoCollection().add(pedido);
                numMesaNew = em.merge(numMesaNew);
            }
            if (idEstadoPedidoOld != null && !idEstadoPedidoOld.equals(idEstadoPedidoNew)) {
                idEstadoPedidoOld.getPedidoCollection().remove(pedido);
                idEstadoPedidoOld = em.merge(idEstadoPedidoOld);
            }
            if (idEstadoPedidoNew != null && !idEstadoPedidoNew.equals(idEstadoPedidoOld)) {
                idEstadoPedidoNew.getPedidoCollection().add(pedido);
                idEstadoPedidoNew = em.merge(idEstadoPedidoNew);
            }
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getPedidoCollection().remove(pedido);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getPedidoCollection().add(pedido);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Pedido oldNumPedidoOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getNumPedido();
                    facturaCollectionNewFactura.setNumPedido(pedido);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldNumPedidoOfFacturaCollectionNewFactura != null && !oldNumPedidoOfFacturaCollectionNewFactura.equals(pedido)) {
                        oldNumPedidoOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldNumPedidoOfFacturaCollectionNewFactura = em.merge(oldNumPedidoOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (ProductoPedido productoPedidoCollectionNewProductoPedido : productoPedidoCollectionNew) {
                if (!productoPedidoCollectionOld.contains(productoPedidoCollectionNewProductoPedido)) {
                    Pedido oldPedidoOfProductoPedidoCollectionNewProductoPedido = productoPedidoCollectionNewProductoPedido.getPedido();
                    productoPedidoCollectionNewProductoPedido.setPedido(pedido);
                    productoPedidoCollectionNewProductoPedido = em.merge(productoPedidoCollectionNewProductoPedido);
                    if (oldPedidoOfProductoPedidoCollectionNewProductoPedido != null && !oldPedidoOfProductoPedidoCollectionNewProductoPedido.equals(pedido)) {
                        oldPedidoOfProductoPedidoCollectionNewProductoPedido.getProductoPedidoCollection().remove(productoPedidoCollectionNewProductoPedido);
                        oldPedidoOfProductoPedidoCollectionNewProductoPedido = em.merge(oldPedidoOfProductoPedidoCollectionNewProductoPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getNumPedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getNumPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = pedido.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable numPedido field.");
            }
            Collection<ProductoPedido> productoPedidoCollectionOrphanCheck = pedido.getProductoPedidoCollection();
            for (ProductoPedido productoPedidoCollectionOrphanCheckProductoPedido : productoPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the ProductoPedido " + productoPedidoCollectionOrphanCheckProductoPedido + " in its productoPedidoCollection field has a non-nullable pedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Mesa numMesa = pedido.getNumMesa();
            if (numMesa != null) {
                numMesa.getPedidoCollection().remove(pedido);
                numMesa = em.merge(numMesa);
            }
            EstadoPedido idEstadoPedido = pedido.getIdEstadoPedido();
            if (idEstadoPedido != null) {
                idEstadoPedido.getPedidoCollection().remove(pedido);
                idEstadoPedido = em.merge(idEstadoPedido);
            }
            Empleado idEmpleado = pedido.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getPedidoCollection().remove(pedido);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
