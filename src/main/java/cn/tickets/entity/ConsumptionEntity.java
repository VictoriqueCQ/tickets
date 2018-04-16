package cn.tickets.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
@Entity
@Table(name = "consumption", schema = "tickets", catalog = "")
public class ConsumptionEntity implements Serializable {
    private int cid;//消费单号
    private int vid;//场馆号
    private int mid;//会员号
    private Timestamp date;//活动日期
    private int fsnumber;//前排座位预订数量
    private int bsnumber;//后排座位预订数量
    private int fprice;//前排座位价格
    private int bprice;//后排座位价格
    private int aprice;//总预订价格
    private int predefine;//订单预订状态，0为退订，1为预订，2为预订完成即活动开始
    private String type;//活动类型
    private Timestamp orderdate;//预订日期
    private int isbought;//是否已经付钱

    public ConsumptionEntity(){

    }

    public ConsumptionEntity(int vid, int mid, Timestamp date, int fsnumber, int bsnumber, int fprice, int bprice, int aprice, int predefine, String type, Timestamp orderdate,int isbought) {
        this.vid = vid;
        this.mid = mid;
        this.date = date;
        this.fsnumber = fsnumber;
        this.bsnumber = bsnumber;
        this.fprice = fprice;
        this.bprice = bprice;
        this.aprice = aprice;
        this.predefine = predefine;
        this.type = type;
        this.orderdate = orderdate;
        this.isbought = isbought;
    }
    @GeneratedValue
    @Id
    @Column(name = "cid", nullable = false)
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Basic
    @Column(name = "vid", nullable = false)
    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    @Basic
    @Column(name = "mid", nullable = false)
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "fsnumber", nullable = false)
    public int getFsnumber() {
        return fsnumber;
    }

    public void setFsnumber(int fnumber) {
        this.fsnumber = fnumber;
    }

    @Basic
    @Column(name = "bsnumber", nullable = false)
    public int getBsnumber() {
        return bsnumber;
    }

    public void setBsnumber(int bsnumber) {
        this.bsnumber = bsnumber;
    }

    @Basic
    @Column(name = "fprice", nullable = false)
    public int getFprice() {
        return fprice;
    }

    public void setFprice(int fprice) {
        this.fprice = fprice;
    }

    @Basic
    @Column(name = "bprice", nullable = false)
    public int getBprice() {
        return bprice;
    }

    public void setBprice(int bprice) {
        this.bprice = bprice;
    }

    @Basic
    @Column(name = "aprice", nullable = false)
    public int getAprice() {
        return aprice;
    }

    public void setAprice(int aprice) {
        this.aprice = aprice;
    }

    @Basic
    @Column(name = "predefine", nullable = false)
    public int getPredefine() {
        return predefine;
    }

    public void setPredefine(int predefine) {
        this.predefine = predefine;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "orderdate", nullable = false)
    public Timestamp getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Timestamp orderdate) {
        this.orderdate = orderdate;
    }

    @Basic
    @Column(name = "isbought",nullable = false)
    public int getIsbought() {
        return isbought;
    }

    public void setIsbought(int isbought) {
        this.isbought = isbought;
    }

    @Override
    public String toString() {
        return "ConsumptionEntity{" +
                "cid=" + cid +
                ", vid=" + vid +
                ", mid=" + mid +
                ", date=" + date +
                ", fsnumber=" + fsnumber +
                ", bsnumber=" + bsnumber +
                ", fprice=" + fprice +
                ", bprice=" + bprice +
                ", aprice=" + aprice +
                ", predefine=" + predefine +
                ", type='" + type + '\'' +
                ", orderdate=" + orderdate +
                ", isbought=" + isbought +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumptionEntity that = (ConsumptionEntity) o;
        return cid == that.cid &&
                vid == that.vid &&
                mid == that.mid &&
                fsnumber == that.fsnumber &&
                bsnumber == that.bsnumber &&
                fprice == that.fprice &&
                bprice == that.bprice &&
                aprice == that.aprice &&
                predefine == that.predefine &&
                isbought == that.isbought &&
                Objects.equals(date, that.date) &&
                Objects.equals(type, that.type) &&
                Objects.equals(orderdate, that.orderdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cid, vid, mid, date, fsnumber, bsnumber, fprice, bprice, aprice, predefine, type, orderdate, isbought);
    }

}
