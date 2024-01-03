package org.unibl.etf.sni.backend.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "title for comment is mandatory!")
    @Size(max = 45, message = "Maximum character size for title is 45!")
    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @NotBlank(message = "content for comment is mandatory!")
    @Size(max = 2000, message = "Maximum character size for content is 2000!")
    @Basic
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @NotNull(message = "roomId for comment is mandatory!")
    @Max(value = 1000000, message = "roomId value for code must be less than or equal to 1000000")
    @Min(value = 1, message = "roomId value for code must be greater than or equal to 1!")
    @Basic
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @NotNull(message = "userId for comment is mandatory!")
    @Max(value = 1000000, message = "userId value for code must be less than or equal to 1000000")
    @Min(value = 1, message = "userId value for code must be greater than or equal to 1!")
    @Basic
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserModel writer;

    @NotNull(message = "dateCreated for comment is mandatory!")
    @Basic
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull(message = "dateCreated for comment is mandatory!")
    @Basic
    @Column(name = "forbidden", nullable = false)
    private Boolean forbidden;

    @NotNull(message = "dateCreated for comment is mandatory!")
    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

}
