package com.autobon.staff.repository;

import com.autobon.staff.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liz on 2016/2/18.
 */
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff findByUsername(String username);

    Staff findByPhone(String phone);

    Staff findByEmail(String email);

    Page<Staff> findByName(String name, Pageable pageable);

}
