package com.autobon.technician.service;


import com.autobon.technician.entity.Team;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.TeamRepository;
import com.autobon.technician.repository.TechnicianRepository;
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
    @Autowired
    private TechnicianRepository technicianRepository;


    public Team get(int id) {
        return repository.findOne(id);
    }

    public Team getByName(String name) {
        return repository.findByName(name);
    }

    public Team getByManagerId(Integer managerId) {
        return repository.findByManagerId(managerId);
    }


    public Page<Team> find(String name, Integer managerId, String managerName, String managerPhone, int page, int pageSize) {
        if(name != null){
            name = "%"+name+"%";
        }

        if(managerName != null){
            managerName = "%"+managerName+"%";
        }

        return repository.find(name, managerName, managerId, managerPhone, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Team save(Team team){
        return repository.save(team);
    }

    public void deleteTeam(Integer id){
        repository.delete(id);
    }

    public Page<Technician> findTechByTeam(Integer teamId, int page, int pageSize){
        return technicianRepository.findByTeam(teamId, new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.DESC, "id")));
    }
}
