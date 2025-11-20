package com.example.wallet.service;

import com.example.wallet.dto.AccountDto;
import com.example.wallet.dto.AccountOperationRequestDto;

import java.util.UUID;

public interface WalletService {
    AccountDto processOperation(AccountOperationRequestDto request);

    AccountDto getBalance(UUID walletUUID);

    AccountDto createWallet();
}
