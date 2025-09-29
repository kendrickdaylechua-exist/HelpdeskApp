package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountMapper;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Transactional
    public Page<AccountResponse> getAccounts(AccountListRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountResponse> accountResponses = accountMapper.toResponseList(accountPage.getContent());
        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

//    @Transactional
//    public AccountResponse createAccount(AccountRequest request) {
//
//    }
}
