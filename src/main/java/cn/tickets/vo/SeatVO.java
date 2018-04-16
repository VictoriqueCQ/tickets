package cn.tickets.vo;

import java.util.Date;

public class SeatVO {
    private int cid;
    private int vid;
    private int isfront;
    private int seatnumber;
    private Date orderdate;
    private int isbooked;


    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getIsfront() {
        return isfront;
    }

    public void setIsfront(int isfront) {
        this.isfront = isfront;
    }

    public int getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public int getIsbooked() {
        return isbooked;
    }

    public void setIsbooked(int isbooked) {
        this.isbooked = isbooked;
    }


    @Override
    public String toString() {
        return "SeatVO{" +
                "cid=" + cid +
                ", vid=" + vid +
                ", isfront=" + isfront +
                ", seatnumber=" + seatnumber +
                ", orderdate=" + orderdate +
                ", isbooked=" + isbooked +
                '}';
    }
}
