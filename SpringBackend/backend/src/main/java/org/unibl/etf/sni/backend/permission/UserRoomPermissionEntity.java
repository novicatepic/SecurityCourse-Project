package org.unibl.etf.sni.backend.permission;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Id
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Basic
    @Column(name = "can_create", nullable = false)
    private Boolean canCreate;


    @Basic
    @Column(name = "can_update", nullable = false)
    private Boolean canUpdate;

    @Basic
    @Column(name = "can_delete", nullable = false)
    private Boolean canDelete;

}
