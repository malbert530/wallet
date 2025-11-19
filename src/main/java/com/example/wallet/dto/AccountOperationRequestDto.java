package com.example.wallet.dto;

import com.example.wallet.model.OperationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountOperationRequestDto {
    @NotNull(message = "Wallet ID must not be null")
    private UUID walletId;

    @NotNull(message = "Operation type must not be null")
    private OperationType operationType;

    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;
}
