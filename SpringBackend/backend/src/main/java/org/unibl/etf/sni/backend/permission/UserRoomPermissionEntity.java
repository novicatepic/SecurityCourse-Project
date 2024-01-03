package org.unibl.etf.sni.backend.permission;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.unibl.etf.sni.backend.room.RoomModel;
import org.unibl.etf.sni.backend.user.UserModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
@Table(name = "user_permission_room", schema = "sni_project", catalog = "")
@IdClass(UserRoomPermissionEntityPK.class)
public class UserRoomPermissionEntity {
    @NotNull(message = "userId for user_permission_room is mandatory!")
    @Max(value = 1000000, message = "userId value for code must be less than or equal to 1000000")
    @Min(value = 1, message = "userId value for code must be greater than or equal to 1!")
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull(message = "roomId for user_permission_room is mandatory!")
    @Max(value = 1000000, message = "roomId value for code must be less than or equal to 1000000")
    @Min(value = 1, message = "roomId value for code must be greater than or equal to 1!")
    @Id
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @NotNull(message = "canCreate for user_permission_room is mandatory!")
    @Basic
    @Column(name = "can_create", nullable = false)
    private Boolean canCreate;

    @NotNull(message = "canUpdate for user_permission_room is mandatory!")
    @Basic
    @Column(name = "can_update", nullable = false)
    private Boolean canUpdate;

    @NotNull(message = "canDelete for user_permission_room is mandatory!")
    @Basic
    @Column(name = "can_delete", nullable = false)
    private Boolean canDelete;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "room_id", nullable = false, updatable = false, insertable = false)
    private RoomModel room;

}
