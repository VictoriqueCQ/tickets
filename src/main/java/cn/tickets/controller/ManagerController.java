package cn.tickets.controller;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.service.ConsumptionService;
import cn.tickets.service.ManagerService;
import cn.tickets.service.MemberService;
import cn.tickets.service.VenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    private ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;

    }


    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    @ResponseBody
    public boolean approve(int vid, int approve) {
        return managerService.approve(vid, approve);
    }


    @RequestMapping("/statistics")
    public String statistics() {
        return "manager/statistics";
    }

    @GetMapping("/financeStatus")
    @ResponseBody
    public Map<String, Object> getFinanceStatus() {
//        System.err.println(managerService.getFinanceStatus());
        return managerService.getFinanceStatus();
    }

    @GetMapping("/memberStatus")
    @ResponseBody
    public Map<String, Object> getMemberStatus() {
        return managerService.getMemberStatus();
    }


    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("opens", managerService.getAllOpenApplication());
        model.addAttribute("edits", managerService.getAllModifyApplication());
        return "manager/index";
    }

    @RequestMapping("/settlement")
    public String settlement() {
        return "manager/settlement";
    }

    @RequestMapping("/getsettlement")
    @ResponseBody
    public Map<String, Object> getSettlement() {
        return managerService.getSettlement();
    }

    //以下是管理信息系统作业新增代码
    @RequestMapping("/analysis")
    public String analysis(Model model) {
        model.addAttribute("managementAnalysis", managerService.analysis(model));
        return "manager/analysis";
    }

    @RequestMapping("/venueProfitRatio")
    @ResponseBody
    public Map<String, Object> venueProfitRatio() {
        return managerService.venueProfitRatio();
    }

    @RequestMapping("/activityProfitRatio")
    @ResponseBody
    public Map<String, Object> activityProfitRatio() {
        return managerService.activityProfitRatio();
    }

    @RequestMapping("/profitChange")
    @ResponseBody
    public Map<String, Object> profitChange() {
        return managerService.profitChange();
    }

    @RequestMapping("/activeMemberNumber")
    @ResponseBody
    public Map<String, Object> activeMemberNumber() {
        return managerService.activeMemberNumber();
    }

    @RequestMapping("/activeVenueNumber")
    @ResponseBody
    public Map<String, Object> activeVenueNumber() {
        return managerService.activeVenueNumber();
    }

    @RequestMapping("/orderMonth")
    @ResponseBody
    public Map<String, Object> orderMonth(){
        return managerService.orderMonth();
    }

    @RequestMapping("/activityMonth")
    @ResponseBody
    public Map<String,Object> activityMonth(){
        return managerService.activityMonth();
    }
}
