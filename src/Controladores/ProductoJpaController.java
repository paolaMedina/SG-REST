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
import Clases.CategoriaProducto;
import Clases.Producto;
import Clases.ProductoPK;
import Clases.ProductoPedido;
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
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getProductoPK() == null) {
            producto.setProductoPK(new ProductoPK());
        }
        if (producto.getProductoPedidoCollection() == null) {
            producto.setProductoPedidoCollection(new ArrayList<ProductoPedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoriaProducto idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                producto.setIdCategoria(idCategoria);
            }
            Collection<ProductoPedido> attachedProductoPedidoCollection = new ArrayList<ProductoPedido>();
            for (ProductoPedido productoPedidoCollectionProductoPedidoToAttach : producto.getProductoPedidoCollection()) {
                productoPedidoCollectionProductoPedidoToAttach = em.getReference(productoPedidoCollectionProductoPedidoToAttach.getClass(), productoPedidoCollectionProductoPedidoToAttach.getProductoPedidoPK());
                attachedProductoPedidoCollection.add(productoPedidoCollectionProductoPedidoToAttach);
            }
            producto.setProductoPedidoCollection(attachedProductoPedidoCollection);
            em.persist(producto);
            if (idCategoria != null) {
                idCategoria.getProductoCollection().add(producto);
                idCategoria = em.merge(idCategoria);
            }
            for (ProductoPedido productoPedidoCollectionProductoPedido : producto.getProductoPedidoCollection()) {
                Producto oldProductoOfProductoPedidoCollectionProductoPedido = productoPedidoCollectionProductoPedido.getProducto();
                productoPedidoCollectionProductoPedido.setProducto(producto);
                productoPedidoCollectionProductoPedido = em.merge(productoPedidoCollectionProductoPedido);
                if (oldProductoOfProductoPedidoCollectionProductoPedido != null) {
                    oldProductoOfProductoPedidoCollectionProductoPedido.getProductoPedidoCollection().remove(productoPedidoCollectionProductoPedido);
                    oldProductoOfProductoPedidoCollectionProductoPedido = em.merge(oldProductoOfProductoPedidoCollectionProductoPedido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getProductoPK()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getProductoPK());
            CategoriaProducto idCategoriaOld = persistentProducto.getIdCategoria();
            CategoriaProducto idCategoriaNew = producto.getIdCategoria();
            Collection<ProductoPedido> productoPedidoCollectionOld = persistentProducto.getProductoPedidoCollection();
            Collection<ProductoPedido> productoPedidoCollectionNew = producto.getProductoPedidoCollection();
            List<String> illegalOrphanMessages = null;
            for (ProductoPedido productoPedidoCollectionOldProductoPedido : productoPedidoCollectionOld) {
                if (!productoPedidoCollectionNew.contains(productoPedidoCollectionOldProductoPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductoPedido " + productoPedidoCollectionOldProductoPedido + " since its producto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                producto.setIdCategoria(idCategoriaNew);
            }
            /*
            Collection<ProductoPedido> attachedProductoPedidoCollectionNew = new ArrayList<ProductoPedido>();
            for (ProductoPedido productoPedidoCollectionNewProductoPedidoToAttach : productoPedidoCollectionNew) {
                productoPedidoCollectionNewProductoPedidoToAttach = em.getReference(productoPedidoCollectionNewProductoPedidoToAttach.getClass(), productoPedidoCollectionNewProductoPedidoToAttach.getProductoPedidoPK());
                attachedProductoPedidoCollectionNew.add(productoPedidoCollectionNewProductoPedidoToAttach);
            }
            productoPedidoCollectionNew = attachedProductoPedidoCollectionNew;*/
            producto.setProductoPedidoCollection(productoPedidoCollectionNew);
            producto = em.merge(producto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoCollection().remove(producto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoCollection().add(producto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }/*
            for (ProductoPedido productoPedidoCollectionNewProductoPedido : productoPedidoCollectionNew) {
                if (!productoPedidoCollectionOld.contains(productoPedidoCollectionNewProductoPedido)) {
                    Producto oldProductoOfProductoPedidoCollectionNewProductoPedido = productoPedidoCollectionNewProductoPedido.getProducto();
                    productoPedidoCollectionNewProductoPedido.setProducto(producto);
                    productoPedidoCollectionNewProductoPedido = em.merge(productoPedidoCollectionNewProductoPedido);
                    if (oldProductoOfProductoPedidoCollectionNewProductoPedido != null && !oldProductoOfProductoPedidoCollectionNewProductoPedido.equals(producto)) {
                        oldProductoOfProductoPedidoCollectionNewProductoPedido.getProductoPedidoCollection().remove(productoPedidoCollectionNewProductoPedido);
                        oldProductoOfProductoPedidoCollectionNewProductoPedido = em.merge(oldProductoOfProductoPedidoCollectionNewProductoPedido);
                    }
                }
            }*/
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductoPK id = producto.getProductoPK();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getProductoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProductoPedido> productoPedidoCollectionOrphanCheck = producto.getProductoPedidoCollection();
            for (ProductoPedido productoPedidoCollectionOrphanCheckProductoPedido : productoPedidoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the ProductoPedido " + productoPedidoCollectionOrphanCheckProductoPedido + " in its productoPedidoCollection field has a non-nullable producto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CategoriaProducto idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoCollection().remove(producto);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(ProductoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
