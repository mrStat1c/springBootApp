package ru.amelin.springBoot.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Сервис для админов
 */
@Service
public class AdminService {

    // метод будет вызываться только для пользователя с ролью ROLE_ADMIN
    // для других пользователей будет ошибка
    @PreAuthorize("hasRole('ROLE_ADMIN')")//можно писать разные выражения
    public void doIt(){
        System.out.println("SUPER HOT! SUPER HOT! SUPER HOT!");
    }
}
