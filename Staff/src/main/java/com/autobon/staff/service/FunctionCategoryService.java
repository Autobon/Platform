package com.autobon.staff.service;

import com.autobon.staff.entity.FunctionCategory;
import com.autobon.staff.repository.FunctionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wh on 2018/6/25.
 */
@Service
public class FunctionCategoryService {


    @Autowired
    FunctionCategoryRepository functionCategoryRepository;


    public List<FunctionCategory> findByMenuId(int meunuId){

        return functionCategoryRepository.findByMenuId(meunuId);

    }

}
