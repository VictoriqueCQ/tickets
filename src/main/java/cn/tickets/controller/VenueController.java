package cn.tickets.controller;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.ConsumptionService;
import cn.tickets.service.ManagerService;
import cn.tickets.service.PlanService;
import cn.tickets.service.VenueService;
import cn.tickets.util.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/venue")
public class VenueController {
    private VenueService venueService;
    private PlanService planService;
    private ConsumptionService consumptionService;
    private VenueRepository venueRepository;
    private ManagerService managerService;
    private static Locale locale = new Locale("zh");

    @Autowired
    public VenueController(VenueService venueService, PlanService planService, ConsumptionService consumptionService, VenueRepository venueRepository, ManagerService managerService) {
        this.venueService = venueService;
        this.planService = planService;
        this.consumptionService = consumptionService;
        this.venueRepository = venueRepository;
        this.managerService = managerService;
    }

    //下面两个方法是查看本场馆信息
    @RequestMapping("/info")
    public String findVenueInfo(Model model, @SessionAttribute(Default.USER_ID) int vid) {
        VenueEntity venueEntity = venueRepository.findOne(vid);
        model.addAttribute("venue", venueEntity);

        return "venue/info";
    }

    @RequestMapping("/statistics")
    public String statistics(Model model, @SessionAttribute(Default.USER_ID) int vid) {
        model.addAttribute("locale", locale);
        return venueService.statistics(model, vid);
    }



    @RequestMapping({"/", "/index"})
    public String index(@SessionAttribute(Default.USER_ID) int id, Model model) {
        model.addAttribute("locale", locale);
        System.err.println(planService.indexplan(model, id));
        return planService.indexplan(model, id);
    }

    //修改场馆信息
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@SessionAttribute(Default.USER_ID) int id, String name, String address, int fsnumber, int bsnumber) {
        System.err.println("update venue info:" + id + " " + name + " " + address + " " + fsnumber + " " + bsnumber);
        return venueService.updateVenueInfo(id, address, fsnumber, bsnumber, name);
    }

    @RequestMapping(value = "/savePlan", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePlan(@SessionAttribute(Default.USER_ID) int id, String time, String type, String description, int fprice, int bprice) {
        System.err.println("save plan:" + id + " " + time + " " + type + " " + description + " " + fprice + " " + bprice);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try {
            date = sdf.parse(time);
            System.err.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = df.format(date);
        Timestamp timestamp = Timestamp.valueOf(time1);
        return planService.savePlan(id, timestamp, type, description, fprice, bprice);
    }

    @RequestMapping("/scene")
    public String scene(@SessionAttribute(Default.USER_ID) int id, Model model) {
        model.addAttribute("locale", locale);
        return planService.scenePlan(model,id);
    }

    @RequestMapping("/buy")
    @ResponseBody
    public Map<String, Object> buy(@SessionAttribute(Default.USER_ID) int id, int mid, int pid, int fsnumber, int bsnumber) {
        Map<String, Object> result = new TreeMap<>();
        int aprice = consumptionService.sceneBuy(id,mid,pid,fsnumber,bsnumber);
        result.put("aprice",aprice);
        result.put(Default.HTTP_RESULT,true);

        return result;
    }

    @RequestMapping("/check")
    @ResponseBody
    public Map<String, Object> check(int cid) {
        Map<String, Object> result = new TreeMap<>();
        boolean flag = consumptionService.check(cid);
        result.put(Default.HTTP_RESULT,flag);
        return result;
    }


    //以下是管理信息系统作业新增代码
    @RequestMapping("/analysis")
    public String analysis(Model model, @SessionAttribute("userID") int vid) {
//        return venueService.analysis(model,vid);
        model.addAttribute("venueAnalysis",venueService.analysis(model,vid));
        return "venue/analysis";
    }

    @RequestMapping("/memberNumber")
    @ResponseBody
    public Map<String, Object> memberNumber(@SessionAttribute("userID") int vid) {
        return venueService.memberNumber(vid);
    }

    @RequestMapping("/profitChange")
    @ResponseBody
    public Map<String, Object> profitChange(@SessionAttribute("userID") int vid) {
        return venueService.profitChange(vid);
    }

    @RequestMapping("/unitPriceChange")
    @ResponseBody
    public Map<String, Object> unitPriceChange(@SessionAttribute("userID") int vid) {
        return venueService.unitPriceChange(vid);
    }

    @RequestMapping("/profitDistribution")
    @ResponseBody
    public Map<String, Object> profitDistribution(@SessionAttribute("userID") int vid) {
        return venueService.profitDistribution(vid);
    }

    @RequestMapping("/activityDistribution")
    @ResponseBody
    public Map<String, Object> activityDistribution(@SessionAttribute("userID") int vid) {
        return venueService.activityDistribution(vid);
    }

}
