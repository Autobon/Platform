package com.autobon.message.service;

import com.autobon.message.entity.Message;
import com.autobon.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by liz on 2016/4/2.
 */

@Service
public class MessageService {
    @Autowired
    MessageRepository repository;

    /**
     * 根据ID查找单个通知
     *
     * @param id
     * @return
     */
    public Message getById(int id) {
        return repository.findOne(id);
    }

    /**
     * 保存一条通知
     *
     * @param msg
     * @return
     */
    public Message save(Message msg) {
        return repository.save(msg);
    }


    /**
     * 删除一条通知
     *
     * @param id
     * @return
     */
    public void delete(int id) {
        repository.delete(id);
    }


    /**
     * 分页查找通知
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<Message> find(Integer sendTo, Integer status, int page, int pageSize) {
        return repository.find(sendTo, status,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

}
