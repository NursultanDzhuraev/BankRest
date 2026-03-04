package com.aiyl.bank.rest.controller;

import com.aiyl.bank.rest.dto.response.TransferResponse;
import com.aiyl.bank.rest.dto.request.TransferRequest;
import com.aiyl.bank.rest.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Создавать переводы между счетами")
    public ResponseEntity<TransferResponse> transferBetweenAccounts(@Valid @RequestBody TransferRequest transferRequest){
      TransferResponse response = transactionService.transferBetweenAccounts(transferRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
