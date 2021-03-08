package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class AccountServicesImpl implements AccountService {

    private AccountRepository accountRepository;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private AccountToAccountPojo accountToAccountPojo;
    private PasswordEncoder passwordEncoder;

    public AccountServicesImpl(AccountRepository accountRepository, AccountToAccountPojo accountToAccountPojo,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountToAccountPojo = accountToAccountPojo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveAccount(Account account) {

        if (!account.getLogin()
                .isEmpty() && this.findUserByUsername(account.getLogin()) == null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
        }

    }

    @Override
    public void deleteAccount(Long id) {

        if (accountRepository.existsById(id))
            accountRepository.deleteById(id);
        else
            logger.info("Nie znaleziono konta");

    }

    @Override
    public List<AccountPojo> findAllAccounts() {
        List<AccountPojo> accountPojos = new ArrayList<>();

        for (Account tmp : accountRepository.findAll()) {
            accountPojos.add(accountToAccountPojo.convert(tmp));
        }
        return accountPojos;
    }

    @Override
    public Account findUserByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByLogin(username);
        return optionalAccount.isPresent() ? optionalAccount.get() : null;

    }

    @Override
    public Account correctCredentials(LoginRequest fromForm) {

        Account account = this.findUserByUsername(fromForm.getLogin());

        if (account != null && account.getPassword()
                .equals(fromForm.getPassword())) {
            return account;
        }
        return null;
    }

    @Override
    public boolean isAccountInDatabase(LoginRequest loginRequest) {
        Account account = this.findUserByUsername(loginRequest.getLogin());
        if (account == null)
            return false;
        return passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());
    }


    @Override
    public boolean areCredentialsTaken(RegisterModel registerModel) {
        Account account = this.findUserByUsername(registerModel.getLogin());
        if (account != null)
            return true;
        return false;
    }


    @Override
    public Account findById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.isPresent() ? optionalAccount.get() : null;
    }




}

