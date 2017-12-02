package com.obabichev.todotree.rest;

import com.obabichev.todotree.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRest {
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
