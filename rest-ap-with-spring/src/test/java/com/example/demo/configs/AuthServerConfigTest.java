package com.example.demo.configs;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.BaseControllerTest;
import com.example.demo.common.TestDescription;

class AuthServerConfigTest extends BaseControllerTest {

	@Autowired
	AccountService accountService;
	
	@Test
	@TestDescription("인증 토큰을 발급 받는 테스트")
	public void getAuthToken() throws Exception {
		
		//Given
		String username = "keeun@email.com";
		String password = "keesun";
		Account gos1004 = Account.builder()
				.email(username)
				.password(password)
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
		this.accountService.saveAccount(gos1004);
		
		String clientId = "myApp";
		String clientSecret = "pass";
		
		this.mockMvc.perform(post("/oauth/token")
			.with(httpBasic(clientId, clientSecret))
			.param("username", username)
			.param("password", password)
			.param("grant_type", "password"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("access_token").exists());
	}
}
