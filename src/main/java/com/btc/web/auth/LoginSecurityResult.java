package com.btc.web.auth;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by Eric on 1/8/15.
 */
@Data
@ToString
public class LoginSecurityResult {
    private String login;
    private String label;
    private boolean success;
    private int active;
    private String message;
    private List<String> roles;
}
