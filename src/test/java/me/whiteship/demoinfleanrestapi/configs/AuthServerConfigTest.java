package me.whiteship.demoinfleanrestapi.configs;

import lombok.RequiredArgsConstructor;
import me.whiteship.demoinfleanrestapi.accounts.Account;
import me.whiteship.demoinfleanrestapi.accounts.AccountService;
import me.whiteship.demoinfleanrestapi.common.AppProperties;
import me.whiteship.demoinfleanrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static me.whiteship.demoinfleanrestapi.accounts.AccountRole.ADMIN;
import static me.whiteship.demoinfleanrestapi.accounts.AccountRole.USER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;
    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{
          mockMvc.perform(post("/oauth/token")
                  //클라이언트 아이디,패스워드로 basic auth 헤더를 만들었다.
                          .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                          .param("username",appProperties.getUserUsername())
                          .param("password",appProperties.getUserPassword())
                          .param("grant_type","password")
                  )
                  .andDo(print())
                  .andExpect(status().isOk())
                  .andExpect(jsonPath("access_token").exists())
          ;
    }

}
