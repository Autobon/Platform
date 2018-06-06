package com.autobon.technician.service;


import com.autobon.technician.entity.Team;
import com.autobon.technician.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tian on 18/6/5.
 */
@Service
public class TeamService {
    @Autowired
    private TeamRepository repository;

    public Team get(int id) {
        return repository.findOne(id);
    }

    public Page<Team> find(String name, String managerName, int managerId, int page, int pageSize) {
        if(name != null){
            name = "%"+name+"%";
        }

        if(managerName != null){
            managerName = "%"+managerName+"%";
        }

        return repository.find(name, managerName, managerId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Team save(Team team){
        return repository.save(team);
    }

    public void deleteTeam(Integer id){
        repository.delete(id);
    }
}
