package com.example.wallet.mapper;

import com.example.wallet.dto.AccountDto;
import com.example.wallet.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto accountToDto(Account account);
}
