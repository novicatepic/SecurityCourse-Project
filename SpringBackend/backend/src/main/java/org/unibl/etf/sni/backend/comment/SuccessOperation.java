package org.unibl.etf.sni.backend.comment;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class SuccessOperation {

    private Boolean result;
    private String message;

}
