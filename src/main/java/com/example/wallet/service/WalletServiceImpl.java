package com.example.wallet.service;

import com.example.wallet.dto.AccountDto;
import com.example.wallet.dto.AccountOperationRequestDto;
import com.example.wallet.exception.LowBalanceException;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.mapper.AccountMapper;
import com.example.wallet.model.Account;
import com.example.wallet.model.OperationType;
import com.example.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final AccountMapper mapper;

    @Override
    @Transactional
    public AccountDto processOperation(AccountOperationRequestDto request) {
        log.info("Operation with wallet {}", request);
        Account existAccount = getAccountIfExistOrElseThrow(request.getWalletId());
        if (OperationType.WITHDRAW.equals(request.getOperationType())) {
            return withdrawOperation(request.getAmount(), existAccount);
        } else {
            return depositOperation(request.getAmount(), existAccount);
        }

    }

    private AccountDto withdrawOperation(BigDecimal amount, Account existAccount) {
        if (existAccount.getBalance().compareTo(amount) < 0) {
            String errorMessage = String.format("Wallet balance %s is less than the requested amount %s", existAccount.getBalance(), amount);
            throw new LowBalanceException(errorMessage);
        }
        existAccount.setBalance(existAccount.getBalance().subtract(amount));
        return mapper.accountToDto(walletRepository.save(existAccount));
    }

    private AccountDto depositOperation(BigDecimal amount, Account existAccount) {
        existAccount.setBalance(existAccount.getBalance().add(amount));
        return mapper.accountToDto(walletRepository.save(existAccount));
    }

    @Override
    @Transactional
    public AccountDto getBalance(UUID walletUUID) {
        log.info("Get balance for wallet with id {}", walletUUID);
        Account accountByUUID = getAccountIfExistOrElseThrow(walletUUID);
        return mapper.accountToDto(accountByUUID);
    }

    @Override
    public AccountDto createWallet() {
        Account newAccount = new Account();
        Account created = walletRepository.save(newAccount);
        return mapper.accountToDto(created);
    }

    private Account getAccountIfExistOrElseThrow(UUID walletUUID) {
        return walletRepository.findById(walletUUID)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с id " + walletUUID + " не существует"));
    }
}
