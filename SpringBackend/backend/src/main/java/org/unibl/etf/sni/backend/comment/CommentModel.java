package org.unibl.etf.sni.backend.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.sni.backend.user.UserModel;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment", schema = "sni_project", catalog = "")
public class CommentModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Basic
    @Column(name = "content", nullable = false, length = 45)
    private String content;

    @Basic
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserModel writer;

    @Basic
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Basic
    @Column(name = "forbidden", nullable = false)
    private Boolean forbidden;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

}
