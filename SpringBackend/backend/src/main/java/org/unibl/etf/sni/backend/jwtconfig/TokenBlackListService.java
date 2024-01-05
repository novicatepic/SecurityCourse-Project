package org.unibl.etf.sni.backend.jwtconfig;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TokenBlackListService {

    private List<String> blacklistedTokens = new ArrayList<>();

    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

}
