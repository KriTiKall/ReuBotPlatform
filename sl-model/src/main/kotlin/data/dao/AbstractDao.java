package data.dao;

import data.DAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public abstract class AbstractDao<Entity, Key extends Serializable> implements DAO<Entity, Key> {

    protected final SessionFactory sessionFactory;

    private Entity entity;

    public AbstractDao(SessionFactory sessionFactory, Entity entity) {
        this.entity = entity;
        this.sessionFactory = sessionFactory;
    }

    public AbstractDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Entity entity) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Entity client) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Entity entity) {
        try (final Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public Entity read(Key key) {
        try (final Session session = sessionFactory.openSession()) {
            Entity entity = (Entity) session.get(this.entity.getClass(), key);
            return entity != null ? entity : (Entity) this.entity.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Entity> getList() {
        try (final Session session = sessionFactory.openSession()) {
            return session.getSession().createQuery("from " + entity.getClass().getName()).list();
        }
    }

    public Entity read(Key key, Entity clazz) {
        try (final Session session = sessionFactory.openSession()) {
            Entity entity = (Entity) session.get(clazz.getClass(), key);
            return entity != null ? entity : (Entity) clazz.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Entity> getList(String name) {
        try (final Session session = sessionFactory.openSession()) {
            return session.getSession().createQuery("from " + name).list();
        }
    }
}