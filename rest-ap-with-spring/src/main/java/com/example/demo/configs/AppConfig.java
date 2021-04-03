package com.example.demo.configs;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.AppProperties;

@Configuration
public class AppConfig {
	
	@Bean
	public ModelMapper modeMapper() {
		return new ModelMapper();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    //characterEncodingFilter.setEncoding("UTF-8");
	    characterEncodingFilter.setForceEncoding(true);
	    return characterEncodingFilter;
	}
	
	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {

			@Autowired
			AccountService accountService;
			
			@Autowired
			AppProperties appProperties;
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account admin = Account.builder()
						.email(appProperties.getAdminUsername())
						.password(appProperties.getAdminPassword())
						.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
						.build();
				accountService.saveAccount(admin);
				
				Account user = Account.builder()
						.email(appProperties.getUserUsername())
						.password(appProperties.getUserPassword())
						.roles(Set.of(AccountRole.USER))
						.build();
				accountService.saveAccount(user);
			}
		};
	}
}