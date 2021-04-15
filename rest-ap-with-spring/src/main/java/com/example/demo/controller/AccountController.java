package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.events.Event;
import com.example.demo.service.AccountService;

@Controller
@RequestMapping(value= "/api/accounts", produces = MediaTypes.HAL_JSON_VALUE)
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	public ResponseEntity<?> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
		
		
		return ResponseEntity.ok(accountService.getAccount());
	}
}
