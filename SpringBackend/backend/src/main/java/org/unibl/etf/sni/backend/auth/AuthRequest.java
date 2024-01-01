package org.unibl.etf.sni.backend.auth;


public class AuthRequest {

    private String username;
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
