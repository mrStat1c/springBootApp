package ru.amelin.springBoot.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.amelin.springBoot.models.Customer;
import ru.amelin.springBoot.services.CustomerService;

/**
 * Валидатор объектов Customer
 */
@Component
public class CustomerValidator implements Validator {


    private final CustomerService customerService;

    @Autowired
    public CustomerValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Customer.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
        // Проверку уникальности в бд driver_lic нужно выполнять в одном из двух случаев:
        // 1) Customer новый (т.е. не существует в бд)
        // 2) Customer обновляется (т.е. существует в бд) и на форме введен новый номер DriverLicenseNumber (отличается от значения в бд)
        int id = customer.getId();
        if (!customerService.exists(id) || !customerService.get(id).getDriverLicenseNumber().equals(customer.getDriverLicenseNumber())) {
            if (customerService.exists(customer.getDriverLicenseNumber())) {
                errors.rejectValue("driverLicenseNumber", "", "Customer with same driver license number is already exists");
            }
        }
    }
}
