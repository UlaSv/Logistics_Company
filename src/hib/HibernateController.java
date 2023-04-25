package hib;

import model.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernateController {
    private EntityManagerFactory emf;

    public HibernateController(EntityManagerFactory entityManagerFactory) {
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
        } catch (RollbackException e) {
            Throwable cause = e.getCause();
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            System.err.println("Error while committing the transaction: " + cause.getMessage());
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

    public List<Comment> getCommentsForForum(int forumID) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
        Root<Comment> comment = query.from(Comment.class);
        query.select(comment).where(cb.equal(comment.get("parentForum"), forumID));
        Query q;
        q = em.createQuery(query);
        try {
            List<Comment> result = q.getResultList();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such comments");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
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
    public List<Checkpoint> getUnassignedCheckpoints() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Checkpoint> cq = cb.createQuery(Checkpoint.class);
        Root<Checkpoint> root = cq.from(Checkpoint.class);
        cq.select(root).where(cb.isNull(root.get("destination")));
        return em.createQuery(cq).getResultList();
    }


    public List<Truck> getAllTrucksThatAreNotTaken() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Truck> cq = cb.createQuery(Truck.class);
        Root<Truck> root = cq.from(Truck.class);
        cq.select(root).where(cb.isNull(root.get("currentDestination")));
        if(em.createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return em.createQuery(cq).getResultList();

    }

    public List<Cargo> getAllCargoThatIsNotTaken() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cargo> cq = cb.createQuery(Cargo.class);
        Root<Cargo> root = cq.from(Cargo.class);
        cq.select(root).where(cb.isNull(root.get("destination")));
        if(em.createQuery(cq).getResultList().isEmpty())
            return null;
        else
            return em.createQuery(cq).getResultList();
    }
    public Map<StatusType, Long> getDestinationStatusCounts() {
        Map<StatusType, Long> counts = new HashMap<>();

        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Destination> root = cq.from(Destination.class);
            cq.select(cb.count(root));

            cq.where(cb.equal(root.get("status"), StatusType.WAITING_FOR_INFO));
            Long waitingCount = em.createQuery(cq).getSingleResult();
            counts.put(StatusType.WAITING_FOR_INFO, waitingCount);

            cq.where(cb.equal(root.get("status"), StatusType.READY));
            Long readyCount = em.createQuery(cq).getSingleResult();
            counts.put(StatusType.READY, readyCount);

            cq.where(cb.equal(root.get("status"), StatusType.ON_ROAD));
            Long inProgressCount = em.createQuery(cq).getSingleResult();
            counts.put(StatusType.ON_ROAD, inProgressCount);

            cq.where(cb.equal(root.get("status"), StatusType.DONE));
            Long completedCount = em.createQuery(cq).getSingleResult();
            counts.put(StatusType.DONE, completedCount);

            return counts;
        } finally {
            em.close();
        }
    }
}
