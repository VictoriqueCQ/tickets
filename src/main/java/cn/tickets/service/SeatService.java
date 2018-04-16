package cn.tickets.service;

import cn.tickets.entity.SeatEntity;

import java.util.List;

public interface SeatService {
    List<SeatEntity> getSeat(int pid, int isbooked);

    void setSeatOrdered(int cid,int vid,List<Integer> frontSeatList, List<Integer> backSeatList);

    boolean assignSeat(int cid, int vid, int fnumber,int bnumber);
}
