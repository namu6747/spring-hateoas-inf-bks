package me.whiteship.demoinfleanrestapi.configs;

import me.whiteship.demoinfleanrestapi.accounts.Account;
import me.whiteship.demoinfleanrestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static me.whiteship.demoinfleanrestapi.accounts.AccountRole.ADMIN;
import static me.whiteship.demoinfleanrestapi.accounts.AccountRole.USER;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                String username = "keesun@email.com";
                String password = "keesun";
                Account account = Account.builder()
                        .email(username)
                        .password(password)
                        .roles(Set.of(ADMIN,USER))
                        .build();
                accountService.saveAccount(account);
            }
        };
    }*/
}
