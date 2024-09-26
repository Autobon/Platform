package com.autobon.staff.repository;

import com.autobon.staff.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    Menu findByName(String Name);
}
