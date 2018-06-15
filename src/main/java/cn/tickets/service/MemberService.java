package cn.tickets.service;

import cn.tickets.entity.ConsumptionEntity;
import cn.tickets.entity.MemberEntity;
import cn.tickets.vo.MemberInfoVO;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;


public interface MemberService {


    MemberInfoVO getMemberProfile(int mid);

    String statistics(Model model, int mid);

    Map<String, Object> edit(int id, String name, String mail, String phone);

    Map<String, Object> deleteAccount(int id, String password);

    Map<String, Object> reducePoint(int id,int Point);


    boolean unSubscribe(int cid);

    //以下是管理信息系统新增代码
    String analysis(Model model, int mid);

}