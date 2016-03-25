package com.autobon.cooperators.service;

import com.autobon.cooperators.entity.CoopAccount;
import com.autobon.cooperators.repository.CoopAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yuh on 2016/3/23.
 */
@Service
public class CoopAccountService {

    @Autowired
    private CoopAccountRepository coopAccountRepository;

    public List<CoopAccount> findCoopAccountByCooperatorId(int coopId) {
        return coopAccountRepository.findCoopAccountByCooperatorId(coopId);
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
}
