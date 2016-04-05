package com.autobon.message.repository;

import com.autobon.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by liz on 2016/4/2.
 */

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>{

}
