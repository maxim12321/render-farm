package com.mkohan.render.repositories;

import com.mkohan.render.entities.Task;
import com.mkohan.render.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Transactional(readOnly = true)
    @Query("select t from Task t " +
            "where t.submitter = :submitter " +
            "order by t.submittedAt desc ")
    List<Task> findBySubmitter(User submitter);
}
