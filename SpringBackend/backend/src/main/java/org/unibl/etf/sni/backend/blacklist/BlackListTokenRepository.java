package org.unibl.etf.sni.backend.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Integer> {

    Optional<BlackListToken> findByToken(String token);

}
