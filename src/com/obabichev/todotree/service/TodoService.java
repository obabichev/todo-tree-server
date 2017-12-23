package com.obabichev.todotree.service;

import com.obabichev.todotree.domain.Tag;
import com.obabichev.todotree.domain.Todo;
import com.obabichev.todotree.domain.User;
import com.obabichev.todotree.service.domain.PlainTag;
import com.obabichev.todotree.service.domain.PlainTodo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("todoService")
public class TodoService {

    @Resource(name = "entityManagerFactory")
    private EntityManagerFactory entityManagerFactory;

    public PlainTodo create(Long userId, PlainTodo plainTodo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Todo todo = create(entityManager, userId, plainTodo);

            entityManager.getTransaction().commit();

            return convert(todo);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private PlainTodo convert(Todo todo) {
        PlainTodo plainTodo = new PlainTodo();

        plainTodo.setId(todo.getId());
        plainTodo.setComment(todo.getComment());
        plainTodo.setEndDate((Date) todo.getEndDate().clone());
        plainTodo.setStartDate((Date) todo.getStartDate().clone());
        plainTodo.setImportant(todo.getImportant());
        plainTodo.setName(todo.getName());
        plainTodo.setPriority(todo.getPriority());
        plainTodo.setWeight(todo.getWeight());

        plainTodo.setUserId(todo.getUser().getId());
        plainTodo.setTags(todo.getTags().stream().map(this::convert).collect(Collectors.toSet()));

        return plainTodo;
    }

    private PlainTag convert(Tag tag) {
        PlainTag plainTag = new PlainTag();

        plainTag.setId(tag.getId());
        plainTag.setName(tag.getName());
        plainTag.setTodoIds(tag.getTodoList().stream().map(Todo::getId).collect(Collectors.toSet()));
        return plainTag;
    }

    private Todo create(EntityManager entityManager, Long userId, PlainTodo plainTodo) {
        Todo todo = new Todo();

        todo.setComment(plainTodo.getComment());
        todo.setEndDate((Date) plainTodo.getEndDate().clone());
        todo.setStartDate((Date) plainTodo.getStartDate().clone());
        todo.setImportant(plainTodo.getImportant());
        todo.setName(plainTodo.getName());
        todo.setPriority(plainTodo.getPriority());
        todo.setWeight(plainTodo.getWeight());

        User user = entityManager.find(User.class, userId);
        todo.setUser(user);

        entityManager.persist(todo);

        findOrCreateTag(plainTodo, entityManager).forEach(todo::addTag);

        return todo;
    }

    private List<Tag> findOrCreateTag(PlainTodo plainTodo, EntityManager entityManager) {

        return plainTodo.getTags().stream()
                .map(plainTag -> {
                    List<Tag> tags = entityManager
                            .createQuery("SELECT tag FROM Tag tag WHERE tag.name = :name", Tag.class)
                            .setParameter("name", plainTag.getName())
                            .getResultList();
                    if (tags.size() == 1) {
                        return tags.get(0);
                    } else {
                        Tag tag = new Tag(plainTag.getName());
                        entityManager.persist(tag);
                        return tag;
                    }
                })
                .collect(Collectors.toList());
    }


}
