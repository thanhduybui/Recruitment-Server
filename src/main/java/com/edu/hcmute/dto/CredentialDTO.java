package com.edu.hcmute.dto;

import lombok.Data;

@Data
public class CredentialDTO {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private String email_verified;
    private String nbf;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String iat;
    private String exp;
    private String jti;
}
