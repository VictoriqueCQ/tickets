package cn.tickets.service;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.vo.MemberInfoVO;

import java.util.List;
import java.util.Map;

public interface ManagerService {


    Map<String,Object> getMemberStatus();

    Map<String,Object> getFinanceStatus();

    boolean approve(int vid,int approve);

    List<VenueEntity> getAllOpenApplication();

    List<VenueEntity> getAllModifyApplication();

    Map<String,Object> getSettlement();
}
