package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.AccountDto;
import com.example.demo.events.Event;
import com.example.demo.service.AccountService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value= "/api/accounts", produces = MediaTypes.HAL_JSON_VALUE)
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
		//PageHelper.startPage(1,10);
		Page<AccountDto> account = accountService.getAccount();
		Page<AccountDto> page = PageHelper.startPage(2, 10).doSelectPage(() -> accountService.getAccount());
		PageInfo<AccountDto> pageInfo = PageHelper.startPage(2, 10).doSelectPageInfo(() -> accountService.getAccount());
		
		return ResponseEntity.ok(pageInfo);
	}
}
