package com.autobon.merchandiser.service;

import com.autobon.merchandiser.entity.Merchandiser;
import com.autobon.merchandiser.repository.MerchandiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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


    public void delete(int id){

        repository.delete(id);
    }

    public Page<Merchandiser> find(String phone, String name, Merchandiser.Status status, int page, int pageSize){

        if(phone != null){
            phone = "%"+phone+"%";
        }

        if(name != null){
            name = "%"+name+"%";
        }


        Integer statusCode = status == null ? null : status.getStatusCode();
        return repository.find(phone, name, statusCode, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

}
