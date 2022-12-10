package ru.amelin.springBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.amelin.springBoot.models.User;
import ru.amelin.springBoot.security.UserDetailsImpl;
import ru.amelin.springBoot.services.AdminService;
import ru.amelin.springBoot.services.RegistrationService;

@Controller
@RequestMapping("/hello")
public class HelloController {

    private final RegistrationService registrationService;
    private final AdminService adminService;

    @Autowired
    public HelloController(RegistrationService registrationService, AdminService adminService) {
        this.registrationService = registrationService;
        this.adminService = adminService;
    }

    @GetMapping
    public String sayHello() {
        //получение данных о пользователе (почему-то падает при касте)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails.getUser());
        return "hello/sayHello";
    }

    @GetMapping("/login")
    // кастомная страница с логином (указывается в SpringConfig). Если кастомная страница не реализована,
    // будет использоваться стандартная
    public String loginPage() {
        return "hello/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "hello/registration";
    }

    @PostMapping("/registration")
    //по хорошему нужно добавить валидацию объекта и выводить ошибки при ошибках валидации
    public String registration(@ModelAttribute("user") User user) {
        //по хорошему нужно добавить валидатор, который проверяет объект на уникальность (есть ли цже такой пользователь в бд)
        this.registrationService.register(user);
        return "redirect:hello/login";
    }

    @GetMapping("/admin")
    public String registration() {
        this.adminService.doIt();
        return "hello/admin";
    }

}


