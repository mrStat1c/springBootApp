package ru.amelin.springBoot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.amelin.springBoot.services.UserDetailServiceImpl;

/**
 * Настройки Spring Security
 */
@EnableWebSecurity
//включает возможность выставления прав (authority) на отдельные методы
//теперь можно использовать аннотацию @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public SecurityConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }


    //Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder
                .userDetailsService(this.userDetailService)
                .passwordEncoder(getPasswordEncoder());
    }

    //Настраивает какие формы для логина, ошибок и т.д. использовать
    // + конфигурирование авторизации
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //при обработке запросов проверка правил (настроек) идет сверху вниз:
        httpSecurity
                //отключение защиты от межсайтовой подделки запросов (по ум. включена)
//                .csrf().disable()
                //настройки авторизации
                .authorizeRequests()
                // к админской странице доступ только у админа. Вместо ROLE_ADMIN пишем просто ADMIN
//                .antMatchers("/hello/admin").hasRole("ADMIN")
                //к этим страницам доступ без авторизации
                .antMatchers("/hello/login", "/hello/registration", "/error").permitAll()
                //ко всем остальным страницам доступ у всех аутентифицированных
//                .anyRequest().authenticated()
                //ко всем остальным страницам доступ у админа или обычного пользователя
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                //настройки страницы аутентификации
                .formLogin()
                //адрес страницы логина
                .loginPage("/hello/login")
                //адрес, по которому Spring будет обрабатывать запросы с формы логина
                .loginProcessingUrl("/process_login")
                //адрес страницы, на которую будет перенаправляться пользователя после успешной аутентификации
                .defaultSuccessUrl("/hello", true)
                //адрес страницы, на которую будет перенаправляться пользователя после неуспешной аутентификации
                .failureUrl("/auth/login?error")
                .and()
                //конфигурация logout (при logout удаляются куки и сессия)
                .logout()
                //URL, по переходу на который, будет происходить logout
                .logoutUrl("/logout")
                //URL, на который будет перебрасывать при успешном logout
                .logoutSuccessUrl("/hello/login");
    }


    @Bean
    //алгоритм шифрования пароля
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
