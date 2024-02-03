package org.unibl.etf.sni.backend.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//username and password for auth request
public class AuthRequest {
    @NotBlank(message = "username for login is mandatory!")
    @Size(max = 45, message = "Maximum character size for username for login is 500!")
    private String username;

    @NotBlank(message = "password for login is mandatory!")
    @Size(max = 500, message = "Maximum character size for password for login is 500!")
    private String password;

    public AuthRequest() {}

    public AuthRequest(String username, String pw) {
        this.username = username;
        this.password = pw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
