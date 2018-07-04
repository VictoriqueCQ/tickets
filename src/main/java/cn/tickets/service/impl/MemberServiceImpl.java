package cn.tickets.service.impl;

import cn.tickets.entity.*;
import cn.tickets.repository.*;
import cn.tickets.service.MemberService;
import cn.tickets.util.Default;
import cn.tickets.vo.*;
import org.hibernate.Transaction
        ;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;
    private AccountRepository accountRepository;
    private VenueRepository venueRepository;
    private ConsumptionRepository consumptionRepository;
    private SeatRepository seatRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, AccountRepository accountRepository, VenueRepository venueRepository, ConsumptionRepository consumptionRepository, SeatRepository seatRepository) {
        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.venueRepository = venueRepository;
        this.consumptionRepository = consumptionRepository;
        this.seatRepository = seatRepository;
    }


    @Override
    public MemberInfoVO getMemberProfile(int mid) {
        MemberEntity entity = memberRepository.findOne(mid);
        MemberInfoVO vo = new MemberInfoVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setDiscount(vo.getDiscount() * vo.getLevel());
        return vo;
    }

    public String statistics(Model model, int mid) {
        System.err.println(mid);
        List<ConsumptionEntity> bookConsumptionEntities = consumptionRepository.findByMidAndPredefine(mid, 1);//预订订单
        List<ConsumptionEntity> unsubscribeConsumptionEntities = consumptionRepository.findByMidAndPredefine(mid, 0);//退订订单
        List<ConsumptionEntity> completeConsumptionEntities = consumptionRepository.findByMidAndPredefine(mid, 2);//完成订单

        model.addAttribute("statistics", buildMemberStatisticsVO(bookConsumptionEntities, unsubscribeConsumptionEntities, completeConsumptionEntities));
        return "member/statistics";
    }


    public MemberStatisticsVO buildMemberStatisticsVO(
            List<ConsumptionEntity> bookConsumptionEntities,
            List<ConsumptionEntity> unsubscribeConsumptionEntities,
            List<ConsumptionEntity> completeConsumptionEntities) {
        MemberStatisticsVO vo = new MemberStatisticsVO();
        //预订订单
        List<BookOrderVO> bookOrderVOS = new ArrayList<>(bookConsumptionEntities.size());

        bookConsumptionEntities.forEach(entity -> {
            BookOrderVO bookOrderVO = new BookOrderVO();
            BeanUtils.copyProperties(entity, bookOrderVO);
            bookOrderVO.setVenueName(venueRepository.findOne(entity.getVid()).getName());
            bookOrderVOS.add(bookOrderVO);
        });

        //退订订单
        List<UnsubscribeOrderVO> unsubscribeOrderVOS = new ArrayList<>(unsubscribeConsumptionEntities.size());

        unsubscribeConsumptionEntities.forEach(entity -> {
            UnsubscribeOrderVO unsubscribeOrderVO = new UnsubscribeOrderVO();
            BeanUtils.copyProperties(entity, unsubscribeOrderVO);
            unsubscribeOrderVO.setVenueName(venueRepository.findOne(entity.getVid()).getName());
            unsubscribeOrderVOS.add(unsubscribeOrderVO);
        });

        //完成订单
        List<CompleteOrderVO> completeOrderVOS = new ArrayList<>(completeConsumptionEntities.size());

        completeConsumptionEntities.forEach(entity -> {
            CompleteOrderVO completeOrderVO = new CompleteOrderVO();
            BeanUtils.copyProperties(entity, completeOrderVO);
//            completeOrderVO.setVenueName(venueEntities.get(0).getName());
            completeOrderVO.setVenueName(venueRepository.findOne(entity.getVid()).getName());
            completeOrderVOS.add(completeOrderVO);
        });
        vo.setBookOrderVOS(bookOrderVOS);
        vo.setUnsubscribeOrderVOS(unsubscribeOrderVOS);
        vo.setCompleteOrderVOS(completeOrderVOS);
//        System.err.println(completeOrderVOS);
        return vo;
    }


    @Override
    public Map<String, Object> edit(int id, String name, String mail, String phone) {
        Map<String, Object> result = new TreeMap<>();
        AccountEntity accountEntity = accountRepository.findOne(id);
        accountEntity.setEmail(mail);

        accountRepository.save(accountEntity);
        MemberEntity memberEntity = memberRepository.findOne(id);
        memberEntity.setName(name);
        memberEntity.setPhone(phone);
        memberEntity.setEmail(mail);
        memberRepository.save(memberEntity);
        result.put(Default.HTTP_RESULT, true);
        System.err.println("member result:" + result);
        return result;
    }

    @Override
    public Map<String, Object> deleteAccount(int id, String password) {
        Map<String, Object> result = new TreeMap<>();
        if (accountRepository.existsByIdAndPassword(id, password)) {
            memberRepository.updateQualification(id, 0);
            result.put(Default.HTTP_RESULT, true);
            return result;
        }
        result.put(Default.HTTP_RESULT, false);
        result.put(Default.HTTP_REASON, "password is error!");
        return result;
    }

    @Override
    public Map<String, Object> reducePoint(int id, int point) {
        Map<String, Object> result = new TreeMap<>();
        MemberEntity memberEntity = memberRepository.findOne(id);
        memberEntity.setPoint(memberEntity.getPoint() - point);
        memberRepository.save(memberEntity);
        result.put(Default.HTTP_RESULT, true);
        System.err.println("reduce point:" + memberEntity);
        return result;
    }

    @Override
    public boolean unSubscribe(int cid) {

        ConsumptionEntity consumptionEntity = consumptionRepository.findOne(cid);
        consumptionEntity.setPredefine(0);

        int aprice = consumptionEntity.getAprice();
        int mid = consumptionEntity.getMid();

        Timestamp timestamp = consumptionEntity.getDate();
        Date date = new Date();
        Timestamp timestampNow = new Timestamp(date.getTime());
        long deltaTime = (timestamp.getTime() - timestampNow.getTime()) / (1000 * 60 * 60 * 24 * 14);//毫秒转成周，2周
        System.err.println("deltaTime:" + deltaTime + "周");
        if (consumptionEntity.getIsbought() == 1) {//付款了才能退钱
            if (deltaTime >= 2) {
                MemberEntity memberEntity = memberRepository.findOne(mid);
                memberEntity.setMoney(memberEntity.getMoney() + aprice);

                memberRepository.save(memberEntity);
            } else {
                MemberEntity memberEntity = memberRepository.findOne(mid);
                memberEntity.setMoney(memberEntity.getMoney() + aprice / 2);//2周内退半价，积分不扣

                memberRepository.save(memberEntity);
            }
        }


        consumptionRepository.updatePredefine(0, cid);//设为退订状态

        List<SeatEntity> seatEntities = seatRepository.findByCid(cid);
        if (seatEntities != null) {
            for (SeatEntity seatEntity : seatEntities) {
                seatEntity.setCid(0);
                seatEntity.setIsbooked(0);
                Timestamp ts = null;
                String tsStr = "1980-01-01 00:00:00";
                try {
                    ts = Timestamp.valueOf(tsStr);
                    System.out.println(ts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                seatEntity.setOrderdate(ts);
                seatRepository.save(seatEntity);
            }
        }
        return true;
    }


    //以下是管理信息系统作业新增代码
    @Override
    public MemberAnalysisVO analysis(int mid) {
        MemberAnalysisVO memberAnalysisVO = new MemberAnalysisVO();
        List<ConsumptionEntity> listLastYear = consumptionRepository.allConsumptionsLastYear(mid);
//        System.err.println(listLastYear.toString());
        List<ConsumptionEntity> listLastMonth = consumptionRepository.consumptionsLastMonth(mid);
//        System.err.println(listLastMonth.toString());

        //月平均订单单价
        String averagePriceLastMonth = "";
        if (listLastMonth.size() > 0) {
            int allPriceLastMonth = 0;
            for (ConsumptionEntity consumptionEntity : listLastMonth) {
                allPriceLastMonth += consumptionEntity.getAprice();
            }

            averagePriceLastMonth = allPriceLastMonth / listLastMonth.size() + "元/月";
        } else {
            averagePriceLastMonth = "0元/月";
        }
        memberAnalysisVO.setAveragePerMonth(averagePriceLastMonth);

        //各种年平均
        String averagePriceLastYear = "";
        List<String> activityKeyList = new ArrayList<>();
        List<String> venueKeyList = new ArrayList<>();
        String refundRatio = "";
        String orderRatio = "";
        if (listLastYear.size() > 0) {
            TreeSet<String> activitySet = new TreeSet<>();
            TreeSet<Integer> venueIdSet = new TreeSet<>();
            int allPriceLastYear = 0;
            int refundTime = 0;//退单单数
            for (ConsumptionEntity consumptionEntity : listLastYear) {
//                System.err.println(consumptionEntity.getPredefine());
                allPriceLastYear += consumptionEntity.getAprice();
                activitySet.add(consumptionEntity.getType());//获得所有活动类型的集合
                venueIdSet.add(consumptionEntity.getVid());//获得所有场馆id的集合
                if (consumptionEntity.getPredefine() == 0) {
                    refundTime += 1;
                }
            }
            System.err.println("退单次数" + refundTime);
            averagePriceLastYear = allPriceLastYear / listLastYear.size() + "元/月";


            //退单率计算
            DecimalFormat df = new DecimalFormat("0.0");
            refundRatio = df.format(((double) refundTime / listLastYear.size()) * 100) + "%";

            //下单频率计算
            orderRatio = df.format((double) listLastYear.size() / 12) + "单/月";


            List<ConsumptionEntity> completeListLastYear = consumptionRepository.consumptionsLastYear(mid);
            //活动集合
            TreeMap<String, Integer> activityMap = new TreeMap<>();
            for (String activity : activitySet) {
                int num = 0;
                for (ConsumptionEntity consumptionEntity : completeListLastYear) {
                    if (consumptionEntity.getType().equals(activity)) {
                        num += 1;
                    }
                }
                //获得所有活动参加的次数
                activityMap.put(activity, num);
            }
            int activityMaxTimes = 0;
            for (Integer time : activityMap.values()) {
                //获得最大参加次数
                activityMaxTimes = activityMaxTimes >= time ? activityMaxTimes : time;
            }
            System.err.println("参加最多的活动次数"+activityMaxTimes);
            for (String getKey : activityMap.keySet()) {
                //获得最大参加次数的活动的列表
                if (activityMap.get(getKey) == activityMaxTimes) {
                    activityKeyList.add(getKey);
                }
            }


            //场馆集合
            TreeMap<Integer, Integer> venueMap = new TreeMap<>();
            for (Integer venueId : venueIdSet) {
                int num = 0;
                for (ConsumptionEntity consumptionEntity : completeListLastYear) {
                    if (consumptionEntity.getVid() == venueId) {
                        num += 1;
                    }
                }
                //获得所有场馆去的次数集合
                venueMap.put(venueId, num);
            }
            int venueMaxTimes = 0;
            for (Integer time : venueMap.values()) {
                //获得去场馆最大次数
                venueMaxTimes = venueMaxTimes >= time ? venueMaxTimes : time;
            }

            for (Integer getKey : venueMap.keySet()) {
                if (venueMap.get(getKey) == venueMaxTimes) {
                    VenueEntity venueEntity = venueRepository.findOne(getKey);
                    venueKeyList.add(venueEntity.getName());
                }
            }

        } else {
            averagePriceLastYear = "0元/月";
            activityKeyList.add("无");
            venueKeyList.add("无");
            refundRatio = "--%";
            orderRatio = "0单/月";
        }
        //年平均订单单价
        memberAnalysisVO.setAveragePerYear(averagePriceLastYear);
        memberAnalysisVO.setMostActivity(activityKeyList.toString());
        memberAnalysisVO.setMostVenue(venueKeyList.toString());
        //退单率
        memberAnalysisVO.setRefundRatio(refundRatio);
        //下单频率
        memberAnalysisVO.setOrderRatio(orderRatio);

        return memberAnalysisVO;
    }

    @Override
    public Map<String, Object> activityDistribution(int mid) {
        Map<String, Object> result = new TreeMap<>();
        List<String> activityList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.consumptionsLastYear(mid);
        if (listLastYear.size() > 0) {
            List<String> activityTypeList = new ArrayList<>();
            for(ConsumptionEntity consumptionEntity:listLastYear){
                if(!activityTypeList.contains(consumptionEntity.getType())){
                    activityTypeList.add(consumptionEntity.getType());
                }
//                System.err.println(consumptionEntity.toString()+"————————————————————"+activityTypeList.toString());
            }
//            TreeSet<String> activitySet = new TreeSet<>();
//            for (ConsumptionEntity consumptionEntity : listLastYear) {
//                activitySet.add(consumptionEntity.getType());//获得所有活动类型的集合
//            }
            //活动集合
            List<Integer> activityTime = new ArrayList<>();
            for(String activityType:activityTypeList){
                int num = 0;
                for(ConsumptionEntity consumptionEntity:listLastYear){
                    if(consumptionEntity.getType().equals(activityType)){
                        num+=1;
                    }
                }
                activityTime.add(num);
            }
//            TreeMap<String, Integer> activityMap = new TreeMap<>();
//            for (String activity : activitySet) {
//                int num = 0;
//                for (ConsumptionEntity consumptionEntity : listLastYear) {
//                    if (consumptionEntity.getType().equals(activity)) {
//                        num += 1;
//                    }
//                }
//                //获得所有活动参加的次数
//                activityMap.put(activity, num);
//            }
            for(int i = 0;i<activityTypeList.size();i++){
                activityList.add(activityTypeList.get(i));
                numberList.add(activityTime.get(i));
            }
//            for (String getKey : activityMap.keySet()) {
//                activityList.add(getKey);
//                numberList.add(activityMap.get(getKey));
//            }
        } else {
            activityList.add("无");
            numberList.add(0);
        }
//        System.err.println("activityList"+activityList.toString());
        result.put("activityList", activityList);
        result.put("numberList", numberList);
        result.put(Default.HTTP_RESULT, true);
        System.err.println("activityDistribution:"+result);
        return result;
    }

    @Override
    public Map<String, Object> consumptionDistribution(int mid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.consumptionsLastYear(mid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
        int[] moneyArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Integer> moneyList = new ArrayList<>(12);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());

        c2.add(Calendar.MONTH, 1);
        for (int i = 0; i < 12; i++) {

            Date m = c.getTime();
            Date m2 = c2.getTime();
            monthList.add(sdf.format(m));
            monthList2.add(sdf.format(m2));
            c.add(Calendar.MONTH, -1);
            c2.add(Calendar.MONTH, -1);
        }
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < monthList.size(); i++) {
                if (orderdate.compareTo(monthList.get(i)) > 0 && orderdate.compareTo(monthList2.get(i)) < 0) {
                    moneyArray[i] += consumptionEntity.getAprice();
                }
            }
        }
//        for(int i = 0;i<monthList.size();i++){
//            for(ConsumptionEntity consumptionEntity:listLastYear){
//                String
//            }
//        }
//        System.err.println(moneyArray.toString());
        for(int i = 0;i<moneyArray.length;i++){
//            System.err.println("moneyArray:"+moneyArray[i]);
            moneyList.add(moneyArray[i]);
        }
        result.put("monthList",monthList);
        result.put("moneyList",moneyList);
        result.put(Default.HTTP_RESULT,true);
        System.err.println("consumptionDistribution:"+result);
        return result;
    }

    @Override
    public Map<String, Object> venueDistribution(int mid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.consumptionsLastYear(mid);
        List<String> venueList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        if (listLastYear.size() > 0) {
            TreeSet<Integer> venueIdSet = new TreeSet<>();
            for (ConsumptionEntity consumptionEntity : listLastYear) {
                venueIdSet.add(consumptionEntity.getVid());//获得所有场馆id的集合
            }
            //场馆集合
            TreeMap<Integer, Integer> venueMap = new TreeMap<>();
            for (Integer venueId : venueIdSet) {
                int num = 0;
                for (ConsumptionEntity consumptionEntity : listLastYear) {
                    if (consumptionEntity.getVid() == venueId) {
                        num += 1;
                    }
                }
                //获得所有场馆去的次数集合
                venueMap.put(venueId, num);
            }
            for (int getKey : venueMap.keySet()) {
                venueList.add(venueRepository.findOne(getKey).getName());
                numberList.add(venueMap.get(getKey));
            }
        } else {
            venueList.add("无");
            numberList.add(0);
        }
        result.put("venueList", venueList);
        result.put("numberList", numberList);
        result.put(Default.HTTP_RESULT, true);
        System.err.println("venueDistribution:"+result);
        return result;
    }

}
