package com.autobon.merchandiser.repository;

import com.autobon.merchandiser.entity.Merchandiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wh on 2018/6/1.
 */
@Repository
public interface MerchandiserRepository  extends JpaRepository<Merchandiser, Integer> {

    Merchandiser findByPhone(String phone);

    Merchandiser findByPushId(String pushId);
}
