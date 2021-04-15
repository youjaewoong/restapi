package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.AccountDto;

@Repository
@Mapper
public interface AccountMapper {
	public List<AccountDto> getAccount();
}
