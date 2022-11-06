package ru.amelin.springBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.amelin.springBoot.models.Customer;
import ru.amelin.springBoot.models.Motocycle;
import ru.amelin.springBoot.services.CustomerService;
import ru.amelin.springBoot.services.MotocycleService;
import ru.amelin.springBoot.validators.MotocycleValidator;

import javax.validation.Valid;

/**
 * Обрабатывает запросы, приходящие на /moto
 */
@Controller
@RequestMapping("/moto")
public class MotocycleController {

    private final MotocycleService motocycleService;
    private final CustomerService customerService;
    private final MotocycleValidator motocycleValidator;

    @Autowired
    public MotocycleController(MotocycleService motocycleService, CustomerService customerService, MotocycleValidator motocycleValidator) {
        this.motocycleService = motocycleService;
        this.customerService = customerService;
        this.motocycleValidator = motocycleValidator;
    }

    /**
     * Преобразует пустые строки в null при отправке формы
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("motoList", motocycleService.getAll());
        return "moto/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int motoId, Model model) {
        Motocycle motocycle = motocycleService.get(motoId);
        model.addAttribute("moto", motocycle);
        model.addAttribute("renter", new Customer());
        if (motocycle.getCustomer() != null) {
            model.addAttribute("customer", customerService.get(motocycle.getCustomer().getId()));
        } else {
            model.addAttribute("customers", customerService.getAll());
        }
        return "moto/show";
    }

    @GetMapping("/create")
    public String createForm(@ModelAttribute("moto") Motocycle motocycle) {
        return "moto/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("moto") @Valid Motocycle motocycle,
                         BindingResult bindingResult) {
        motocycleValidator.validate(motocycle, bindingResult);
        if (bindingResult.hasErrors()) {
            return "moto/create";
        }
        this.motocycleService.add(motocycle);
        return "redirect:/moto";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int motoId, Model model) {
        model.addAttribute("moto", motocycleService.get(motoId));
        return "moto/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int motoId,
                         @ModelAttribute("moto") @Valid Motocycle motocycle,
                         BindingResult bindingResult) {
        motocycleValidator.validate(motocycle, bindingResult);
        if (bindingResult.hasErrors()) {
            return "moto/update";
        }
        this.motocycleService.update(motoId, motocycle);
        return "redirect:/moto";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int motoId) {
        this.motocycleService.delete(motoId);
        return "redirect:/moto";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int motoId) {
        this.motocycleService.release(motoId);
        return "redirect:/moto/{id}";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int motoId, @ModelAttribute("renter") Customer customer) {
        this.motocycleService.assign(motoId, customer);
        return "redirect:/moto/{id}";
    }
}
