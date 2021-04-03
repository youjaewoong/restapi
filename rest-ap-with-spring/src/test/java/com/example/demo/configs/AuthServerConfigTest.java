package com.example.demo.configs;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.accounts.AccountService;
import com.example.demo.common.AppProperties;
import com.example.demo.common.BaseControllerTest;
import com.example.demo.common.TestDescription;

class AuthServerConfigTest extends BaseControllerTest {

	@Autowired
	AccountService accountService;
	
	@Autowired
	AppProperties appProperties;
	
	@Test
	@TestDescription("인증 토큰을 발급 받는 테스트")
	public void getAuthToken() throws Exception {
		
		//Given
		/* run 때 이미 셋팅하기 떄문에 불필요
		Account gos1004 = Account.builder()
				.email(appProperties.getUserUsername())
				.password(appProperties.getUserPassword())
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
		this.accountService.saveAccount(gos1004);
		*/
		
		this.mockMvc.perform(post("/oauth/token")
			.with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
			.param("username", appProperties.getUserUsername())
			.param("password", appProperties.getUserPassword())
			.param("grant_type", "password"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("access_token").exists());
	}
}
