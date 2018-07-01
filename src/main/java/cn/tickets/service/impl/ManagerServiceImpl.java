package cn.tickets.service.impl;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.ConsumptionRepository;
import cn.tickets.repository.MemberRepository;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.ManagerService;
import cn.tickets.util.Default;
import cn.tickets.vo.ManagementAnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {
    private ConsumptionRepository consumptionRepository;
    private VenueRepository venueRepository;
    private MemberRepository memberRepository;
    private PlanRepository planRepository;

    @Autowired
    public ManagerServiceImpl(ConsumptionRepository consumptionRepository, VenueRepository venueRepository, MemberRepository memberRepository, PlanRepository planRepository) {
        this.consumptionRepository = consumptionRepository;
        this.venueRepository = venueRepository;
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
    }


    @Override
    public boolean approve(int vid, int approve) {
        venueRepository.approve(vid, approve);
        return true;
    }


    //会员统计
    @Override
    public Map<String, Object> getMemberStatus() {
        Map<String, Object> result = new TreeMap<>();
        Iterable<MemberEntity> memberEntities = memberRepository.findAll();
        List<String> userId = new ArrayList<>();
        List<Integer> bookSum = new ArrayList<>();
        List<Integer> unsubscribeSum = new ArrayList<>();
        List<Integer> completeSum = new ArrayList<>();
        int index = 0;
        for (MemberEntity memberEntity : memberEntities) {
            index++;
            if (index > 40) {
                break;
            }
            int mid = memberEntity.getId();
            userId.add(String.format("%05d", mid));
            List<ConsumptionEntity> consumptionEntities = consumptionRepository.findByMid(mid);
            int bookNum = 0;
            int unsubscribeNum = 0;
            int completeNum = 0;

            for (ConsumptionEntity consumptionEntity : consumptionEntities) {
                if (consumptionEntity.getPredefine() == 1) {
                    bookNum++;
                } else if (consumptionEntity.getPredefine() == 0) {
                    unsubscribeNum++;
                } else if (consumptionEntity.getPredefine() == 2) {
                    completeNum++;
                }
            }
            bookSum.add(bookNum);
            unsubscribeSum.add(unsubscribeNum);
            completeSum.add(completeNum);
        }
        result.put("userIdList", userId);
        result.put("bookTime", bookSum);
        result.put("unsubscribeTime", unsubscribeSum);
        result.put("completeTime", completeSum);
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    //财务统计
    @Override
    public Map<String, Object> getFinanceStatus() {
        Map<String, Object> result = new TreeMap<>();

        List<Date> dateList = new ArrayList<>();
        List<ConsumptionEntity> consumptionEntities = consumptionRepository.findAll();
        for (ConsumptionEntity consumptionEntity : consumptionEntities) {
            if (!dateList.contains(consumptionEntity.getOrderdate())) {
                dateList.add(consumptionEntity.getOrderdate());
            }
        }
//        System.err.println(dateList);
        List<Integer> bookSum = new ArrayList<>();
        List<Integer> unsubscribeSum = new ArrayList<>();
        List<Integer> completeSum = new ArrayList<>();

        for (Date date : dateList) {
            int bookMoney = 0;
            int unsubscribeMoney = 0;
            int completeMoney = 0;
            for (ConsumptionEntity consumptionEntity : consumptionEntities) {
                if (consumptionEntity.getOrderdate().compareTo(date) == 0) {
                    if (consumptionEntity.getPredefine() == 0) {
                        unsubscribeMoney += consumptionEntity.getAprice();
                    } else if (consumptionEntity.getPredefine() == 1) {
                        bookMoney += consumptionEntity.getAprice();
                    } else if (consumptionEntity.getPredefine() == 2) {
                        completeMoney += consumptionEntity.getAprice();
                    }
                }
            }
            bookSum.add(bookMoney);
            unsubscribeSum.add(unsubscribeMoney);
            completeSum.add(completeMoney);
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateListString = new ArrayList<>();
        for (Date d : dateList) {
            dateListString.add(fmt.format(d));
        }
//        System.out.println(dateListString);
//        System.err.println("bookSum:" + bookSum);
//        System.err.println("unsubscribeSum:" + unsubscribeSum);
//        System.err.println("completeSum:" + completeSum);
        result.put("dateList", dateListString);
        result.put("bookMoney", bookSum);
        result.put("unsubscribeMoney", unsubscribeSum);
        result.put("completeMoney", completeSum);
        result.put(Default.HTTP_RESULT, true);
        System.out.println("result:" + result);
        return result;
    }


    @Override
    public List<VenueEntity> getAllOpenApplication() {
        List<VenueEntity> result = new ArrayList<>(Default.LIST_DEFAULT_SIZE);

        venueRepository.findByApprove(0).forEach(result::add);
        return result;
    }

    @Override
    public List<VenueEntity> getAllModifyApplication() {
        List<VenueEntity> result = new ArrayList<>(Default.LIST_DEFAULT_SIZE);

        List<VenueEntity> venueEntities = venueRepository.findByApprove(1);
        for (VenueEntity venueEntity : venueEntities) {
            if (!venueEntity.getName().equals("name")) {
                result.add(venueEntity);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getSettlement() {

        Map<String, Object> result = new TreeMap<>();
        List<VenueEntity> venueEntities = venueRepository.findAll();
        List<String> venueNameList = new ArrayList<>(venueEntities.size());
        List<Integer> realConsumption = new ArrayList<>(venueEntities.size());
        List<Integer> memberDiscount = new ArrayList<>(venueEntities.size());

        for (VenueEntity venueEntity : venueEntities) {
            int consumptionSum = 0;
            int discountSum = 0;
            int vid = venueEntity.getId();
            List<ConsumptionEntity> consumptionEntities = consumptionRepository.findByVid(vid);
            for (ConsumptionEntity consumptionEntity : consumptionEntities) {
                Timestamp date = consumptionEntity.getDate();
                PlanEntity planEntity = planRepository.findByDateAndVid(date, vid);
                consumptionSum += consumptionEntity.getAprice();
                discountSum += planEntity.getFprice() * consumptionEntity.getFsnumber() + planEntity.getBprice() * consumptionEntity.getBsnumber() - consumptionEntity.getAprice();
            }
            venueNameList.add(venueEntity.getName());
            realConsumption.add(consumptionSum);
            memberDiscount.add(discountSum);
        }
        result.put("venueNameList", venueNameList);
        result.put("realConsumption", realConsumption);
        result.put("memberDiscount", memberDiscount);
        result.put(Default.HTTP_RESULT, true);
        return result;

    }


    //以下是管理信息系统作业新增代码
    @Override
    public ManagementAnalysisVO analysis(Model model) {
        Map<String, Object> result = new TreeMap<>();
        //过去3个月入座率
        String firstSeatingRatio = getSeatingRatio(1);
        //过去3-6个月入座率
        String secondSeatingRatio = getSeatingRatio(2);
        //过去6-9个月入座率
        String thirdSeatingRatio = getSeatingRatio(3);
        //过去9-12个月入座率
        String fourthSeatingRatio = getSeatingRatio(4);
        List<ConsumptionEntity> allConsumLastYear = consumptionRepository.allConsumptionLastYear();
        int allProfitLastYear = 0;
        for(ConsumptionEntity consumptionEntity:allConsumLastYear){
            allProfitLastYear += consumptionEntity.getAprice();
        }
        ManagementAnalysisVO managementAnalysisVO = new ManagementAnalysisVO();
        managementAnalysisVO.setFirstSR(firstSeatingRatio);
        managementAnalysisVO.setSecondSR(secondSeatingRatio);
        managementAnalysisVO.setThirdSR(thirdSeatingRatio);
        managementAnalysisVO.setFourthSR(fourthSeatingRatio);
        String aplyStr = allProfitLastYear+"元";
        managementAnalysisVO.setAllProfitLastYear(aplyStr);
        System.err.println(managementAnalysisVO.toString());
        return managementAnalysisVO;
    }

    String getSeatingRatio(int num) {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ConsumptionEntity> consumLastQuarter = null;
        String srStr;
        if (num == 1) {
            consumLastQuarter = consumptionRepository.allConsumptionLastQuarter();
        } else if (num == 2) {
            consumLastQuarter = consumptionRepository.allConsumptionLastSecondQuarter();
        } else if (num == 3) {
            consumLastQuarter = consumptionRepository.allConsumptionLastThirdQuarter();
        } else if (num == 4) {
            consumLastQuarter = consumptionRepository.allConsumptionLastFourthQuarter();
        }

        if(consumLastQuarter.size()!=0){
            Map<Integer, List<String>> activityLastQuarter = new HashMap<>();
            //按场馆获得所有活动日期
            for (ConsumptionEntity consumptionEntity : consumLastQuarter) {
                int vid = consumptionEntity.getVid();
                String consumDate = df1.format(consumptionEntity.getDate());
                if (!activityLastQuarter.containsKey(vid)) {
                    List<String> dateList = new ArrayList<>();
                    dateList.add(consumDate);
                    activityLastQuarter.put(vid, dateList);
                } else {
                    if (!activityLastQuarter.get(vid).contains(consumDate)) {
                        activityLastQuarter.get(vid).add(consumDate);
                    }
                }
            }
            //获得总座位数
            Set<Integer> vidKeys = activityLastQuarter.keySet();
            int allSeatNumber = 0;
            for (int vid : vidKeys) {
                int activityTime = activityLastQuarter.get(vid).size();
                VenueEntity venueEntity = venueRepository.findById(vid);
                int seatNumber = venueEntity.getBsnumber() + venueEntity.getFsnumber();
                allSeatNumber += seatNumber * activityTime;
            }
            //获得入座数
            int hasSeatedNumber = 0;
            for (ConsumptionEntity consumptionEntity : consumLastQuarter) {
                hasSeatedNumber += consumptionEntity.getFsnumber() + consumptionEntity.getBsnumber();
            }
            DecimalFormat df2 = new DecimalFormat("0.0");
            String seatingRatio = df2.format((double) hasSeatedNumber / allSeatNumber * 100) + "%";
            srStr = seatingRatio;
        }else{
            srStr = "0.0%";
        }
        return srStr;
    }


}
