package me.whiteship.demoinfleanrestapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 외부 요청이 리소스에 접근을 할 때 여기서 인증을 한다.
 * 인증이 필요할 때 OAuth 서버에서 제공하는 토큰을 받고 유효성을 확인
 * 토큰을 발급받는 과정은 클라이언트가 해야할 일
 *
 * 인증 정보에 토큰 기반으로 인증 정보가 있는지 확인하고
 * 접근 제한을 한다
 *
 * 이벤트 리소스를 제공하는 서버와 같이 있는 게 맞고
 * 인증 서버는 따로 쓰는 게 맞다.
 *
 * 리소스의 id같은 걸 설정하는 서버
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig implements ResourceServerConfigurer {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()// 익명 유저를 허용하고
                .and()
                .authorizeRequests()// 해당 요청에는
                    .mvcMatchers(HttpMethod.GET, "/api/**") // 이와 일치하는 요청에는
                        .permitAll()
                // .anonymous() // 익명의 유저만 허용한다. 인증하면 사용할 수 없게 됨.
                    .anyRequest()// 그 밖의 다른 요청들은
                        .authenticated() // 인증을 필요로 한다
                .and()
                .exceptionHandling()// 인증이 잘못됐거나 권한이 없는 경우
                .accessDeniedHandler(new OAuth2AccessDeniedHandler()) // 위의 경우 접근 권한이 없을떄 이 핸들러를 사용
        ;
    }
}
