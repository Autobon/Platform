package com.autobon.merchandiser.repository;

import com.autobon.merchandiser.entity.MerchandiserCooperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wh on 2018/6/5.
 */
@Repository
public interface MerchandiserCooperatorRepository extends JpaRepository<MerchandiserCooperator, Integer> {

    @Query(value = "select mc.id, mc.merchandiser_id, mc.cooperator_id, c.fullname from t_merchandiser_cooperator mc left join t_cooperators c on c.id = mc.cooperator_id where mc.merchandiser_id = ?1", nativeQuery = true)
    List<Object[]> find(int merchandiserId);
}
