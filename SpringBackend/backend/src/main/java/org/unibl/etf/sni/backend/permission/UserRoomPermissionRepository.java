package org.unibl.etf.sni.backend.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomPermissionRepository extends JpaRepository<UserRoomPermissionEntity, UserRoomPermissionEntityPK> {

    List<UserRoomPermissionEntity> findByUserId(Integer userId);

    Optional<UserRoomPermissionEntity> findByUserIdAndRoomId(Integer userId, Integer programId);

}
