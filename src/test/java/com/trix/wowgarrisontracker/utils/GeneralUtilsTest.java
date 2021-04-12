package com.trix.wowgarrisontracker.utils;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.pojos.OptionsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class GeneralUtilsTest {

    GeneralUtils generalUtils;

    CustomUserDetails customUserDetails;

    Account account;
    Options options;
    Server server;

    @BeforeEach
    void setUp() {
        generalUtils = new GeneralUtils();
        account = new Account();
        options = new Options();
        server = new Server();

        account.setId(1L);
        account.setOptions(options);

        options.setId(1L);
        options.setAccount(account);
        options.setServer(server);

        this.customUserDetails = new CustomUserDetails(account);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customUserDetails,customUserDetails.getPassword(),new ArrayList<>()));

    }

    @Test
    void getUserSettings() {
        //given
        options.getServer().setName("test1");

        //when
        OptionsDTO converted = generalUtils.getUserSettings();

        //then
        assertEquals(options.getServer().getName(), converted.getServer().getName());
        assertEquals(options.getId(), converted.getId());
        assertEquals(options.isReceiveEmailNotifications(), converted.isReceiveEmailNotifications());
        assertEquals(options.getAccount().getId(), converted.getAccountId());
    }
}