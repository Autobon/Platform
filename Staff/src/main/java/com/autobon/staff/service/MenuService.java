package com.autobon.staff.service;

import com.autobon.staff.entity.Menu;
import com.autobon.staff.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> findMenus(){

        return menuRepository.findAll();
    }

}
