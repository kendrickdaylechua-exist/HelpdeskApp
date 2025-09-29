package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.dto.account.AccountListRequest;
import com.exist.HelpdeskApp.dto.account.AccountMapper;
import com.exist.HelpdeskApp.dto.account.AccountRequest;
import com.exist.HelpdeskApp.dto.account.AccountResponse;
import com.exist.HelpdeskApp.exception.businessexceptions.EmployeeNotFoundException;
import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.Employee;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.repository.EmployeeRepository;
import com.exist.HelpdeskApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              AccountMapper accountMapper,
                              PasswordEncoder passwordEncoder,
                              EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Page<AccountResponse> getAccounts(AccountListRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("desc") ? Sort.by(request.getSortBy()).descending() : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountResponse> accountResponses = accountMapper.toResponseList(accountPage.getContent());
        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }

    @Transactional
    public AccountResponse updateAccount(String username, AccountRequest request) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new EmployeeNotFoundException("Account id not found"));
        if (request.getEmployeeId() != null) {
            Employee employee =  employeeRepository.findByIdAndDeletedFalse(request.getEmployeeId()).orElseThrow(
                    () -> new EmployeeNotFoundException("Employee with ID " + request.getEmployeeId() + " not found!")
            );
            account.setEmployee(employee);
        }
        accountMapper.toUpdate(request, account);
        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }

    @Transactional
    public AccountResponse updateStatus(String username, boolean enabled) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                () -> new EmployeeNotFoundException("Account not found!")
        );
        account.setEnabled(enabled);
        Account updated = accountRepository.save(account);
        return accountMapper.toResponse(updated);
    }
}
