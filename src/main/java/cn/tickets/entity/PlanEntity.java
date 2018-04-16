package cn.tickets.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "plan", schema = "tickets", catalog = "")
public class PlanEntity implements Serializable {
    private int pid;//计划号
    private int vid;//场馆号
    private Timestamp date;//计划活动日期
    private int fprice;//前排座位价格
    private int bprice;//后排座位价格
    private String type;//活动类型
    private String description;//活动描述


    public PlanEntity(){}

    public PlanEntity(int vid, Timestamp date, int fprice, int bprice, String type, String description) {
        this.vid = vid;
        this.date = date;
        this.fprice = fprice;
        this.bprice = bprice;
        this.type = type;
        this.description = description;
    }

    @GeneratedValue
    @Id
    @Column(name = "pid", nullable = false)
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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
    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanEntity that = (PlanEntity) o;
        return pid == that.pid &&
                vid == that.vid &&
                fprice == that.fprice &&
                bprice == that.bprice &&
                Objects.equals(date, that.date) &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, vid, date, fprice, bprice, type, description);
    }

    @Override
    public String toString() {
        return "PlanEntity{" +
                "pid=" + pid +
                ", vid=" + vid +
                ", date=" + date +
                ", fprice=" + fprice +
                ", bprice=" + bprice +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
