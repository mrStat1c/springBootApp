package ru.amelin.springBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amelin.springBoot.models.User;
import ru.amelin.springBoot.repositories.UserRepository;

/**
 * Сервис регистрации пользователей
 */
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    @Transactional
    public void register(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // для Spring название роли должно начинаться на "ROLE_"
        user.setRole("ROLE_USER");
        this.userRepository.save(user);
    }

}
