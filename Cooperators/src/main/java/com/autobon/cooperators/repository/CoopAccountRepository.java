package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.CoopAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/3/23.
 */
@Repository
public interface CoopAccountRepository extends JpaRepository<CoopAccount, Integer> {

    List<CoopAccount> findCoopAccountByCooperatorIdOrderByFiredAsc(int coopId);

    CoopAccount getByShortname(String shortname);

    CoopAccount getByPhone(String phone);

    CoopAccount getByCooperatorIdAndIsMain(int coopId, boolean b);

    CoopAccount getByPushId(String pushId);

    @Modifying
    @Query("update CoopAccount a set a.shortname = ?2 where a.cooperatorId = ?1")
    int batchUpdateShortname(int cooperatorId, String shortname);
}
