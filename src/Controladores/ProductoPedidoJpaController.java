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
import Clases.Producto;
import Clases.Pedido;
import Clases.ProductoPedido;
import Clases.ProductoPedidoPK;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class ProductoPedidoJpaController implements Serializable {

    public ProductoPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoPedido productoPedido) throws PreexistingEntityException, Exception {
        if (productoPedido.getProductoPedidoPK() == null) {
            productoPedido.setProductoPedidoPK(new ProductoPedidoPK());
        }
        productoPedido.getProductoPedidoPK().setPedidoNumPedido(productoPedido.getPedido().getNumPedido());
        productoPedido.getProductoPedidoPK().setProductoId(productoPedido.getProducto().getProductoPK().getId());
        productoPedido.getProductoPedidoPK().setProductoNombre(productoPedido.getProducto().getProductoPK().getNombre());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto = productoPedido.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getProductoPK());
                productoPedido.setProducto(producto);
            }
            Pedido pedido = productoPedido.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getNumPedido());
                productoPedido.setPedido(pedido);
            }
            em.persist(productoPedido);
            if (producto != null) {
                producto.getProductoPedidoCollection().add(productoPedido);
                producto = em.merge(producto);
            }
            if (pedido != null) {
                pedido.getProductoPedidoCollection().add(productoPedido);
                pedido = em.merge(pedido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductoPedido(productoPedido.getProductoPedidoPK()) != null) {
                throw new PreexistingEntityException("ProductoPedido " + productoPedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoPedido productoPedido) throws NonexistentEntityException, Exception {
        productoPedido.getProductoPedidoPK().setPedidoNumPedido(productoPedido.getPedido().getNumPedido());
        productoPedido.getProductoPedidoPK().setProductoId(productoPedido.getProducto().getProductoPK().getId());
        productoPedido.getProductoPedidoPK().setProductoNombre(productoPedido.getProducto().getProductoPK().getNombre());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoPedido persistentProductoPedido = em.find(ProductoPedido.class, productoPedido.getProductoPedidoPK());
            Producto productoOld = persistentProductoPedido.getProducto();
            Producto productoNew = productoPedido.getProducto();
            Pedido pedidoOld = persistentProductoPedido.getPedido();
            Pedido pedidoNew = productoPedido.getPedido();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getProductoPK());
                productoPedido.setProducto(productoNew);
            }
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getNumPedido());
                productoPedido.setPedido(pedidoNew);
            }
            productoPedido = em.merge(productoPedido);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getProductoPedidoCollection().remove(productoPedido);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getProductoPedidoCollection().add(productoPedido);
                productoNew = em.merge(productoNew);
            }
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getProductoPedidoCollection().remove(productoPedido);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getProductoPedidoCollection().add(productoPedido);
                pedidoNew = em.merge(pedidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductoPedidoPK id = productoPedido.getProductoPedidoPK();
                if (findProductoPedido(id) == null) {
                    throw new NonexistentEntityException("The productoPedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductoPedidoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoPedido productoPedido;
            try {
                productoPedido = em.getReference(ProductoPedido.class, id);
                productoPedido.getProductoPedidoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoPedido with id " + id + " no longer exists.", enfe);
            }
            Producto producto = productoPedido.getProducto();
            if (producto != null) {
                producto.getProductoPedidoCollection().remove(productoPedido);
                producto = em.merge(producto);
            }
            Pedido pedido = productoPedido.getPedido();
            if (pedido != null) {
                pedido.getProductoPedidoCollection().remove(productoPedido);
                pedido = em.merge(pedido);
            }
            em.remove(productoPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoPedido> findProductoPedidoEntities() {
        return findProductoPedidoEntities(true, -1, -1);
    }

    public List<ProductoPedido> findProductoPedidoEntities(int maxResults, int firstResult) {
        return findProductoPedidoEntities(false, maxResults, firstResult);
    }

    private List<ProductoPedido> findProductoPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoPedido.class));
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

    public ProductoPedido findProductoPedido(ProductoPedidoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoPedido.class, id);
        } finally {
            em.close();
        }
    }
    

    public List<ProductoPedido> findProductoPedidoEntities(int numPedido) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("ProductoPedido.findByPedidoNumPedido");
            q.setParameter("pedidoNumPedido", numPedido);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
   
    
    
    public int getProductoPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoPedido> rt = cq.from(ProductoPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
