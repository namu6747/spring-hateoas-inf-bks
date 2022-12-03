package me.whiteship.demoinfleanrestapi.accounts;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static me.whiteship.demoinfleanrestapi.accounts.AccountRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        String username = "keesun@email.com";
        String password = "keesun";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(ADMIN,USER))
                .build();
        this.accountService.saveAccount(account);
        UserDetails userDetails = accountService.loadUserByUsername(username);

        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameEmailWithTryCatch(){
        String username = "random@email.com";
        try{
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e){
                assertThat(e instanceof UsernameNotFoundException).isTrue();
                assertThat(e.getMessage()).contains(username);
        }
    }

    @Test
    public void findByUsernameEmailWithExpectedException(){
        String username = "random@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        accountService.loadUserByUsername(username);
    }
}
