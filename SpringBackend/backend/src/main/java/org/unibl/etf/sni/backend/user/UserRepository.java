package org.unibl.etf.sni.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.sni.backend.user.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByUsernameAndPassword(String username, String password);

    Optional<UserModel> findByUsername(String username);

}
