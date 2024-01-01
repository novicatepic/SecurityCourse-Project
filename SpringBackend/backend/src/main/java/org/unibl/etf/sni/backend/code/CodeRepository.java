package org.unibl.etf.sni.backend.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.sni.backend.user.UserModel;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {

    Optional<Code> findByUserId(Integer userId);

}
