package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCharacterServiceImplTest {

    @Mock
    EntryToEntryPojo entryToEntryPojo;
    @InjectMocks
    AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
    @Mock
    EntryRepository entryRepository;
    @Mock
    AccountCharacterRepository accountCharacterRepository;

    AccountCharacterServiceImpl accountCharacterService;

    Account account;

    List<AccountCharacter> accountCharacters;

    @BeforeEach
    void setUp() {
        accountCharacterService = new AccountCharacterServiceImpl(accountCharacterRepository,
                null,
                accountCharacterToAccountCharacterPojo);

        account = new Account();
        account.setId(1l);

        accountCharacters = new ArrayList<>();
        AccountCharacter character1 = new AccountCharacter();
        character1.setId(1l);
        character1.setAccount(account);
        character1.setCharacterName("test1");
        character1.setGarrisonResources(100l);
        character1.setWarPaint(200l);
        AccountCharacter character2 = new AccountCharacter();
        character2.setId(2l);
        character2.setAccount(account);
        character2.setCharacterName("test2");
        character2.setGarrisonResources(200l);
        character2.setWarPaint(100l);
        AccountCharacter character3 = new AccountCharacter();
        character3.setId(3l);
        character3.setAccount(account);
        character3.setCharacterName("test3");
        character3.setGarrisonResources(0l);
        character3.setWarPaint(0l);
        accountCharacters.addAll(Arrays.asList(character1, character2, character3));
        account.getAccountCharacters().addAll(new HashSet<>(Arrays.asList(character1,character2,character3)));
    }


    @Test
    void getListOfAccountCharactersConvertedToPojo_correctSizeAndData() {

        //given

        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(accountCharacters);

        //then
        List<AccountCharacterPojo> accountCharacterPojoList = accountCharacterService.getListOfAccountCharactersConvertedToPojo(0l);
        assertEquals(accountCharacters.size(), accountCharacterPojoList.size());
        assertEquals("test1", accountCharacterPojoList.get(0).getCharacterName());
        assertEquals("test2", accountCharacterPojoList.get(1).getCharacterName());
    }

    @Test
    void isNameTake() {

        //given
        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(accountCharacters);

        //then
        assertTrue(accountCharacterService.isNameTaken(Mockito.anyLong(), "test1"));
        assertFalse(accountCharacterService.isNameTaken(Mockito.anyLong(), "does not exist"));
    }

    @Test
    void findById() {

        //when
        long firstId = 1l;
        long secondId = 2l;
        long thirdId = 5l;
        when(accountCharacterRepository.findById(firstId)).thenReturn(Optional.ofNullable(accountCharacters.get(0)));
        when(accountCharacterRepository.findById(secondId)).thenReturn(Optional.ofNullable(accountCharacters.get(1)));
        when(accountCharacterRepository.findById(thirdId)).thenReturn(Optional.empty());

        //then
        assertTrue(accountCharacterService.findById(firstId)!=null);
        assertTrue(accountCharacterService.findById(secondId)!=null);
        assertFalse(accountCharacterService.findById(thirdId)!=null);
    }

    @Test
    void accountGetTotalGarrisonResources(){

        assertEquals(300l, account.getTotalGarrisonResources());
        assertNotEquals(400l, account.getTotalGarrisonResources());

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setGarrisonResources(50l);
        accountCharacter.setAccount(account);
        account.getAccountCharacters().add(accountCharacter);

        assertEquals(350l, account.getTotalGarrisonResources());
        assertNotEquals(300l, account.getTotalGarrisonResources());
    }
    @Test
    void accountGetWarPaint(){

        assertEquals(300l, account.getTotalWarPaint());
        assertNotEquals(400l, account.getTotalWarPaint());

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setWarPaint(50l);
        accountCharacter.setAccount(account);
        account.getAccountCharacters().add(accountCharacter);

        assertEquals(350l, account.getTotalWarPaint());
        assertNotEquals(300l, account.getTotalWarPaint());
    }
}