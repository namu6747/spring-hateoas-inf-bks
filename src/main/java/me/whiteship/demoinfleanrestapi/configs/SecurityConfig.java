package me.whiteship.demoinfleanrestapi.configs;

import lombok.RequiredArgsConstructor;
import me.whiteship.demoinfleanrestapi.accounts.AccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");
        // 시큐리티가 적용되지 않도록
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/docs/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
//        ;
//
        http.anonymous() // 난 비회원도 허용한다.
                .and()
                .formLogin() // 폼 로그인 페이지도 허용한다.
                .and()
                .authorizeRequests() // 특별히 요청을 허락할 페이지가 있다.
                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated() // GET 요청의 해당 경로를 허락한다.
                .anyRequest().authenticated() // 나머지는 인증이 필요하다
        ;
    }

}




