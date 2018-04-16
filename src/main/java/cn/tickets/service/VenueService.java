package cn.tickets.service;

import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.VenueEntity;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VenueService {

    Map<String,Object> updateVenueInfo(int vid, String location, int fsnumber, int bsnumber,String name);//更新场馆信息



    String statistics(Model model, int vid);




}
