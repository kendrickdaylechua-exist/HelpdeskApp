package com.exist.HelpdeskApp.service.Implementations;

import com.exist.HelpdeskApp.model.Account;
import com.exist.HelpdeskApp.model.UserPrincipal;
import com.exist.HelpdeskApp.repository.AccountRepository;
import com.exist.HelpdeskApp.service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        if (account ==null) {
            System.out.println("Account 404");
            throw new UsernameNotFoundException("Account 404");
        }
        return new UserPrincipal(account);
    }
}
