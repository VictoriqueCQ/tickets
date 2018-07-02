package cn.tickets.repository;

import cn.tickets.entity.ConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<ConsumptionEntity, Integer> {
    List<ConsumptionEntity> findByMid(int mid);

    List<ConsumptionEntity> findByMidAndPredefine(int mid, int predefine);

    List<ConsumptionEntity> findByVid(int vid);

    List<ConsumptionEntity> findByVidAndPredefine(int vid, int predefine);

    @Modifying
    @Query(value = "update ConsumptionEntity c set c.predefine = :predefine where c.cid = :cid")
    void updatePredefine(@Param("predefine") int predefine, @Param("cid") int cid);

    List<ConsumptionEntity> findByIsboughtAndPredefine(int isbougnt, int predefine);

    //过去一个月某会员消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and mid=:mid and date_sub(curdate(), interval 1 month) <= date(orderdate)")
    List<ConsumptionEntity> consumptionsLastMonth(@Param("mid") int mid);

    //过去一年某会员消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and mid=:mid and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> consumptionsLastYear(@Param("mid") int mid);

    //过去一年某场馆消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and vid=:vid and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> venueConsumptionLastYear(@Param("vid") int vid);

    //过去某场馆某类型的消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and vid=:vid and type=:type and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> venueTypeConsumptionLastYear(@Param("vid") int vid, @Param("type") String type);

    //过去某类型消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and type=:type and date_sub(curdate(), interval 12 month) <= date(orderdate)")
    List<ConsumptionEntity> typeConsumptionLastYear(@Param("type") String type);

    //过去3个月所有消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and date(orderdate)>=date_sub(curdate(), interval 3 month)")
    List<ConsumptionEntity> allConsumptionLastQuarter();
    //过去3-6个月所有消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and date(orderdate)>=date_sub(curdate(), interval 6 month) and date(orderdate)<=date_sub(curdate(), interval 3 month)")
    List<ConsumptionEntity> allConsumptionLastSecondQuarter();
    //过去6-9个月所有消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and date(orderdate)>=date_sub(curdate(), interval 9 month) and date(orderdate)<=date_sub(curdate(), interval 6 month)")
    List<ConsumptionEntity> allConsumptionLastThirdQuarter();
    //过去9-12个月所有消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and date(orderdate)>=date_sub(curdate(), interval 12 month) and date(orderdate)<=date_sub(curdate(), interval 9 month)")
    List<ConsumptionEntity> allConsumptionLastFourthQuarter();


    //过去一年所有完成的消费记录
    @Query(nativeQuery = true, value = "select * from consumption where predefine=2 and date(orderdate)>=date_sub(curdate(), interval 12 month)")
    List<ConsumptionEntity> allConsumptionLastYear();

    //过去一年完成的消费记录
    @Query(nativeQuery = true, value = "select * from consumption where date(orderdate)>=date_sub(curdate(), interval 12 month)")
    List<ConsumptionEntity> consumpLastYear();
}
