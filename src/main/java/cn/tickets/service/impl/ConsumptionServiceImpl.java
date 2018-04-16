package cn.tickets.service.impl;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.ConsumptionRepository;
import cn.tickets.repository.MemberRepository;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.ConsumptionService;
import cn.tickets.util.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class ConsumptionServiceImpl implements ConsumptionService {
    @Autowired
    private ConsumptionRepository consumptionRepository;
    private MemberRepository memberRepository;
    private PlanRepository planRepository;
    private VenueRepository venueRepository;


    public ConsumptionServiceImpl(ConsumptionRepository consumptionRepository, MemberRepository memberRepository, PlanRepository planRepository, VenueRepository venueRepository) {
        this.consumptionRepository = consumptionRepository;
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.venueRepository = venueRepository;
    }


    @Override
    public boolean updatePredefine(int predefine, int cid) {
        consumptionRepository.updatePredefine(predefine, cid);
        return true;
    }

    @Override
    public Map<String, Object> buy(int id, int cid, int ap, int isbought) {
        Map<String, Object> result = new TreeMap<>();
        ConsumptionEntity consumptionEntity = consumptionRepository.findOne(cid);
        MemberEntity memberEntity = memberRepository.findOne(id);
        if (consumptionEntity.getIsbought() == isbought) {
            //已付款，无需再付款
            result.put("number", 0);
            result.put(Default.HTTP_RESULT, true);
            System.err.println("consumption service result:" + result);
            return result;
        } else {
            if (memberEntity.getMoney() < ap) {
                //余额不足
                result.put("number", 2);
                result.put(Default.HTTP_RESULT, true);
                System.err.println("consumption service result:" + result);
                return result;
            } else {
                //付款
                memberEntity.setMoney(memberEntity.getMoney() - ap);
                memberEntity.setPoint(memberEntity.getPoint() + ap);
                int point = memberEntity.getPoint();
                int level = point / 2000;
                memberEntity.setLevel(level);
                consumptionEntity.setIsbought(1);
                consumptionRepository.save(consumptionEntity);
                memberRepository.save(memberEntity);
                result.put("number", 1);
                result.put(Default.HTTP_RESULT, true);
                System.err.println("consumption service result:" + result);
                return result;
            }
        }
    }

    @Override
    public int orderNow(int id, int pid, int fnumber, int bnumber) {
        PlanEntity planEntity = planRepository.findOne(pid);
        MemberEntity memberEntity = memberRepository.findOne(id);
        double discount = ((double) memberEntity.getDiscount() / 100.0) * memberEntity.getLevel();
        ConsumptionEntity consumptionEntity = new ConsumptionEntity();
        consumptionEntity.setMid(id);
        consumptionEntity.setVid(planEntity.getVid());
        consumptionEntity.setDate(planEntity.getDate());
        consumptionEntity.setFsnumber(fnumber);
        consumptionEntity.setBsnumber(bnumber);

        int fprice = (int) (planEntity.getFprice() * (1 - discount));
        consumptionEntity.setFprice(fprice);

        int bprice = (int) (planEntity.getBprice() * (1 - discount));
        consumptionEntity.setBprice(bprice);

        int aprice = fnumber * fprice + bnumber * bprice;
        consumptionEntity.setAprice(aprice);
        consumptionEntity.setPredefine(1);
        consumptionEntity.setType(planEntity.getType());

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        consumptionEntity.setOrderdate(timestamp);
        consumptionEntity.setIsbought(0);
        consumptionRepository.save(consumptionEntity);
        VenueEntity venueEntity = venueRepository.findOne(planEntity.getVid());

        venueRepository.save(venueEntity);
        System.err.println("<——————————————————————————————————————————————————————————————————————————————————————————————————>");
        System.err.println("cid:" + consumptionEntity.getCid());
        return consumptionEntity.getCid();
    }

    /**
     *
     * @param vid
     * @param mid
     * @param pid
     * @param fsnumber
     * @param bsnumber
     * @return
     */
    @Override
    public int sceneBuy(int vid, int mid, int pid, int fsnumber, int bsnumber) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        double discount;
        int aprice = 0;
        PlanEntity planEntity = planRepository.findOne(pid);
        int fprice = planEntity.getFprice();
        int bprice = planEntity.getBprice();
        String type = planEntity.getType();
        Timestamp planTimestamp = planEntity.getDate();
        if (mid != 0) {
            MemberEntity memberEntity = memberRepository.findOne(mid);
            discount = ((double) memberEntity.getDiscount() / 100.0) * memberEntity.getLevel();
            fprice = (int) (fprice * (1 - discount));
            bprice = (int) (bprice * (1 - discount));
            aprice = fprice * fsnumber + bprice * bsnumber;
            memberEntity.setPoint(memberEntity.getPoint()+aprice);
            int point = memberEntity.getPoint();
            int level = point / 2000;
            memberEntity.setLevel(level);
            memberRepository.save(memberEntity);
        } else {
            aprice = fprice * fsnumber + bprice * bsnumber;
        }
        ConsumptionEntity consumptionEntity = new ConsumptionEntity(vid, mid, planTimestamp, fsnumber, bsnumber, fprice, bprice, aprice, 2, type, timestamp, 1);
        consumptionRepository.save(consumptionEntity);
        return aprice;
    }

    @Override
    public boolean check(int cid) {
        ConsumptionEntity consumptionEntity = consumptionRepository.findOne(cid);
        boolean flag = false;
        if (consumptionEntity != null) {
            flag = true;
            consumptionEntity.setPredefine(2);
            consumptionRepository.save(consumptionEntity);
        }

        return flag;
    }



}
