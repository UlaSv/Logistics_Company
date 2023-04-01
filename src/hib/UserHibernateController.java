package hib;

import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserHibernateController {
    private EntityManagerFactory emf;

    public UserHibernateController(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public <T> void save(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public <T> void update(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public <T> void delete(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    public <T> T getEntityById(Class<T> entity, int id) {
        EntityManager em = null;
        T result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            result = em.find(entity, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such entity by given Id");
        }
        return result;
    }

    public User getUserById(int id) {
        EntityManager em = null;
        User result = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            result = em.find(User.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such entity by given Id");
        }
        return result;
    }

    public <T> List<T> getAllRecords(Class<T> entity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(entity));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return null;
    }

    public User findUserByCredentials(String login, String password) throws Exception {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
        Query q;
        q = em.createQuery(query);
        try {
            User singleResult = (User) q.getSingleResult();
            return singleResult;
        } catch (NoResultException e) {
            return null;
        }

    }


}
