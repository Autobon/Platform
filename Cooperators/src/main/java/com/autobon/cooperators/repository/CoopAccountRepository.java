package com.autobon.cooperators.repository;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.entity.Cooperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuh on 2016/3/23.
 */
@Repository
public interface CoopAccountRepository extends JpaRepository<CoopAccount, Integer> {

    List<CoopAccount> findCoopAccountByCooperatorId(int coopId);
}
