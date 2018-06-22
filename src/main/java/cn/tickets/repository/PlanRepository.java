package cn.tickets.repository;

import cn.tickets.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Integer> {
    void deleteByPid(int pid);

    List<PlanEntity> findByVid(int vid);

    PlanEntity findByDateAndVid(Timestamp date, int vid);

    //过去一年所有场馆发布的计划"select * from consumption where mid=:mid and date_sub(curdate(), interval 1 month) <= date(orderdate)"
    @Query(nativeQuery = true,value = "select * from plan where type=:type and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<PlanEntity> allPlansLastYear(@Param("type")String type);
    //查询过去一年某场馆发布的计划
    @Query(nativeQuery = true,value = "select * fron plan where vid=:vid and type=:type and data_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<PlanEntity> plansLastYear(@Param("vid")int vid,@Param("type")String type);
}
