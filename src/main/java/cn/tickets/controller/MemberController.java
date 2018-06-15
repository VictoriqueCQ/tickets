package cn.tickets.controller;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;

import cn.tickets.entity.SeatEntity;
import cn.tickets.service.*;
import cn.tickets.util.Default;
import cn.tickets.vo.BackSeatVO;
import cn.tickets.vo.FrontSeatVO;
import com.alibaba.fastjson.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    private static int pid;
    private MemberService memberService;
    private ConsumptionService consumptionService;
    private PlanService planService;
    private SeatService seatService;
    private static Locale locale = new Locale("zh");

    @Autowired
    public MemberController(MemberService memberService, ConsumptionService consumptionService, PlanService planService, SeatService seatService) {
        this.memberService = memberService;
        this.consumptionService = consumptionService;
        this.planService = planService;
        this.seatService = seatService;
    }

    //index页面
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("miplan", planService.allMemberIndexPlan());
//        System.err.println(planService.allMemberIndexPlan().toString());
        return "member/index";
    }

    //profile页面
    @RequestMapping("/profile")
    public String profile(@SessionAttribute(Default.USER_ID) int id, Model model) {

        model.addAttribute("member", memberService.getMemberProfile(id));

        return "member/profile";
    }

    @RequestMapping("/coupon")
    public String coupon(@SessionAttribute(Default.USER_ID) int id, Model model) {
        model.addAttribute("coupon", memberService.getMemberProfile(id));

        return "member/coupon";
    }

    @RequestMapping("/statistics")
    public String statistics(Model model, @SessionAttribute("userID") int mid) {
        model.addAttribute("locale", locale);
        return memberService.statistics(model, mid);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(@SessionAttribute(Default.USER_ID) int id, String name, String email, String phone) {
        System.err.println("edit:" + id + " " + name + " " + email + " " + phone);
        return memberService.edit(id, name, email, phone);
    }

    @PostMapping("/stop")
    @ResponseBody
    public Map<String, Object> stop(@SessionAttribute(Default.USER_ID) int id, String password) {
        Map<String, Object> result = memberService.deleteAccount(id, password);

        if ((Boolean) result.get(Default.HTTP_RESULT)) {

            return result;
        }
        return result;
    }

    @RequestMapping("../register")
    public String register() {
        return "redirect:/register";
    }




    private boolean updatePredefine(int predefine, int cid) {
        return consumptionService.updatePredefine(predefine, cid);
    }

    @RequestMapping(value = "/reducePoint", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> reducePoint(@SessionAttribute(Default.USER_ID) int id, int point) {
        return memberService.reducePoint(id, point);
    }

    @RequestMapping("/buy")
    @ResponseBody
    public Map<String, Object> buy(@SessionAttribute(Default.USER_ID) int id, int cid, int ap, int isbought) {
        return consumptionService.buy(id, cid, ap, isbought);
    }

    @RequestMapping("/us")//退订
    @ResponseBody
    public Map<String, Object> us(int cid, int isus) {
        Map<String, Object> result = new TreeMap<>();
        boolean flag = false;
        boolean flag2 = false;
        if (isus == 1) {
            //把票设为退订状态
            flag = updatePredefine(0, cid);

            //把钱退回，把资金退回，把座位还给场馆，把座位设为为预订
            flag2 = memberService.unSubscribe(cid);


        }
        if (flag&&flag2) {
            result.put("number", 1);
            result.put(Default.HTTP_RESULT, true);
        }
        return result;
    }


    @RequestMapping("/buyfirstbutton")
    @ResponseBody
    public Map<String, Object> buyfirst(int pid) {
        Map<String, Object> result = new TreeMap<>();
        this.pid = pid;
        System.err.println("pid:" + pid);

        result.put(Default.HTTP_RESULT, true);

        return result;
    }

    @RequestMapping("/buyfirst")
    public String buyfirst(Model model) {
        List<SeatEntity> seatEntities = seatService.getSeat(pid, 0);
//        System.err.println(seatEntities.toString());
        List<SeatEntity> frontSeat = new ArrayList<>();
        List<SeatEntity> backSeat = new ArrayList<>();
        for (SeatEntity seatEntity : seatEntities) {
            if (seatEntity.getIsfront() == 1) {
                frontSeat.add(seatEntity);
            } else {
                backSeat.add(seatEntity);
            }
        }
        model.addAttribute("frontseat",frontSeat);
        model.addAttribute("backseat",backSeat);
        return "member/buyfirst";
    }

    @RequestMapping("/buysecond")
    public String buysecond() {
        return "member/buysecond";
    }

    @RequestMapping("/orderNow")
    @ResponseBody
    public Map<String, Object> order(@SessionAttribute(Default.USER_ID) int id, int pid, int fnumber, int bnumber) {
        System.err.println("buyNow:" + id + " " + pid + " " + fnumber + " " + bnumber);
        consumptionService.orderNow(id, pid, fnumber, bnumber);
        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }


    @RequestMapping("/orderSelect")
    @ResponseBody
    public Map<String,Object> orderSelect(@SessionAttribute(Default.USER_ID)int id,String frontselect,String backselect){


        List<FrontSeatVO> frontSeatVOS = JSONArray.parseArray(frontselect,FrontSeatVO.class);
        List<BackSeatVO> backSeatVOS = JSONArray.parseArray(backselect,BackSeatVO.class);

        List<Integer> frontSeatList = new ArrayList<>();
        List<Integer> backSeatList = new ArrayList<>();
        for(FrontSeatVO frontSeatVO:frontSeatVOS){
            frontSeatList.add(frontSeatVO.getFid());
        }
        for(BackSeatVO backSeatVO:backSeatVOS){
            backSeatList.add(backSeatVO.getBid());
        }


        //增加订单
        int cid = consumptionService.orderNow(id,pid,frontSeatList.size(),backSeatList.size());
        //把座位设置成已购买

        seatService.setSeatOrdered(cid,pid,frontSeatList,backSeatList);
        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }



    //以下是管理信息系统作业新增代码
    @RequestMapping("/analysis")
    public String analysis(Model model, @SessionAttribute("userID") int mid) {
//        model.addAttribute("member", memberService.getMemberProfile(id));
//
//        return "member/profile";
//        return memberService.analysis(model,mid);
        model.addAttribute("memberAnalysis",memberService.analysis(model,mid));
        return "member/analysis";
    }

    @RequestMapping("/activityDistribution")
    @ResponseBody
    public Map<String,Object> activityDistribution(){


        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @RequestMapping("/consumptionDistribution")
    @ResponseBody
    public Map<String,Object> consumptionDistribution(){


        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

    @RequestMapping("/venueDistribution")
    @ResponseBody
    public Map<String,Object> venueDistribution(){


        Map<String, Object> result = new TreeMap<>();
        result.put(Default.HTTP_RESULT, true);
        return result;
    }

}
