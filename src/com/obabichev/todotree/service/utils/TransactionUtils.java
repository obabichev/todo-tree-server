package com.obabichev.todotree.service.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.function.Function;

@Component("transactionUtils")
public class TransactionUtils {
    @Resource(name = "entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    public <R> R performInsideTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        try {

            R result = function.apply(entityManager);

            entityManager.getTransaction().commit();

            return result;

        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
