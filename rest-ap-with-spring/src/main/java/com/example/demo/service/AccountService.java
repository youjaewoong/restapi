package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.AccountDto;
import com.github.pagehelper.Page;


public interface AccountService {
	public Page<AccountDto> getAccount();
}
