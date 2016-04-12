package com.autobon.message.repository;

import com.autobon.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by liz on 2016/4/2.
 */

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {

    @Query("from Message m where (?1 is null or m.sendTo = ?1) " +
            "and (?2 is null or m.status = ?2)")
    Page<Message> find(Integer sendTo, Integer status, Pageable pageable);
}
