package ru.mikhailblokhin.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.mikhailblokhin.entities.User;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(User user) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            session.persist(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User read(long id) {
        Transaction transaction = null;

        User user = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            user = session.find(User.class, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return user;
    }

    public void update(User user) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            User updatingUser = session.find(User.class, user.getId());
            updatingUser.setName(user.getName());
            updatingUser.setEmail(user.getEmail());
            updatingUser.setAge(user.getAge());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            User user = session.find(User.class, id);
            session.remove(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User readLast() {
        Transaction transaction = null;

        User user = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            user = session.createQuery("FROM User u ORDER BY u.id DESC LIMIT 1", User.class).getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return user;
    }

    public List<User> readAll() {
        Transaction transaction = null;

        List<User> list = null;

        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();

            list = session.createQuery("FROM User", User.class).getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return list;
    }

    public void exit() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
