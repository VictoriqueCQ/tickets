package cn.tickets.service;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.vo.ManagementAnalysisVO;
import cn.tickets.vo.MemberInfoVO;
import cn.tickets.vo.MemberOrderAnalysisVO;
import cn.tickets.vo.MemberSituationVO;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface ManagerService {


    Map<String,Object> getMemberStatus();

    Map<String,Object> getFinanceStatus();

    boolean approve(int vid,int approve);

    List<VenueEntity> getAllOpenApplication();

    List<VenueEntity> getAllModifyApplication();

    Map<String,Object> getSettlement();

    //以下是管理信息系统新增代码
    ManagementAnalysisVO analysis(Model model);
    Map<String,Object>venueProfitRatio();
    Map<String,Object>activityProfitRatio();
    Map<String,Object>profitChange();
    Map<String,Object>activeMemberNumber();
    Map<String,Object>activeVenueNumber();
    Map<String,Object>orderMonth();
    Map<String,Object>activityMonth();
    List<MemberOrderAnalysisVO> memberOrder(Model model);
    List<String> venueDetails(Model model);
    List<String> memberDetails(Model model);
    Map<String,Object> venueProfitChange(String venueName);
    List<MemberSituationVO>memberSituation(String memberName);
    String memberLevel(String memberName);
    String memberAllRefund(String memberName);
    Map<String,Object> memberActivityDistribution(String memberName);

}
