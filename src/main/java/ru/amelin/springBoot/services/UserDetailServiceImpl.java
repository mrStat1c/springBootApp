package ru.amelin.springBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.amelin.springBoot.models.User;
import ru.amelin.springBoot.repositories.UserRepository;
import ru.amelin.springBoot.security.UserDetailsImpl;

import java.util.Optional;

/**
 * Реализация UserDetailsService. Нужен для получения обертки UserDetails
 */
@Service
@Transactional(readOnly = true)
//UserDetailsService нужен для Spring Security (???)
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new UserDetailsImpl(user.get());
    }
}
