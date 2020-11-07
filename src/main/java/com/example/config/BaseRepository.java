package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class BaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

//    public List<Object> findPaginated(int page, int pageSize) {
//
//        Pageable paging = PageRequest.of(page, pageSize);
//        Page<Object> pagedResult = repository.findAll(paging);
//
//        return pagedResult.toList();
//    }


}
