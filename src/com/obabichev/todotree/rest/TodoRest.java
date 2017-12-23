package com.obabichev.todotree.rest;

import com.obabichev.todotree.rest.utils.AuthenticationUtils;
import com.obabichev.todotree.service.TodoService;
import com.obabichev.todotree.service.domain.PlainTodo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TodoRest {

    @Resource(name = "todoService")
    private TodoService todoService;

    @Resource(name = "authenticationUtils")
    private AuthenticationUtils authenticationUtils;

    @RequestMapping(path = "/todo", method = RequestMethod.POST)
    public ResponseEntity<PlainTodo> create(HttpServletRequest request, @RequestBody PlainTodo plainTodo) {
        return authenticationUtils.peformAfterAuthentication(request, userId -> {
            PlainTodo result = todoService.create(userId, plainTodo);

            return new ResponseEntity<>(result, HttpStatus.OK);
        });
    }
}
