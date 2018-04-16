package cn.tickets.service.impl;

import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.SeatEntity;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.SeatRepository;
import cn.tickets.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class SeatServiceImpl implements SeatService {
    private SeatRepository seatRepository;
    private PlanRepository planRepository;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository, PlanRepository planRepository) {
        this.seatRepository = seatRepository;
        this.planRepository = planRepository;
    }

    @Override
    public List<SeatEntity> getSeat(int pid, int isbooked) {
        return seatRepository.findByPidAndIsbooked(pid, 0);
    }

    @Override
    public void setSeatOrdered(int cid, int pid, List<Integer> frontSeatList, List<Integer> backSeatList) {
        for (int i = 0; i < frontSeatList.size(); i++) {
            SeatEntity seatEntity = seatRepository.findByPidAndIsfrontAndSeatnumberAndIsbooked(pid, 1, frontSeatList.get(i), 0);
            seatEntity.setCid(cid);
            seatEntity.setIsbooked(1);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            seatEntity.setOrderdate(timestamp);
            seatRepository.save(seatEntity);

        }
        for (int i = 0; i < backSeatList.size(); i++) {
            SeatEntity seatEntity = seatRepository.findByPidAndIsfrontAndSeatnumberAndIsbooked(pid, 0, backSeatList.get(i), 0);
            seatEntity.setCid(cid);
            seatEntity.setIsbooked(1);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            seatEntity.setOrderdate(timestamp);
            seatRepository.save(seatEntity);
        }
    }

    @Override
    public boolean assignSeat(int cid, int pid, int fnumber, int bnumber) {
         boolean flag = false;
        List<SeatEntity> frontSeatEntities = seatRepository.findByPidAndIsfrontAndIsbooked(pid, 1, 0);
        List<SeatEntity> backSeatEntities = seatRepository.findByPidAndIsfrontAndIsbooked(pid,0,0);
        if(frontSeatEntities.size()>=fnumber&&backSeatEntities.size()>=bnumber){//如果配票成功
            for (int i = 0; i < fnumber; i++) {
                SeatEntity seatEntity = frontSeatEntities.get(i);
                seatEntity.setCid(cid);
                seatEntity.setIsbooked(1);
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                seatEntity.setOrderdate(timestamp);
            }
            seatRepository.save(frontSeatEntities);


            for(int i = 0;i<bnumber;i++){
                SeatEntity seatEntity = backSeatEntities.get(i);
                seatEntity.setCid(cid);
                seatEntity.setIsbooked(1);
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                seatEntity.setOrderdate(timestamp);
            }
            seatRepository.save(backSeatEntities);
            flag = true;
        }
        return flag;
    }
}
