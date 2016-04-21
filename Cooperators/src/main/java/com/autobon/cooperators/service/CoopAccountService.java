package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.repository.CoopAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yuh on 2016/3/23.
 */
@Service
public class CoopAccountService {

    @Autowired
    private CoopAccountRepository coopAccountRepository;

    public List<CoopAccount> findCoopAccountByCooperatorId(int coopId) {
        return coopAccountRepository.findCoopAccountByCooperatorIdOrderByFiredAsc(coopId);
    }

    public CoopAccount getById(int coopAccountId) {
        return coopAccountRepository.findOne(coopAccountId);
    }

    public void save(CoopAccount coopAccount) {
        coopAccountRepository.save(coopAccount);
    }

    public CoopAccount getByShortname(String shortname) {
        return  coopAccountRepository.getByShortname(shortname);
    }

    public CoopAccount getByPhone(String phone) {
        return coopAccountRepository.getByPhone(phone);
    }

    public CoopAccount getByCooperatorIdAndIsMain(int coopId, boolean b) {
        return coopAccountRepository.getByCooperatorIdAndIsMain(coopId, b);
    }

    public CoopAccount getByPushId(String pushId) {
        return coopAccountRepository.getByPushId(pushId);
    }

    @Transactional
    public int batchUpdateShortname(int cooperatorId, String shortname) {
        return coopAccountRepository.batchUpdateShortname(cooperatorId, shortname);
    }
}
