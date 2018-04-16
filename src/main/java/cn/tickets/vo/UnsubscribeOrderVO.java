package cn.tickets.vo;

import java.util.Date;

public class UnsubscribeOrderVO {
    private int cid;
    private String venueName;
    private String type;
    private Date date;
    private int fsnumber;
    private int fprice;
    private int bsnumber;
    private int bprice;
    private int aprice;
    private int predefine;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFsnumber() {
        return fsnumber;
    }

    public void setFsnumber(int fsnumber) {
        this.fsnumber = fsnumber;
    }

    public int getFprice() {
        return fprice;
    }

    public void setFprice(int fprice) {
        this.fprice = fprice;
    }

    public int getBsnumber() {
        return bsnumber;
    }

    public void setBsnumber(int bsnumber) {
        this.bsnumber = bsnumber;
    }

    public int getBprice() {
        return bprice;
    }

    public void setBprice(int bprice) {
        this.bprice = bprice;
    }

    public int getAprice() {
        return aprice;
    }

    public void setAprice(int aprice) {
        this.aprice = aprice;
    }

    public int getPredefine() {
        return predefine;
    }

    public void setPredefine(int predefine) {
        this.predefine = predefine;
    }

    @Override
    public String toString() {
        return "UnsubscribeOrderVO{" +
                "cid=" + cid +
                ", venueName='" + venueName + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", fsnumber=" + fsnumber +
                ", fprice=" + fprice +
                ", bsnumber=" + bsnumber +
                ", bprice=" + bprice +
                ", aprice=" + aprice +
                ", predefine=" + predefine +
                '}';
    }
}
