package cn.tickets.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "seat", schema = "tickets", catalog = "")
public class SeatEntity {
    private int cid;
    private int vid;
    private int isfront;
    private int seatnumber;
    private Timestamp orderdate;
    private int isbooked;
    private int sid;
    private int pid;

    public SeatEntity() {
    }

    public SeatEntity(int cid, int vid, int isfront, int seatnumber, Timestamp orderdate, int isbooked,int pid) {
        this.cid = cid;
        this.vid = vid;
        this.isfront = isfront;
        this.seatnumber = seatnumber;
        this.orderdate = orderdate;
        this.isbooked = isbooked;
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @GeneratedValue
    @Id
    @Column(name = "sid",nullable = false)
    public int getSid(){return sid;}

    public void setSid(int sid){this.sid = sid;}

    @Basic
    @Column(name = "cid",nullable = false)
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
    @Basic
    @Column(name = "vid",nullable = false)
    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    @Basic
    @Column(name = "isfront",nullable = false)
    public int getIsfront() {
        return isfront;
    }

    public void setIsfront(int isfront) {
        this.isfront = isfront;
    }

    @Basic
    @Column(name = "seatnumber",nullable = false)
    public int getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    @Basic
    @Column(name = "orderdate",nullable = false)
    public Timestamp getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Timestamp orderdate) {
        this.orderdate = orderdate;
    }

    @Basic
    @Column(name = "isbooked",nullable = false)
    public int getIsbooked() {
        return isbooked;
    }

    public void setIsbooked(int isbooked) {
        this.isbooked = isbooked;
    }

    @Override
    public String toString() {
        return "SeatEntity{" +
                "cid=" + cid +
                ", vid=" + vid +
                ", isfront=" + isfront +
                ", seatnumber=" + seatnumber +
                ", orderdate=" + orderdate +
                ", isbooked=" + isbooked +
                ", sid=" + sid +
                ", pid=" + pid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatEntity that = (SeatEntity) o;
        return cid == that.cid &&
                vid == that.vid &&
                isfront == that.isfront &&
                seatnumber == that.seatnumber &&
                isbooked == that.isbooked &&
                sid == that.sid &&
                pid == that.pid &&
                Objects.equals(orderdate, that.orderdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cid, vid, isfront, seatnumber, orderdate, isbooked, sid, pid);
    }

}
