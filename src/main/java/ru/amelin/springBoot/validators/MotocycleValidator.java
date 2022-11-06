package ru.amelin.springBoot.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.amelin.springBoot.models.Motocycle;
import ru.amelin.springBoot.services.MotocycleService;

/**
 * Валидатор объектов Motocycle
 */
@Component
public class MotocycleValidator implements Validator {

    private final MotocycleService motocycleService;

    @Autowired
    public MotocycleValidator(MotocycleService motocycleService) {
        this.motocycleService = motocycleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Motocycle.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Motocycle motocycle = (Motocycle) target;
        // Проверку уникальности в бд vin нужно выполнять в одном из двух случаев:
        // 1) Motocycle новый (т.е. не существует в бд)
        // 2) Motocycle обновляется (т.е. существует в бд) и на форме введен новый номер vin (отличается от значения в бд)
        int id = motocycle.getId();
        if (!motocycleService.exists(id) || !motocycleService.get(id).getVin().equals(motocycle.getVin())) {
            if (motocycleService.exists(motocycle.getVin())) {
                errors.rejectValue("vin", "", "Motocycle with same vin is already exists");
            }
        }
    }
}
