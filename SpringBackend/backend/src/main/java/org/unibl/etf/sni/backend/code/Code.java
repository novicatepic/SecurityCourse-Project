package org.unibl.etf.sni.backend.code;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "code is mandatory!")
    @Size(max = 5, message = "Maximum character size for code is 5!")
    @Basic
    @Column(name = "code", nullable = false, length = 5)
    private String code;

    @NotNull(message = "userId for code is mandatory!")
    @Max(value = 1000000, message = "userId value for code must be less than or equal to 1000000")
    @Min(value = 1, message = "userId value for code must be greater than or equal to 1!")
    @Basic
    @Column(name = "user_id", nullable = false)
    private Integer userId;

}
