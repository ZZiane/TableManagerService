package org.zzach.tmservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final static String REALM_ACCESS = "realm_access";
    private final static String RESOURCE_ACCESS = "resource_access";
    private final static String ROLES = "roles";

    public AbstractAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        Collection<String> roles = extractRoles(claims);
        Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<String> extractRoles(Map<String, Object> claims) {
        Collection<String> realmRoles = getRolesFromClaim(REALM_ACCESS, claims);
        Collection<String> resourceRoles = getRolesFromClaim(RESOURCE_ACCESS, claims);
        return Stream.concat(realmRoles.stream(), resourceRoles.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private Collection<String> getRolesFromClaim(String claimName, Map<String, Object> claims) {
        Map<String, Object> claim = (Map<String, Object>) claims.get(claimName);
        if (claim != null && claim.containsKey(ROLES)) {
            return (Collection<String>) claim.get(ROLES);
        }
        return Collections.emptyList();
    }


}
