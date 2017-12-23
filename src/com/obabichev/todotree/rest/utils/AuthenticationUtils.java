package com.obabichev.todotree.rest.utils;

import com.obabichev.todotree.security.TokenManager;
import com.obabichev.todotree.security.TokenPayload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component("authenticationUtils")
public class AuthenticationUtils {
    @Resource(name = "tokenManager")
    private TokenManager tokenManager;

    public <R> ResponseEntity<R> peformAfterAuthentication(HttpServletRequest request,
                                                           Function<Long, ResponseEntity<R>> function) {
        String token = request.getHeader("Authentication");

        if (!tokenManager.verifyToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenPayload tokenPayload = tokenManager.extractPayload(token);
        return function.apply(tokenPayload.getUserId());
    }
}
