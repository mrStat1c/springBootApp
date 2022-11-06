package ru.amelin.springBoot.dao;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.amelin.springBoot.models.Customer;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO для работы с объектами Customer
 */
@Component
public class CustomerDAO {

    private final EntityManager entityManager;

    @Autowired
    public CustomerDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Пример использования JOIN. Метод нигде не используется
    @Transactional(readOnly = true)
    public Set<Customer> activeCustomers() {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM Customer c INNER JOIN FETCH c.motocycleList";
        List<Customer> activeCustomers = session.createQuery(hql).getResultList();
        //Set для того, чтобы избавиться от дубликатов т.к. использовался JOIN
        return new HashSet<>(activeCustomers);
    }
}
