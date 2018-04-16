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
}
