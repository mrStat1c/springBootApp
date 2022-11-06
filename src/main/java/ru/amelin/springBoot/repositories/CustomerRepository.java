package ru.amelin.springBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.amelin.springBoot.models.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Repository для объектов Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByDriverLicenseNumber(String driverLicenseNumber);

    List<Customer> findByNameLike(String namePart);

}
