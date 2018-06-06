package com.autobon.technician.service;


import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.TechnicianRepository;
import com.autobon.technician.vo.TechnicianLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by dave on 16/2/13.
 */
@Service
public class TechnicianService {
    @Autowired
    private TechnicianRepository repository;

    public Technician get(int id) {
        return repository.findOne(id);
    }

    public Technician getByPhone(String phone) {
        return repository.getByPhone(phone);
    }

    public Technician save(Technician technician) {
        return repository.save(technician);
    }

    public void deleteById(int id) {
        repository.delete(id);
    }

    public Page<Technician> findAll(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Page<Technician> findByName(String name, int page, int pageSize) {
        return repository.findByName(name, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public Technician getByPushId(String pushId) {
        return repository.getByPushId(pushId);
    }

    public Page<Technician> findActivedFrom(Date date, int page, int pageSize) {
        return repository.findActivedFrom(date, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "lastLoginAt")));
    }

    public List<Integer> find(String query){

        query = "%"+query+"%";

        return repository.find(query);

    }

    public Page<Technician> find(String phone, String name, Technician.Status status, int page, int pageSize) {
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

    public int countOfNew(Date from, Date to) {
        return repository.countOfNew(from, to);
    }

    public int countOfVerified(Date from, Date to) {
        return repository.countOfVerified(from, to);
    }

    public int totalOfRegistered() {
        return repository.totalOfRegistered();
    }

    public int totalOfVerified() {
        return repository.totalOfVerified();
    }


    public Page<Technician> find(String phone ,String name, Integer currentPage, Integer pageSize){

        Pageable pageable = new PageRequest(currentPage-1, pageSize);
        Page<Technician> technicians = repository.find(phone, name , pageable);
        return technicians;
    }

    public Page<Technician> findTech(String phone ,String name,Integer techId, Integer currentPage, Integer pageSize){

        Pageable pageable = new PageRequest(currentPage-1, pageSize);
        Page<Technician> technicians = repository.findTech(phone, name , techId, pageable);
        return technicians;
    }



    public TechnicianLocation getById(int techId){
        List<Object[]> list = repository.getByTechId(techId);
        if(list!=null&&list.size()>0){
            TechnicianLocation technicianLocation = new TechnicianLocation(list.get(0), 0);
            return technicianLocation;
        }
        return  null;
    }


    public List<Technician> findAll(){

        return repository.findAll();
    }

    public Technician saveRemark(int techId, String remark){
        Technician technician = repository.getById(techId);

        technician.setRemark(remark == null ? technician.getRemark() : remark);
        return technician;
    }

}
