package com.autobon.merchandiser.service;

import com.autobon.merchandiser.entity.Merchandiser;
import com.autobon.merchandiser.repository.MerchandiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wh on 2018/6/1.
 */

@Service
public class MerchandiserService {

    @Autowired
    MerchandiserRepository repository;

    public Merchandiser get(int id) {
        return repository.findOne(id);
    }


    public Merchandiser findByPhone(String phone){

        return repository.findByPhone(phone);
    }

    public void save(Merchandiser merchandiser){

        repository.save(merchandiser);
    }

    public Merchandiser findByPushId(String pushId){

        return repository.findByPushId(pushId);
    }

}
