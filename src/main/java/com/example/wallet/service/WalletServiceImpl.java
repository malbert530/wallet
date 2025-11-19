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

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final AccountMapper mapper;

    @Override
    @Transactional
    public void processOperation(AccountOperationRequestDto request) {
        log.info("Operation with wallet {}", request);
        Account existAccount = getAccountIfExistOrElseThrow(request.getWalletId());
        if (OperationType.WITHDRAW.equals(request.getOperationType())) {
            if (existAccount.getBalance().compareTo(request.getAmount()) < 0) {
                String errorMessage = String.format("Wallet balance %s is less than the requested amount %s", existAccount.getBalance(), request.getAmount());
                throw new LowBalanceException(errorMessage);
            }
            existAccount.setBalance(existAccount.getBalance().subtract(request.getAmount()));
            walletRepository.save(existAccount);
        } else {
            existAccount.setBalance(existAccount.getBalance().add(request.getAmount()));
            walletRepository.save(existAccount);
        }

    }

    @Override
    @Transactional
    public AccountDto getBalance(UUID walletUUID) {
        log.info("Get balance for wallet with id {}", walletUUID);
        Account accountByUUID = getAccountIfExistOrElseThrow(walletUUID);
        return mapper.accountToDto(accountByUUID);
    }

    @Override
    public UUID createWallet() {
        Account newAccount = new Account();
        Account created = walletRepository.save(newAccount);
        return created.getId();
    }

    private Account getAccountIfExistOrElseThrow(UUID walletUUID) {
        return walletRepository.findById(walletUUID)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек с id " + walletUUID + " не существует"));
    }
}
