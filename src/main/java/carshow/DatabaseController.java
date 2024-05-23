package carshow;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class DatabaseController {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory()
    {   // try creating session with provided config
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }
        catch(Throwable ex)
        {
            // Throw exception
            System.err.println("Error thrown in buildingFactory. "+ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public static void updateDB(CarShowroom carShowroom) {
        Transaction transaction = null;

        try (Session session = DatabaseController.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(carShowroom);
            transaction.commit();
        } catch (RuntimeException ignore) {
            if (transaction != null) transaction.rollback();
        }

    }

    public static List<CarShowroom> loadDB()
    {
        try (Session session = DatabaseController.getSessionFactory().openSession())
        {
            // HQL query
            String hql = "FROM CarShowroom";
            Query<CarShowroom> query = session.createQuery(hql, CarShowroom.class);
//            System.out.println(query.list().getFirst().getLista_samochodow().getFirst());
            System.out.println(query.list().getFirst().getLista_samochodow().size());
            return query.list();
        } catch (Throwable e)
        {
            System.out.println("Couldn't create a session. " + e);
            return null;
        }
    }
    public static void removeDB(CarShowroom carShowroom)
    {
        Transaction transaction = null;
        try (Session session = DatabaseController.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(carShowroom);
            transaction.commit();
        } catch (RuntimeException ignore) {
            if (transaction != null) transaction.rollback();
        }
    }

    public static void addData(CarShowroom carShowroom)
    {
        Transaction transaction = null;
        try (Session session = DatabaseController.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(carShowroom);
            transaction.commit();
        } catch (RuntimeException ignore) {
            if (transaction != null) transaction.rollback();
        }
    }
    public static void shutdown()
    {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
