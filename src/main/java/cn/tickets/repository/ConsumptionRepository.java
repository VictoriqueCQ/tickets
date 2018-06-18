package cn.tickets.repository;

import cn.tickets.entity.ConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<ConsumptionEntity, Integer>{
    List<ConsumptionEntity> findByMid(int mid);
    List<ConsumptionEntity> findByMidAndPredefine(int mid, int predefine);
    List<ConsumptionEntity> findByVid(int vid);
    List<ConsumptionEntity> findByVidAndPredefine(int vid, int predefine);
    @Modifying
    @Query(value = "update ConsumptionEntity c set c.predefine = :predefine where c.cid = :cid")
    void updatePredefine(@Param("predefine")int predefine, @Param("cid")int cid);

    List<ConsumptionEntity> findByIsboughtAndPredefine(int isbougnt,int predefine);

    //过去一个月某会员消费记录
    @Query(nativeQuery = true, value = "select * from consumption where mid=:mid and date_sub(curdate(), interval 1 month) <= date(orderdate)")
    List<ConsumptionEntity> consumptionsLastMonth(@Param("mid")int mid);

    //过去一年某会员消费记录
    @Query(nativeQuery = true,value = "select * from consumption where mid=:mid and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> consumptionsLastYear(@Param("mid")int mid);

    //过去一年某场馆消费记录
    @Query(nativeQuery = true,value = "select * from consumption where vid=:vid and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> venueConsumptionLastYear(@Param("vid")int vid);
}
