package org.unibl.etf.sni.backend.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log", schema = "sni_project", catalog = "")
public class LogModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "info", nullable = false, length = 3000, unique = true)
    private String info;

    public LogModel(String info) {this.info = info; }

}
