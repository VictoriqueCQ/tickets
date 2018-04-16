package cn.tickets.vo;

public class VenueInfoVO {
    private int vid;//场馆号
    private String location;//场馆地点
    private int fsnumber;//前排座位数量
    private int bsnumber;//后排座位数量

    private int approve;//注册后不批准是0，注册后批准，但是信息未修改是1，信息修改后是2
    private String name;//场馆名字
    private String systemid;



    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFsnumber() {
        return fsnumber;
    }

    public void setFsnumber(int fsnumber) {
        this.fsnumber = fsnumber;
    }

    public int getBsnumber() {
        return bsnumber;
    }

    public void setBsnumber(int bsnumber) {
        this.bsnumber = bsnumber;
    }

    public int getApprove() {
        return approve;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemid(){return systemid;}

    public void setSystemid(String systemid){this.systemid = systemid;}
}
