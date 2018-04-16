package cn.tickets.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "venue", schema = "tickets", catalog = "")
public class VenueEntity implements Serializable {
    private int id;//场馆号
    private String location;//场馆地点
    private int fsnumber;//前排座位数量
    private int bsnumber;//后排座位数量
    private int approve;//注册后不批准是0，注册后批准，但是信息未修改是1，信息修改后是2
    private String name;//场馆名字
    private String systemid;//系统自动分配的id


    public VenueEntity() {

    }

    public VenueEntity(String location, int fsnumber, int bsnumber,  int approve, String name,String systemid) {
        this.location = location;
        this.fsnumber = fsnumber;
        this.bsnumber = bsnumber;
        this.approve = approve;
        this.name = name;
        this.systemid = systemid;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "location", nullable = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "fsnumber", nullable = false)
    public int getFsnumber() {
        return fsnumber;
    }

    public void setFsnumber(int fsnumber) {
        this.fsnumber = fsnumber;
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
    @Column(name = "approve",nullable = false)
    public int getApprove(){
        return approve;
    }

    public void setApprove(int approve){
        this.approve = approve;
    }

    @Basic
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemid() {
        return systemid;
    }

    public void setSystemid(String systemid) {
        this.systemid = systemid;
    }

    @Override
    public String toString() {
        return "VenueEntity{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", fsnumber=" + fsnumber +
                ", bsnumber=" + bsnumber +

                ", approve=" + approve +
                ", name='" + name + '\'' +
                ", systemid='" + systemid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VenueEntity that = (VenueEntity) o;
        return id == that.id &&
                fsnumber == that.fsnumber &&
                bsnumber == that.bsnumber &&

                approve == that.approve &&
                Objects.equals(location, that.location) &&
                Objects.equals(name, that.name) &&
                Objects.equals(systemid, that.systemid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, location, fsnumber, bsnumber, approve, name, systemid);
    }

}
