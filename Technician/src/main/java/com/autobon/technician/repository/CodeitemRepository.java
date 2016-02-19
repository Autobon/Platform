package com.autobon.technician.repository;

import com.autobon.technician.entity.Codeitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/17.
 */
@Repository
public interface CodeitemRepository extends JpaRepository<Codeitem, Integer> {

    @Query("select item.name from Codeitem item where item.codemap = ?1")
    List<String> getByCodemap(String codemap);
}
