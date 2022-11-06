package ru.amelin.springBoot.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.amelin.springBoot.models.Customer;
import ru.amelin.springBoot.services.CustomerService;
import ru.amelin.springBoot.services.MotocycleService;
import ru.amelin.springBoot.validators.CustomerValidator;

import javax.validation.Valid;
import java.util.List;


/**
 * Обрабатывает запросы, приходящие на /customers
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final MotocycleService motocycleService;
    private final CustomerValidator customerValidator;
    private static final Logger log = LogManager.getLogger(CustomerController.class.getName());

    @Autowired
    public CustomerController(CustomerService customerService, MotocycleService motocycleService, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.motocycleService = motocycleService;
        this.customerValidator = customerValidator;
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
        model.addAttribute("customers", customerService.getAll());
        return "customers/index";
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "namePart", required = false) String namePart) {
        List<Customer> customerList;
        if (namePart == null) {
            customerList = this.customerService.getAll();
        } else {
            customerList = this.customerService.search(namePart);
        }
        model.addAttribute("customers", customerList);
        return "customers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int customerId, Model model) {
        Customer customer = this.customerService.get(customerId);
        model.addAttribute("customer", customer);
        model.addAttribute("motoList", this.motocycleService.findByCustomer(customer));
        return "customers/show";
    }

    @GetMapping("/create")
    public String createForm(@ModelAttribute("customer") Customer customer) {
        return "customers/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("customer") @Valid Customer customer,
                         BindingResult bindingResult) {
        this.customerValidator.validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            return "customers/create";
        }
        this.customerService.add(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int customerId, Model model) {
        model.addAttribute("customer", this.customerService.get(customerId));
        return "customers/update";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int customerId,
                         @ModelAttribute("customer") @Valid Customer customer,
                         BindingResult bindingResult) {
        this.customerValidator.validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/customers/update";
        }
        this.customerService.update(customerId, customer);
        return "redirect:/customers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int customerId) {
        this.customerService.delete(customerId);
        return "redirect:/customers";
    }
}