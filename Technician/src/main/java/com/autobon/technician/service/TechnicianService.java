package com.autobon.technician.service;

import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.CodeitemRepository;
import com.autobon.technician.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dave on 16/2/13.
 */
@Service
public class TechnicianService {
    @Autowired
    private TechnicianRepository repository;

    @Autowired
    private CodeitemRepository codeitemRepository;

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

    public Page<Technician> getPage(int page, int pageSize) {
        return repository.findAll(new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "createAt")));
    }

    public HashMap<String, Object> Authenticate(String account, String password) {
        Technician technician = null;
        HashMap<String, Object> ret = new HashMap<>();
        if (Pattern.matches("^\\d{11}$", account)) {
            technician = repository.getByPhone(account);
        }
        ret.put("technician", technician);
        if (technician == null) {
            ret.put("accountValid", false);
            ret.put("passwordValid", false);
            ret.put("result", false);
        } else if (!technician.getPassword().equals(Technician.encryptPassword(password))) {
            ret.put("accountValid", true);
            ret.put("passwordValid", false);
            ret.put("result", false);
        } else {
            ret.put("accountValid", true);
            ret.put("passwordValid", true);
            ret.put("result", true);
        }
        return ret;
    }

    public List<String> getWorkByCodemap(String codemap){
        List<String> list = codeitemRepository.getByCodemap(codemap);
        return list;
    }

}
