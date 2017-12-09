package com.obabichev.todotree.rest;

import com.obabichev.todotree.domain.User;
import com.obabichev.todotree.security.TokenManager;
import com.obabichev.todotree.security.TokenPayload;
import com.obabichev.todotree.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.RollbackException;
import java.util.Calendar;
import java.util.Date;

@RestController
public class UserRest {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "tokenManager")
    private TokenManager tokenManager;

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user) {

        try {
            User createdUser = userService.create(user);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (RollbackException e) {
            return new ResponseEntity<>((User) null, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = "/authentication", method = RequestMethod.POST)
    public ResponseEntity<String> authentication(@RequestBody User user) {
        User authenticationUser = userService.findUserByLoginAndPassword(user.getLogin(), user.getPassword());

        if (authenticationUser == null) {
            return new ResponseEntity<>((String) null, HttpStatus.BAD_REQUEST);
        }

        String token = createToken(authenticationUser);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    private String createToken(User user) {
        Date createdTime = Calendar.getInstance().getTime();
        TokenPayload payload = new TokenPayload(user.getId(), user.getLogin(), createdTime);

        String token = tokenManager.createToken(payload);

        return token;
    }
}
