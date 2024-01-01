package org.unibl.etf.sni.backend.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoomPermissionRepository extends JpaRepository<UserRoomPermissionEntity, UserRoomPermissionEntityPK> {
}
