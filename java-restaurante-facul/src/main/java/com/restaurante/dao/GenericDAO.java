package com.restaurante.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class GenericDAO<T> {
  private static final String PERSISTENCE_UNIT_NAME = "my-persistence-unit";
  private static EntityManagerFactory emf;

  static {
    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
  }

  protected EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public List<T> findAll(Class<T> entityClass) {
    EntityManager em = getEntityManager();
    CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
    criteriaQuery.from(entityClass);
    List<T> entities = em.createQuery(criteriaQuery).getResultList();
    em.close();
    return entities;
  }

  public List<T> findListById(Class<T> entityClass, Long id) {
    EntityManager em = getEntityManager();
    CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
    criteriaQuery.from(entityClass);
    List<T> entities = em.createQuery(criteriaQuery).getResultList();
    em.close();
    return entities;
  }

  public T findById(Class<T> entityClass, Integer id) {
    EntityManager em = getEntityManager();
    T entity = em.find(entityClass, id);
    em.close();
    return entity;
  }

  // Cliente
  public T findByIdLong(Class<T> entityClass, Long id) {
    EntityManager em = getEntityManager();
    T entity = em.find(entityClass, id);
    em.close();
    return entity;
  }

  public void create(T entity) {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    em.persist(entity);
    em.getTransaction().commit();
    em.close();
  }

  public void update(T entity) {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    em.merge(entity);
    em.getTransaction().commit();
    em.close();
  }

  public void delete(T entity) {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();
    em.remove(em.merge(entity));
    em.getTransaction().commit();
    em.close();
  }
}
