package org.unibl.etf.sni.backend.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoomPermissionService {

    @Autowired
    private UserRoomPermissionRepository repository;

    public UserRoomPermissionEntity insert(UserRoomPermissionEntity entity) {
        return repository.save(entity);
    }

}
