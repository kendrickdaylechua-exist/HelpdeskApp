package com.exist.HelpdeskApp.service;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    Page<AccountResponse> getAccounts(AccountListRequest request);
}
