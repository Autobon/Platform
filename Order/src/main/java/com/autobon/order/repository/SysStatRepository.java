package com.autobon.order.repository;
import com.autobon.order.entity.SysStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * Created by lu on 2016/3/21.
 */
@Repository
public interface SysStatRepository extends JpaRepository<SysStat, Integer> {
      //统计某日
      @Query("SELECT s from SysStat s where s.statTime=?1 and s.statType='day'")
      SysStat getOfDay(Date day);

     //统计某个日期段内
     @Query("SELECT s from SysStat s where s.statTime >=?1 and s.statTime<?2 and s.statType='day'")
     List<SysStat> findBetweenOfDay(Date start,Date end);

     //统计某个月份段内
     @Query("SELECT s from SysStat s where s.statTime >=?1 and s.statTime<?2 and s.statType='month'")
     List<SysStat> findBetweenOfMonth(Date start,Date end);

     //统计某月
     @Query("SELECT s from SysStat s where s.statTime=?1 and s.statType='month'")
     SysStat getOfMonth(Date month);

}
