package cn.tickets.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "member", schema = "tickets", catalog = "")
public class MemberEntity implements Serializable {
    private int id;//会员号
    private int qualification;//会员资格
    private int level;//会员级别
    private String email;//邮箱
    private String phone;//手机号
    private String name;//用户名
    private int state = 0;//注册状态， 默认0.验证邮箱后变1
    private int point;//积分,预定1000积分升一级，每次积分变动时调整level
    private int discount = 5;//打折比例，默认为5*level
    private int money;

    public MemberEntity() {

    }

    public MemberEntity(int qualification, int level, String email, String password, String phone, String name, int state, int point, int discount,int money) {
        this.qualification = qualification;
        this.level = level;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.state = state;
        this.point = point;
        this.discount = discount;
        this.money = money;
    }

    @Basic
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "qualification", nullable = false)
    public int getQualification() {
        return qualification;
    }

    public void setQualification(int qualification) {
        this.qualification = qualification;
    }

    @Basic
    @Column(name = "level", nullable = false)
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone", nullable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "state", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "point", nullable = false)
    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Basic
    @Column(name = "discount", nullable = false)
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", qualification=" + qualification +
                ", level=" + level +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", point=" + point +
                ", discount=" + discount +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberEntity that = (MemberEntity) o;
        return id == that.id &&
                qualification == that.qualification &&
                level == that.level &&
                state == that.state &&
                point == that.point &&
                discount == that.discount &&
                money == that.money &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, qualification, level, email, phone, name, state, point, discount, money);
    }
}
