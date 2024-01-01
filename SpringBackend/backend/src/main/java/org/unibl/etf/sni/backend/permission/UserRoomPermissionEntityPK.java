package org.unibl.etf.sni.backend.permission;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class UserRoomPermissionEntityPK implements Serializable {

    @Column(name = "user_id", nullable = false)
    @Id
    private Integer userId;

    @Column(name = "room_id", nullable = false)
    @Id
    private Integer roomId;

}
