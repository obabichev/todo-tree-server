package com.obabichev.todotree;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRest {

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity<String> create() {
        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }


}
