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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

}
