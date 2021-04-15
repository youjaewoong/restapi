package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AccountDto;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountMapper accountMapper;
	
	@Override
	public List<AccountDto> getAccount() {
		return accountMapper.getAccount();
	}

}
