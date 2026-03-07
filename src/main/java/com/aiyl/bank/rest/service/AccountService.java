package com.aiyl.bank.rest.service;

import com.aiyl.bank.rest.dto.response.PaginationResponse;
import com.aiyl.bank.rest.dto.response.StatementDto;

import java.time.LocalDate;

public interface AccountService {
    PaginationResponse<StatementDto> getStatement(String accountNumber, LocalDate from, LocalDate to, int pageNumber, int pageSize);
}
