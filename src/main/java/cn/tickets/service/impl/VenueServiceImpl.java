package cn.tickets.service.impl;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.ConsumptionRepository;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.ConsumptionService;
import cn.tickets.service.VenueService;
import cn.tickets.util.Default;
import cn.tickets.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private VenueRepository venueRepository;
    private ConsumptionRepository consumptionRepository;
    private PlanRepository planRepository;


    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, ConsumptionRepository consumptionRepository, PlanRepository planRepository) {
        this.venueRepository = venueRepository;
        this.consumptionRepository = consumptionRepository;
        this.planRepository = planRepository;
    }


    @Override
    public Map<String, Object> updateVenueInfo(int vid, String location, int fsnumber, int bsnumber, String name) {
        Map<String, Object> result = new TreeMap<>();

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
        for (ConsumptionEntity consumptionEntity : venueConsumptionListLastYear) {
            String activityName = consumptionEntity.getType();
            if (!activityType.contains(activityName)) {
                activityType.add(activityName);
            }
        }
        return activityType;
    }

    @Override
    public Map<String, Object> memberNumber(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<Integer> memberNumberList = new ArrayList<>();
        List<ConsumptionEntity> venueConsumptionListLastYear = consumptionRepository.venueConsumptionLastYear(vid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
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
        for (ConsumptionEntity consumptionEntity : venueConsumptionListLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < monthList.size(); i++) {
                if (orderdate.compareTo(monthList.get(i)) > 0 && orderdate.compareTo(monthList2.get(i)) < 0) {

                }
            }
        }
        for (int i = 0; i < monthList.size(); i++) {
            HashSet<Integer> midSet = new HashSet<>();
            for (ConsumptionEntity consumptionEntity : venueConsumptionListLastYear) {
                String orderdate = consumptionEntity.getOrderdate().toString();
                if (orderdate.compareTo(monthList.get(i)) > 0 && orderdate.compareTo(monthList2.get(i)) < 0) {
                    midSet.add(consumptionEntity.getMid());
                }
            }
            memberNumberList.add(midSet.size());
        }

        result.put("monthList", monthList);
        result.put("memberNumberList", memberNumberList);

        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> profitAverage(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityType = new ArrayList<>();
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String activity = consumptionEntity.getType();
            if (!activityType.contains(activity)) {
                activityType.add(activity);
            }
        }
        List<Double> profitAverage = new ArrayList<>();
        for (String activity : activityType) {
            int sum = 0, iter = 1;
            for (ConsumptionEntity consumptionEntity : listLastYear) {
                if (consumptionEntity.getType().equals(activity)) {
                    sum += consumptionEntity.getAprice();
                    iter += 1;
                }
            }
            double average = (double) sum / iter;
            DecimalFormat df = new DecimalFormat("0.0");
            average = Double.parseDouble(df.format(average));
            profitAverage.add(average);
        }
        result.put("activityType", activityType);
        result.put("profitAverage", profitAverage);
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> unitPriceChange(int vid) {
        Map<String, Object> result = new TreeMap<>();

        List<ConsumptionEntity> venueConsumptionListLastYear = consumptionRepository.venueConsumptionLastYear(vid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        int[] priceSumArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] seatSumArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Double> unitPriceList = new ArrayList<>();
//        int[] memberNumberArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        List<Integer> memberNumberList = new ArrayList<>(12);
//        Map<String,Set<Integer>> memberChangeList = new TreeMap<>();

        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
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

        for (ConsumptionEntity consumptionEntity : venueConsumptionListLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < monthList.size(); i++) {
                if (orderdate.compareTo(monthList.get(i)) > 0 && orderdate.compareTo(monthList2.get(i)) < 0) {
                    priceSumArray[i] += consumptionEntity.getAprice();
                    seatSumArray[i] += consumptionEntity.getFsnumber() + consumptionEntity.getBsnumber();
                }
            }
        }
        for (int i = 0; i < monthList.size(); i++) {
            double unitPrice;
            if (seatSumArray[i] != 0) {
                unitPrice = priceSumArray[i] / seatSumArray[i];
            } else {
                unitPrice = 0.0;
            }

            DecimalFormat df = new DecimalFormat("0.0");
            Double unitPriceDouble = Double.parseDouble(df.format(unitPrice));
            unitPriceList.add(unitPriceDouble);
        }
        result.put("monthList", monthList);
        result.put("unitPrice", unitPriceList);
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> profitDistribution(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityName = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
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

        //获得所有活动名
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String activityType = consumptionEntity.getType();
            if (!activityName.contains(activityType)) {
                activityName.add(activityType);
            }
        }
        int[][] profitPerMonthArray = new int[activityName.size()][12];
        for (int i = 0; i < activityName.size(); i++) {
            for (int j = 0; j < 12; j++) {
                profitPerMonthArray[i][j] = 0;
            }
        }
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < activityName.size(); i++) {
                if (activityName.get(i).equals(consumptionEntity.getType())) {
                    for (int j = 0; j < monthList.size(); j++) {
                        if (orderdate.compareTo(monthList.get(j)) > 0 && orderdate.compareTo(monthList2.get(j)) < 0) {
                            profitPerMonthArray[i][j] += consumptionEntity.getAprice();
                        }
                    }
                }
            }
        }
        List<List<Integer>> profitPerMonth = new ArrayList<>();

        for (int i = 0; i < activityName.size(); i++) {
            List<Integer> profitPerActivity = new ArrayList<>();
            for (int j = 0; j < profitPerMonthArray[i].length; j++) {
                profitPerActivity.add(profitPerMonthArray[i][j]);
            }
            profitPerMonth.add(profitPerActivity);
        }

        result.put("activityType", activityName);
        result.put("monthList", monthList);
        result.put("profitPerMonth", profitPerMonth);
        System.err.println(profitPerMonth.size());
        System.err.println(profitPerMonth.toString());
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public Map<String, Object> activityDistribution(int vid) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> listLastYear = consumptionRepository.venueConsumptionLastYear(vid);
        List<String> activityName = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> monthList = new ArrayList<>(12);
        List<String> monthList2 = new ArrayList<>(12);
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

        //获得所有活动名
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String activityType = consumptionEntity.getType();
            if (!activityName.contains(activityType)) {
                activityName.add(activityType);
            }
        }
        int[][] numberPerMonthArray = new int[activityName.size()][12];
        for (int i = 0; i < activityName.size(); i++) {
            for (int j = 0; j < 12; j++) {
                numberPerMonthArray[i][j] = 0;
            }
        }
        for (ConsumptionEntity consumptionEntity : listLastYear) {
            String orderdate = consumptionEntity.getOrderdate().toString();
            for (int i = 0; i < activityName.size(); i++) {
                if (activityName.get(i).equals(consumptionEntity.getType())) {
                    for (int j = 0; j < monthList.size(); j++) {
                        if (orderdate.compareTo(monthList.get(j)) > 0 && orderdate.compareTo(monthList2.get(j)) < 0) {
                            numberPerMonthArray[i][j] += 1;
                        }
                    }
                }
            }
        }

        List<List<Integer>> numberPerMonth = new ArrayList<>();
        for (int i = 0; i < activityName.size(); i++) {
            List<Integer> numberPerActivity = new ArrayList<>();
            for (int j = 0; j < numberPerMonthArray[i].length; j++) {
                numberPerActivity.add(numberPerMonthArray[i][j]);
            }
            numberPerMonth.add(numberPerActivity);
        }

        result.put("activityType", activityName);
        result.put("monthList", monthList);
        result.put("numberPerMonth", numberPerMonth);
        System.err.println(numberPerMonth.size());
        System.err.println(numberPerMonth.toString());
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @Override
    public DetailsVO details(int vid, String type) {
        List<ConsumptionEntity> typeConsumptionSum = consumptionRepository.typeConsumptionLastYear(type);
        List<ConsumptionEntity> venueTypeConsumptionSum = consumptionRepository.venueTypeConsumptionLastYear(vid, type);
//        System.err.println(typeConsumptionSum.toString());
//        System.err.println(venueTypeConsumptionSum.toString());
        double averagePrice = 0, venueAveragePrice = 0;
        for (ConsumptionEntity consumptionEntity : typeConsumptionSum) {
            averagePrice += consumptionEntity.getFprice() + consumptionEntity.getBprice();
        }
        averagePrice /= typeConsumptionSum.size();
        for (ConsumptionEntity consumptionEntity : venueTypeConsumptionSum) {
            venueAveragePrice += consumptionEntity.getFprice() + consumptionEntity.getBprice();
        }
        venueAveragePrice /= venueTypeConsumptionSum.size();
        DecimalFormat df = new DecimalFormat("0.0");
        DetailsVO detailsVO = new DetailsVO();
        String price = df.format(venueAveragePrice / averagePrice * 100) + "%";
        //给票价指数赋值
        detailsVO.setPrice(price);

        DateFormat df2 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        List<String> dateList = new ArrayList<>();
        int seatingNumber = 0;
        for (ConsumptionEntity consumptionEntity : venueTypeConsumptionSum) {
            String date = df2.format(consumptionEntity.getDate());
//            System.err.println(date);
            if (!dateList.contains(date)) {
                dateList.add(date);
            }
            seatingNumber += consumptionEntity.getFsnumber() + consumptionEntity.getBsnumber();
        }
        int allSeatNumber = venueRepository.findById(vid).getFsnumber() + venueRepository.findById(vid).getBsnumber();
        System.err.println("seatingNumber:"+seatingNumber);
        double seatingRatio = (double)seatingNumber / (dateList.size()* allSeatNumber);
        String seat = df.format(seatingRatio*100)+"%";
        //给入座率赋值
        detailsVO.setSeat(seat);

        long venueProfit=0,allProfit=0;
        List<String> allDatelist = new ArrayList<>();
        for(ConsumptionEntity consumptionEntity:typeConsumptionSum){
            allProfit += consumptionEntity.getAprice();
            String date = df2.format(consumptionEntity.getDate());
            if(!allDatelist.contains(date)){
                allDatelist.add(date);
            }
        }
        System.err.println("allDatelist:"+allDatelist.size());
        double allProfitDouble = (double)allProfit / allDatelist.size();
        System.err.println("allProfit:"+allProfit);
        for(ConsumptionEntity consumptionEntity:venueTypeConsumptionSum){
            venueProfit += consumptionEntity.getAprice();
        }
        double venueProfitDouble = (double)venueProfit / dateList.size();
        System.err.println("venueProfit:"+venueProfit);
        String profit = df.format(venueProfitDouble / allProfitDouble * 100)+"%";
        //给收益率赋值
        detailsVO.setProfit(profit);

        return detailsVO;
    }

    @Override
    public Map<String, Object> priceSeatingFunction(int vid,String type) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> consumptionEntities = consumptionRepository.venueTypeConsumptionLastYear(vid,type);
        List<String> dateList = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        for(ConsumptionEntity consumptionEntity:consumptionEntities){
            //获得所有订票的活动开始日期
            String date = df1.format(consumptionEntity.getDate());
            if(!dateList.contains(date)){
                dateList.add(date);
            }
        }
        //计算入座率
        int seatNumber = 0;
        List<Double> seatRatioList = new ArrayList<>();
        VenueEntity venueEntity = venueRepository.findById(vid);
        List<Integer> seatPriceList = new ArrayList<>();
        int allSeatNumber = venueEntity.getFsnumber()+venueEntity.getBsnumber();
        DecimalFormat df2 = new DecimalFormat("0.0");
        for(String date : dateList){
            for(ConsumptionEntity consumptionEntity:consumptionEntities){
                if(date.equals(df1.format(consumptionEntity.getDate()))){
                    seatPriceList.add((consumptionEntity.getFprice()+consumptionEntity.getBprice())/2);
                }
                break;
            }
            for(ConsumptionEntity consumptionEntity:consumptionEntities){
                if(date.equals(consumptionEntity.getDate())){
                    seatNumber+=consumptionEntity.getFsnumber()+consumptionEntity.getBsnumber();
                }
            }
            String seatRadioStr = df2.format((double)seatNumber/(allSeatNumber*dateList.size())*100);
            double seatRadio = Double.parseDouble(seatRadioStr);
            seatRatioList.add(seatRadio);
        }
        boolean flag = false;
        if(dateList.size()==seatPriceList.size()&&dateList.size()==seatRatioList.size()){
            result.put("seatPrice",seatPriceList);
            result.put("seatRatio",seatRatioList);
            flag = true;
        }
        result.put(Default.HTTP_RESULT, flag);
        return null;
    }

    @Override
    public Map<String, Object> profitPriceFunction(int vid, String type) {
        Map<String, Object> result = new TreeMap<>();
        List<ConsumptionEntity> consumptionEntities = consumptionRepository.venueTypeConsumptionLastYear(vid,type);
        List<String> dateList = new ArrayList<>();
        List<Integer> seatPriceList = new ArrayList<>();
        List<Integer> allProfitList = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        for(ConsumptionEntity consumptionEntity:consumptionEntities){

            String date = df1.format(consumptionEntity.getDate());
            if(!dateList.contains(date)){
                dateList.add(date);
            }
        }
        for(String date:dateList){
            int allProfit = 0;
            for(ConsumptionEntity consumptionEntity:consumptionEntities){
                if(date.equals(df1.format(consumptionEntity.getDate()))){
                    seatPriceList.add((consumptionEntity.getFprice()+consumptionEntity.getBprice())/2);
                }
                break;
            }
            for(ConsumptionEntity consumptionEntity:consumptionEntities){
                if(date.equals(df1.format(consumptionEntity.getDate()))){
                    allProfit += consumptionEntity.getAprice();
                }
            }
            allProfitList.add(allProfit);
        }
        boolean flag = false;
        if(dateList.size()==seatPriceList.size()&&dateList.size()==allProfitList.size()){
            result.put("seatPrice",seatPriceList);
            result.put("allProfit",allProfitList);
            flag = true;
        }

        result.put(Default.HTTP_RESULT, flag);
        return null;
    }


}
