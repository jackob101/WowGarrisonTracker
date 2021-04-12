package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Options;

public interface OptionsService {


    boolean updateOptions(Options options);

    boolean updateOptions(Options options, Long accountId) throws AccountNotFoundException;

    void save(Options options);
}