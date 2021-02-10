package com.trix.wowgarrisontracker;

import java.util.Arrays;
import java.util.HashSet;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.ItemEntityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class BootData implements CommandLineRunner {

    private AccountRepository accountRepository;
    private AccountCharacterRepository accountCharacterRepository;
    private EntryRepository entryRepository;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private ItemEntityRepository itemEntityRepository;
    private AuctionEntityRepository auctionEntityRepository;

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
            EntryRepository entryRepository,ItemEntityRepository itemEntityRepository,
            AuctionEntityRepository auctionEntityRepository) {
        this.accountRepository = accountRepository;
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
        this.itemEntityRepository = itemEntityRepository;
        this.auctionEntityRepository = auctionEntityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        logger.info("Creating data");
//        this.innit();
        
    }

    private void innit() {

        Account account1 = new Account();
        account1.setLogin("login1");
        account1.setPassword("password1");
        accountRepository.save(account1);
        logger.info("Account id : " + account1.getId());

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setCharacterName("Calienda");
        accountCharacter.setAccountId(account1.getId());
        accountCharacterRepository.save(accountCharacter);
        Entry entry1 = new Entry();
        entry1.setWarPaint(150);
        entry1.setGarrisonResources(250);
        entry1.setAccountCharacterId(accountCharacter.getId());


        //accountCharacter.setEntries(new HashSet<>(Arrays.asList(entry1)));
        //setAccountCharacters(new HashSet<>(Arrays.asList(accountCharacter)));

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(109119l);
        itemEntity.setName("True Iron Ore");
        
        AuctionEntity entity = new AuctionEntity();
        entity.setId(1l);
        entity.setItemId(109119l);
        entity.setQuantity(500l);
        entity.setUnitPrice(100000l);
        
        
        itemEntityRepository.save(itemEntity);
        
        auctionEntityRepository.save(entity);
        entryRepository.save(entry1);
        
    }

}
