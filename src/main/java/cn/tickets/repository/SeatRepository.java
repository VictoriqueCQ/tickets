package cn.tickets.repository;

import cn.tickets.entity.SeatEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatRepository extends CrudRepository<SeatEntity, Integer> {

    List<SeatEntity> findByCid(int cid);

    List<SeatEntity> findByPidAndIsbooked(int pid,int isbooked);

    SeatEntity findByPidAndIsfrontAndSeatnumberAndIsbooked(int pid,int isfront,int seatnumber,int isbooked);

    List<SeatEntity> findByPidAndIsfrontAndIsbooked(int pid,int isfront,int isbooked);

    List<SeatEntity> findByPid(int pid);

//    void deleteByPid(int pid);
}
