package cn.tickets.service.impl;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.ConsumptionRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.VenueService;
import cn.tickets.util.Default;
import cn.tickets.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private VenueRepository venueRepository;
    private ConsumptionRepository consumptionRepository;


    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository,ConsumptionRepository consumptionRepository){
        this.venueRepository = venueRepository;
        this.consumptionRepository = consumptionRepository;
    }




    @Override
    public Map<String,Object> updateVenueInfo(int vid, String location, int fsnumber, int bsnumber,String name){
        Map<String,Object> result = new TreeMap<>();

        VenueEntity venueEntity = venueRepository.findOne(vid);
        venueEntity.setName(name);
        venueEntity.setLocation(location);

        venueEntity.setFsnumber(fsnumber);
        venueEntity.setBsnumber(bsnumber);
        venueEntity.setApprove(1);
        venueRepository.save(venueEntity);


        result.put(Default.HTTP_RESULT, true);
        System.err.println("update venue info result:" + result);
        return result;
    }




    public String statistics(Model model, int vid) {
        System.err.println(vid);
        List<ConsumptionEntity> bookConsumptionEntities = consumptionRepository.findByVidAndPredefine(vid, 1);//预订订单
        List<ConsumptionEntity> unsubscribeConsumptionEntities = consumptionRepository.findByVidAndPredefine(vid, 0);//退订订单
        List<ConsumptionEntity> completeConsumptionEntities = consumptionRepository.findByVidAndPredefine(vid, 2);//完成订单
//        System.err.println("statistics预订订单：" + bookConsumptionEntities.toString());
//        System.err.println("statistics退订订单：" + unsubscribeConsumptionEntities.toString());
//        System.err.println("statistics完成订单：" + completeConsumptionEntities.toString());
        model.addAttribute("statistics", buildVenueStatisticsVO(bookConsumptionEntities, unsubscribeConsumptionEntities, completeConsumptionEntities));
        return "venue/statistics";
    }



    public VenueStatisticsVO buildVenueStatisticsVO(
            List<ConsumptionEntity> bookConsumptionEntities,
            List<ConsumptionEntity> unsubscribeConsumptionEntities,
            List<ConsumptionEntity> completeConsumptionEntities) {
        VenueStatisticsVO vo = new VenueStatisticsVO();
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
    public List<String> analysis(Model model, int vid) {
        List<ConsumptionEntity> venueConsumptionListLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityType = new ArrayList<>();
        for(ConsumptionEntity consumptionEntity:venueConsumptionListLastYear){
            String activityName = consumptionEntity.getType();
            if(!activityType.contains(activityName)){
                activityType.add(activityName);
            }
        }
        return activityType;
    }

    @Override
    public Map<String, Object> memberNumber(int vid) {
        Map<String, Object> result = new TreeMap<>();

        List<ConsumptionEntity> venueConsumptionListLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
        int[] memberNumberArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Integer> memberNumberList = new ArrayList<>(12);
        Map<String,Set<Integer>> memberChangeList = new TreeMap<>();

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
            memberChangeList.put(m.toString(),new TreeSet<>());
            c.add(Calendar.MONTH, -1);
            c2.add(Calendar.MONTH, -1);
        }
        for (ConsumptionEntity consumptionEntity : venueConsumptionListLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < monthList.size(); i++) {
                if (orderdate.compareTo(monthList.get(i)) > 0 && orderdate.compareTo(monthList2.get(i)) < 0) {
                    memberChangeList.get(monthList.get(i)).add(consumptionEntity.getMid());
                }
            }
        }
        for(int i = 0;i<monthList.size();i++){
            memberNumberList.add(memberChangeList.get(monthList.get(i)).size());
        }
        result.put("monthList",monthList);
        result.put("memberNumberList",memberNumberList);

        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> profitChange(int vid) {
        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> unitPriceChange(int vid) {
        Map<String, Object> result = new TreeMap<>();

        List<ConsumptionEntity> venueConsumptionListLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
//        int[] memberNumberArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        List<Integer> memberNumberList = new ArrayList<>(12);
//        Map<String,Set<Integer>> memberChangeList = new TreeMap<>();

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
//            memberChangeList.put(m.toString(),new TreeSet<>());
            c.add(Calendar.MONTH, -1);
            c2.add(Calendar.MONTH, -1);
        }

        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> profitDistribution(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityName = new ArrayList<>();
        for(ConsumptionEntity consumptionEntity:listLastYear){
            String type = consumptionEntity.getType();
            if(!activityName.contains(type)){
                activityName.add(type);
            }
        }
        List<Integer> profitSum = new ArrayList<>();
        for(String type:activityName){
            int sum = 0;
            for(ConsumptionEntity consumptionEntity:listLastYear){
                if(consumptionEntity.getType().equals(type)){
                    sum+=consumptionEntity.getAprice();
                }
            }
            profitSum.add(sum);
        }
        result.put("ActivityType",activityName);
        result.put("ProfitSum",profitSum);
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> activityDistribution(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        if (listLastYear.size() > 0) {
            TreeSet<String> activitySet = new TreeSet<>();
            for (ConsumptionEntity consumptionEntity : listLastYear) {
                activitySet.add(consumptionEntity.getType());//获得所有活动类型的集合
            }
            //活动集合
            TreeMap<String, Integer> activityMap = new TreeMap<>();
            for (String activity : activitySet) {
                int num = 0;
                for (ConsumptionEntity consumptionEntity : listLastYear) {
                    if (consumptionEntity.getType().equals(activity)) {
                        num += 1;
                    }
                }
                //获得所有活动参加的次数
                activityMap.put(activity, num);
            }
            for (String getKey : activityMap.keySet()) {
                activityList.add(getKey);
                numberList.add(activityMap.get(getKey));
            }
        } else {
            activityList.add("无");
            numberList.add(0);
        }
        result.put("activityList", activityList);
        result.put("numberList", numberList);
        result.put(Default.HTTP_RESULT, true);
        return result;
    }
}
