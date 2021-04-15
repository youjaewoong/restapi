package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AccountDto;
import com.github.pagehelper.Page;

@Repository
@Mapper
public interface AccountMapper {
	public Page<AccountDto> getAccount();
}
