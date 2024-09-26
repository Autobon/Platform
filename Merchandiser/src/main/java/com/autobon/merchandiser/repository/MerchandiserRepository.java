package com.autobon.merchandiser.repository;

import com.autobon.merchandiser.entity.Merchandiser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2018/6/1.
 */
@Repository
public interface MerchandiserRepository  extends JpaRepository<Merchandiser, Integer> {

    Merchandiser findByPhone(String phone);

    Merchandiser findByPushId(String pushId);


    @Query(value = "select m.* from t_merchandiser m left join t_merchandiser_cooperator mc on mc.merchandiser_id = m.id where mc.cooperator_id = ?1 and (?1 is null or m.phone like ?1) and (?2 is null or m.name like ?2)", nativeQuery = true)
    List<Merchandiser> find(int cooperatorId, String phone, String name);



    @Query("select m from Merchandiser m where (?1 is null or m.phone like ?1) and (?2 is null or m.name like ?2)")
    List<Merchandiser> find(String phone, String name);



    @Query("select t from Merchandiser t " +
            "where (?1 is null or t.phone like ?1) " +
            "and (?2 is null or t.name like ?2) " +
            "and (?3 is null or t.statusCode = ?3)")
    Page<Merchandiser> find(String phone, String name, Integer statusCode, Pageable pageable);

}
