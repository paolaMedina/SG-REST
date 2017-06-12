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
import Clases.Factura;
import Clases.ProductoFactura;
import Clases.ProductoFacturaPK;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Daniel
 */
public class ProductoFacturaJpaController implements Serializable {

    public ProductoFacturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoFactura productoFactura) throws PreexistingEntityException, Exception {
        if (productoFactura.getProductoFacturaPK() == null) {
            productoFactura.setProductoFacturaPK(new ProductoFacturaPK());
        }
        productoFactura.getProductoFacturaPK().setIdFactura(productoFactura.getFactura().getIdFactura());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura = productoFactura.getFactura();
            if (factura != null) {
                factura = em.getReference(factura.getClass(), factura.getIdFactura());
                productoFactura.setFactura(factura);
            }
            em.persist(productoFactura);
            if (factura != null) {
                factura.getProductoFacturaCollection().add(productoFactura);
                factura = em.merge(factura);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductoFactura(productoFactura.getProductoFacturaPK()) != null) {
                throw new PreexistingEntityException("ProductoFactura " + productoFactura + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoFactura productoFactura) throws NonexistentEntityException, Exception {
        productoFactura.getProductoFacturaPK().setIdFactura(productoFactura.getFactura().getIdFactura());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoFactura persistentProductoFactura = em.find(ProductoFactura.class, productoFactura.getProductoFacturaPK());
            Factura facturaOld = persistentProductoFactura.getFactura();
            Factura facturaNew = productoFactura.getFactura();
            if (facturaNew != null) {
                facturaNew = em.getReference(facturaNew.getClass(), facturaNew.getIdFactura());
                productoFactura.setFactura(facturaNew);
            }
            productoFactura = em.merge(productoFactura);
            if (facturaOld != null && !facturaOld.equals(facturaNew)) {
                facturaOld.getProductoFacturaCollection().remove(productoFactura);
                facturaOld = em.merge(facturaOld);
            }
            if (facturaNew != null && !facturaNew.equals(facturaOld)) {
                facturaNew.getProductoFacturaCollection().add(productoFactura);
                facturaNew = em.merge(facturaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductoFacturaPK id = productoFactura.getProductoFacturaPK();
                if (findProductoFactura(id) == null) {
                    throw new NonexistentEntityException("The productoFactura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductoFacturaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoFactura productoFactura;
            try {
                productoFactura = em.getReference(ProductoFactura.class, id);
                productoFactura.getProductoFacturaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoFactura with id " + id + " no longer exists.", enfe);
            }
            Factura factura = productoFactura.getFactura();
            if (factura != null) {
                factura.getProductoFacturaCollection().remove(productoFactura);
                factura = em.merge(factura);
            }
            em.remove(productoFactura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoFactura> findProductoFacturaEntities() {
        return findProductoFacturaEntities(true, -1, -1);
    }

    public List<ProductoFactura> findProductoFacturaEntities(int maxResults, int firstResult) {
        return findProductoFacturaEntities(false, maxResults, firstResult);
    }

    private List<ProductoFactura> findProductoFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoFactura.class));
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

    public ProductoFactura findProductoFactura(ProductoFacturaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoFactura.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<ProductoFactura> findProductoFacturaEntities(int idFactura) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("ProductoFactura.findByIdFactura");
            q.setParameter("idFactura", idFactura);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    //CONSULTA PRIMER REPORTE TOP 10 DE LOS PRODUCTOS MAS VENDIDOS EN EL MES
    public List<String> findProductoFacturaEntitiesPorMesYSemana(String mes, String año) {
        
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("SELECT nombre_producto, Sum(Producto_factura.cantidad) \n" +
                                            "FROM Factura NATURAL JOIN Producto_factura\n" +
                                            "WHERE (EXTRACT (YEAR FROM (fecha_hora)) = '"+ año + "')\n" +
                                            "	AND (EXTRACT (MONTH FROM (fecha_hora)) = '" + mes + "')\n" +
                                            "GROUP BY nombre_producto\n" +
                                            "ORDER BY sum DESC\n" +
                                            "LIMIT 10");
            
            return q.getResultList();
        } finally {
            em.close();
        }
    
    }
    //CONSULTA SEGUNDO REPORTE TOP 10 DE LOS PRODUCTOS MENOS VENDIDOS EN EL SEMESTRE
    public List<String> findProductoFacturaEntitiesPorSemestre(int semestre) {
        EntityManager em = getEntityManager();
        try {
            Query q ;
            if(semestre == 1){
                q = em.createNativeQuery("SELECT nombre_producto, Sum(Producto_factura.cantidad)"
                        + "FROM Factura NATURAL JOIN Producto_factura \n" +
                        "WHERE EXTRACT (MONTH FROM (fecha_hora)) BETWEEN '01' AND '06'  \n" +
                        "GROUP BY nombre_producto\n" +
                        "ORDER BY sum ASC\n" +
                        "LIMIT 10");
            }else{
                q = em.createNativeQuery("SELECT nombre_producto, Sum(Producto_factura.cantidad)"
                        + "FROM Factura NATURAL JOIN Producto_factura \n" +
                        "WHERE EXTRACT (MONTH FROM (fecha_hora)) BETWEEN '07' AND '12'  \n" +
                        "GROUP BY nombre_producto\n" +
                        "ORDER BY sum ASC\n" +
                        "LIMIT 10");
            }
                        
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getProductoFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoFactura> rt = cq.from(ProductoFactura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
