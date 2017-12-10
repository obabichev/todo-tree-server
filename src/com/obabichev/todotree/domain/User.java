package com.obabichev.todotree.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOGIN", unique = true, nullable = false)
    private String login;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Todo> todoList = new HashSet<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Todo> getTodoList() {
        return todoList;
    }

    public void addTodo(Todo todo) {
        addTodo(todo, false);
    }

    void addTodo(Todo todo, boolean otherSideHasAlreadySet) {
        getTodoList().add(todo);
        if (otherSideHasAlreadySet) {
            return;
        }
        todo.setUser(this, true);
    }
}
