package cn.tickets.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "account", schema = "tickets", catalog = "")
public class AccountEntity implements Serializable{
    private int id;
    private int type;//0是会员，1是场馆
    private String email;
    private String password;

    public AccountEntity() {
    }

    public AccountEntity(String email, String password, int type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    @GeneratedValue
    @Id
    @Column(name = "id",nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type",nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "email",nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return id == that.id &&
                type == that.type &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, email, password);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
