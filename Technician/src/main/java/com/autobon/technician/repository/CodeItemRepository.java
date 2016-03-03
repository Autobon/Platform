package com.autobon.technician.repository;

import com.autobon.technician.entity.CodeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/2/17.
 */
@Repository
public interface CodeItemRepository extends JpaRepository<CodeItem, Integer> {

    List<CodeItem> findByCodemap(String codemap);
}
