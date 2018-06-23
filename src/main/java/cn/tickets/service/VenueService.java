package cn.tickets.service;

import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.vo.DetailsVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VenueService {

    Map<String,Object> updateVenueInfo(int vid, String location, int fsnumber, int bsnumber,String name);//更新场馆信息
    String statistics(Model model, int vid);

    //以下是管理信息系统新增代码
    List<String> analysis(Model model,int vid);

    Map<String, Object> memberNumber(int vid);
    Map<String, Object> profitAverage(int vid);
    Map<String, Object> unitPriceChange(int vid);
    Map<String, Object> profitDistribution(int vid);
    Map<String, Object> activityDistribution(int vid);
    DetailsVO details(int vid, String type);
    Map<String, Object> priceSeatingFunction(int vid, String type);
    Map<String, Object> profitPriceFunction(int vid, String type);
}
