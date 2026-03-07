package com.aiyl.bank.rest.controller;

import com.aiyl.bank.rest.dto.response.PaginationResponse;
import com.aiyl.bank.rest.dto.response.StatementDto;
import com.aiyl.bank.rest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<PaginationResponse<StatementDto>> getStatement(
            @PathVariable String accountNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "16") int pageSize) {
        if (pageSize > 100) {
            pageSize = 100;
        }
   PaginationResponse<StatementDto> response =
           accountService.getStatement(accountNumber,from,to,pageNumber,pageSize);
        return ResponseEntity.ok(response);
    }

}
