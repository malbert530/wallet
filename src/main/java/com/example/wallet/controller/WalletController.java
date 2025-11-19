package com.example.wallet.controller;

import com.example.wallet.dto.AccountDto;
import com.example.wallet.dto.AccountOperationRequestDto;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/new")
    public UUID createWallet() {
        return walletService.createWallet();
    }

    @PostMapping
    public void performOperation(@Valid @RequestBody AccountOperationRequestDto request) {
        walletService.processOperation(request);
    }

    @GetMapping("/{walletUUID}")
    public AccountDto getWallet(@PathVariable UUID walletUUID) {
        return walletService.getBalance(walletUUID);
    }
}
