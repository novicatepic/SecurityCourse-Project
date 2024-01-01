package org.unibl.etf.sni.backend.code;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
@Entity
@Table(name = "code", schema = "sni_project", catalog = "")
public class Code {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "code", nullable = false, length = 5)
    private String code;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Integer userId;



}
