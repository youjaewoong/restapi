package com.example.demo.accounts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

	@Autowired
	AccountService accountService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Test
	public void findByUsername() {
		// Given
		String password = "gos1004";
		String username = "gos1004@nate.com";
		Account account = Account.builder()
				.email(username)
				.password(password)
				.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
				.build();
		this.accountRepository.save(account);
		
		// When
		UserDetailsService userDetailsService = accountService;
		UserDetails usrDetails =  userDetailsService.loadUserByUsername(username);
		
		// Then
		assertThat(usrDetails.getPassword()).isEqualTo(password);
		
	}
    
}
