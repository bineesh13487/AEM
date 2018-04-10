package com.mcd.akamai.core.flush;

import java.util.ArrayList;
import java.util.List;

public class AkamaiFlushRequest {

    private String user;
    private String password;
    private final List<String> arls = new ArrayList<>();
    private String domain;
    private String action;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getArls() {
        return arls;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
