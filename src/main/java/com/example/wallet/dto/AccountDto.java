package com.example.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private BigDecimal balance;
}
