package com.autobon.study.repository;

import com.autobon.study.entity.StudyData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by Administrator on 16/3/11.
 */
@Repository
public interface StudyDataRepository extends JpaRepository<StudyData, Integer> {

    @Query("select s from StudyData s " +
            "where (?1 is null or s.fileName like ?1) " +
            "and (?2 is null or s.type = ?2)")
    Page<StudyData> find(String fileName, Integer type, Pageable p);

    StudyData findByFileName(String fileName);

}
