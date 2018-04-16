package cn.tickets;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.SeatEntity;
import cn.tickets.repository.ConsumptionRepository;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.SeatRepository;
import cn.tickets.service.ConsumptionService;
import cn.tickets.service.MemberService;
import cn.tickets.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableScheduling
@SpringBootApplication
//@EnableJpaRepositories(repositoryBaseClass = CustomPayRecordRepositoryImpl.class)
public class Application {
    //TODO add Spring security
    // Not allow not active person or venue to access those statistic page

    private static ConsumptionRepository consumptionRepository;
    private static MemberService memberService;
    private static SeatService seatService;
    private static PlanRepository planRepository;
    private static SeatRepository seatRepository;

    @Autowired
    public Application(MemberService memberService, ConsumptionRepository consumptionRepository, SeatService seatService, PlanRepository planRepository, SeatRepository seatRepository) {
        this.consumptionRepository = consumptionRepository;
        this.memberService = memberService;
        this.seatService = seatService;
        this.planRepository = planRepository;
        this.seatRepository = seatRepository;
    }

    public static void deleteSeat() {
        List<PlanEntity> planEntities = planRepository.findAll();
        List<Integer> planIdList = new ArrayList<>();
        for (PlanEntity planEntity : planEntities) {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            if (timestamp.getTime() - planEntity.getDate().getTime() >= 0) {
                planIdList.add(planEntity.getPid());
            }
        }
        for (Integer pid : planIdList) {
//            seatRepository.deleteByPid(pid);
            List<SeatEntity> seatEntities = seatRepository.findByPid(pid);
//            for(SeatEntity seatEntity : seatEntities){
            seatRepository.delete(seatEntities);
//            }
        }
    }


    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        while (true) {
            //3分钟内没有付款的票要退订
//            try {
//                Thread.sleep(60000);//一分钟执行一次
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            List<ConsumptionEntity> consumptionEntities = consumptionRepository.findByIsboughtAndPredefine(0, 1);
            for (ConsumptionEntity consumptionEntity : consumptionEntities) {
                if (consumptionEntity.getIsbought() == 0) {
                    Timestamp timestamp = consumptionEntity.getOrderdate();
                    Date date = new Date();
                    Timestamp timestampNow = new Timestamp(date.getTime());
                    long deltaTime = (timestampNow.getTime() - timestamp.getTime()) / (1000 * 60);//毫秒转成分钟
                    System.err.println("DeltaTime:" + deltaTime + "分钟");
                    if (deltaTime >= 2) {
                        memberService.unSubscribe(consumptionEntity.getCid());
                    }
                }
            }

            //给直接购买的分配座位
            List<ConsumptionEntity> consumptionEntities_2 = consumptionRepository.findByIsboughtAndPredefine(1, 1);
            for (ConsumptionEntity consumptionEntity : consumptionEntities_2) {
                boolean flag = false;
                int cid = consumptionEntity.getCid();
                int fnumber = consumptionEntity.getFsnumber();
                int bnumber = consumptionEntity.getBsnumber();
                int vid = consumptionEntity.getVid();
                Timestamp timestamp = consumptionEntity.getDate();
                Date date = new Date();
                Timestamp timestampNow = new Timestamp(date.getTime());
                PlanEntity planEntity = planRepository.findByDateAndVid(timestamp, vid);
                int pid = planEntity.getPid();
                long deltaTime = (timestamp.getTime() - timestampNow.getTime()) / (1000 * 60 * 60 * 24 * 14);//开始2周前配票
                if (deltaTime <= 2) {
                    flag = seatService.assignSeat(cid, pid, fnumber, bnumber);
                    if (!flag) {
                        memberService.unSubscribe(cid);
                    }
                }

            }


            deleteSeat();

        }

    }


}