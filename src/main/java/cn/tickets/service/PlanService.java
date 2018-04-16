package cn.tickets.service;

import cn.tickets.entity.PlanEntity;
import cn.tickets.vo.PlanVO;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlanService {

    String indexplan(Model model, int id);

    String scenePlan(Model model,int id);

    Map<String,Object> savePlan(int id, Timestamp time, String type, String description, int fprice, int bprice);

    List<PlanVO> allMemberIndexPlan();
}
