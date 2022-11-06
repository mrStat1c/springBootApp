package ru.amelin.springBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amelin.springBoot.models.Customer;
import ru.amelin.springBoot.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Дополнительный слой бизнес-логики между Controller и Repository
 */
@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return this.customerRepository.findAll(Sort.by("name"));
    }

    public Customer get(int id) {
        Optional<Customer> customer = this.customerRepository.findById(id);
        return customer.orElse(null);
    }

    //Transactional уже стоит на классе. Здесь она будет переопределяться, чтобы readOnly было false
    @Transactional
    public void add(Customer customer) {
        customer.setCreated(new Date());
        this.customerRepository.save(customer);
    }

    @Transactional
    public void update(int id, Customer customer) {
        customer.setId(id);
        this.customerRepository.save(customer);
    }

    @Transactional
    public void delete(int id) {
        this.customerRepository.deleteById(id);
    }

    public boolean exists(int id){
       return this.customerRepository.findById(id).isPresent();
    }

    public boolean exists(String driverLicenseNumber){
        return this.customerRepository
                .findByDriverLicenseNumber(driverLicenseNumber)
                .isPresent();
    }

    public List<Customer> search(String namePart) {
        return this.customerRepository.findByNameLike("%" + namePart + "%");
    }

}
