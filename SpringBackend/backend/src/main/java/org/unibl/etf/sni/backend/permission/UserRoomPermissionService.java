package org.unibl.etf.sni.backend.permission;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.room.RoomModel;
import org.unibl.etf.sni.backend.room.RoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoomPermissionService {

    @Autowired
    private UserRoomPermissionRepository repository;

    @Autowired
    private RoomService roomService;


    public UserRoomPermissionEntity insert(UserRoomPermissionEntity entity) {
        return repository.save(entity);
    }


    public List<UserRoomPermissionEntity> getSetPermissions(Integer userId) {
        /*List<UserRoomPermissionEntity> entities = repository.findAll();

        List<RoomModel> allRooms = roomService.getAllRooms();

        Set<Integer> roomIdsFromEntities = entities.stream()
                .map(UserRoomPermissionEntity::getRoomId)
                .collect(Collectors.toSet());

        return  allRooms.stream()
                .filter(room -> roomIdsFromEntities.contains(room.getId()))
                .collect(Collectors.toList());*/

        return repository.findByUserId(userId);

    }

    public UserRoomPermissionEntity getPermissionForRoomAndUser(Integer userId, Integer roomId) throws NotFoundException {
        return repository.findByUserIdAndRoomId(userId, roomId).orElseThrow(NotFoundException::new);
    }

    public List<RoomModel> getUnsetPermissions(Integer userId) {
        List<UserRoomPermissionEntity> userRoomPermissions = repository.findByUserId(userId);
        List<RoomModel> allRooms = roomService.getAllRooms();

        // Extract room IDs from the userRoomPermissions list
        Set<Integer> roomIdsWithPermission = userRoomPermissions.stream()
                .map(UserRoomPermissionEntity::getRoomId)
                .collect(Collectors.toSet());

        // Filter rooms that don't have an ID in the set
        List<RoomModel> unsetPermissions = allRooms.stream()
                .filter(room -> !roomIdsWithPermission.contains(room.getId()))
                .collect(Collectors.toList());

        return unsetPermissions;

    }

}
