package com.aiyl.bank.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Исходный счет обязателен")
        String sourceAccountNumber,
        @NotBlank(message = "Целевой счет обязателен")
        @Size(max = 16, message = "Номер счета не должен превышать 16 символов")
        String targetAccountNumber,
        @NotNull(message = "Сумма перевода обязательна")
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
        BigDecimal amount
) {
}
