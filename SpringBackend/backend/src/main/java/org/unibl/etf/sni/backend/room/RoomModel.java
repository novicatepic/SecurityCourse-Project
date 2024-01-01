package org.unibl.etf.sni.backend.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.sni.backend.comment.CommentModel;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room", schema = "sni_project", catalog = "")
public class RoomModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false, length = 45, unique = true)
    private String name;

    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL)
    private List<CommentModel> comments = new ArrayList<>();
}
