package cn.tickets.repository;

import cn.tickets.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Integer> {
    void deleteByPid(int pid);

    List<PlanEntity> findByVid(int vid);

    PlanEntity findByDateAndVid(Timestamp date, int vid);

}
