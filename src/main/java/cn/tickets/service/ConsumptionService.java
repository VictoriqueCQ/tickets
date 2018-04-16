package cn.tickets.service;

import cn.tickets.entity.ConsumptionEntity;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Map;

public interface ConsumptionService {

    boolean updatePredefine(int predefine, int cid);

    Map<String, Object> buy(int id, int cid, int ap, int isbought);

    int orderNow(int id, int pid, int fnumber, int bnumber);

    int sceneBuy(int vid,int mid,int pid,int fsnumber,int bsnumber);

    boolean check(int cid);
}
