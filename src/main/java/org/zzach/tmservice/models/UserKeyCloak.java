package org.zzach.tmservice.models;

import lombok.Data;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

@Data
public class UserKeyCloak {

    private String email;
    private String fullName;
    private String entityType;
    private boolean emailVerified;
    public UserKeyCloak(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        this.entityType = (String) claims.get("entity-type");
        this.email = (String) claims.get("email");
        this.fullName = (String) claims.get("name");
        this.emailVerified = (boolean) claims.get("email_verified");

    }
}
