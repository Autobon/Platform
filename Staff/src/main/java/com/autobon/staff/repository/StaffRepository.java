package com.autobon.staff.repository;

import com.autobon.staff.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by liz on 2016/2/18.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff findByUsername(String username);

    Staff findByPhone(String phone);

    Staff findByEmail(String email);

    Page<Staff> findByName(String name, Pageable pageable);

    @Modifying
    @Query("update Staff s set s.password = ?1 where s.phone = ?2")
    void update(String newPassword,String phone);

}
