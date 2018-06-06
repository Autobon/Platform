package com.autobon.technician.repository;

import com.autobon.technician.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by tian on 18/6/5.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    Team getById(int id);

    @Query("select t from Team t " +
            "where (?1 is null or t.name like ?1) " +
            "and (?2 is null or t.manager_name like ?2) " +
            "and (?3 is null or t.manager_id = ?3)")
    Page<Team> find(String name, String managerName, Integer managerId, Pageable pageable);

}
