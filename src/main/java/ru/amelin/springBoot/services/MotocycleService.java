package ru.amelin.springBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amelin.springBoot.models.Customer;
import ru.amelin.springBoot.models.Motocycle;
import ru.amelin.springBoot.repositories.MotocycleRepository;

import java.util.Date;
import java.util.List;

/**
 * Дополнительный слой бизнес-логики между Controller и Repository
 */
@Service
@Transactional(readOnly = true)
public class MotocycleService {

    private final MotocycleRepository motocycleRepository;

    @Autowired
    public MotocycleService(MotocycleRepository motocycleRepository) {
        this.motocycleRepository = motocycleRepository;
    }

    public Motocycle get(int motoId) {
        return this.motocycleRepository.findById(motoId).orElse(null);
    }

    public List<Motocycle> getAll() {
        return this.motocycleRepository.findAll(Sort.by("model"));
    }

    //Transactional уже стоит на классе. Здесь она будет переопределяться, чтобы readOnly было false
    @Transactional
    public void add(Motocycle motocycle) {
        motocycle.setCreated(new Date());
        this.motocycleRepository.save(motocycle);
    }

    @Transactional
    public void update(int id, Motocycle motocycle) {
        motocycle.setId(id);
        this.motocycleRepository.save(motocycle);
    }

    @Transactional
    public void delete(int motoId) {
        this.motocycleRepository.deleteById(motoId);
    }

    @Transactional
    public void release(int motoId) {
        Motocycle motocycle = get(motoId);
        motocycle.setCustomer(null);
        this.motocycleRepository.save(motocycle);
    }

    @Transactional
    public void assign(int motoId, Customer customer) {
        Motocycle motocycle = get(motoId);
        motocycle.setCustomer(customer);
        this.motocycleRepository.save(motocycle);
    }

    public boolean exists(int id) {
       return this.motocycleRepository.findById(id).isPresent();
    }

    public boolean exists(String vin) {
        return this.motocycleRepository.findByVin(vin).isPresent();
    }

    public List<Motocycle> findByCustomer(Customer customer) {
        return this.motocycleRepository.findByCustomer(customer);
    }
}
