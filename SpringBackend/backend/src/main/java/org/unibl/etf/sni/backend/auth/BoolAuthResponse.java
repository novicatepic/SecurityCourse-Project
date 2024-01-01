package org.unibl.etf.sni.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoolAuthResponse {

    private boolean success;
    private Integer userId;

}
