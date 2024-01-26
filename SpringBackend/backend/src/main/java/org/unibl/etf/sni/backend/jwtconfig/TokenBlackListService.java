package org.unibl.etf.sni.backend.jwtconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.blacklist.BlackListToken;
import org.unibl.etf.sni.backend.blacklist.BlackListTokenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenBlackListService {

    private List<String> blacklistedTokens = new ArrayList<>();

    @Autowired
    private BlackListTokenRepository repository;

    public void addToBlacklist(String token) {
        //blacklistedTokens.add(token);
        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setToken(token);
        repository.save(blackListToken);
    }

    public boolean isTokenBlacklisted(String token) {
        Optional<BlackListToken> blackListToken = repository.findByToken(token);
        return blackListToken.isPresent();
    }

}
