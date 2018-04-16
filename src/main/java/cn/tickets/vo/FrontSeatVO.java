package cn.tickets.vo;

public class FrontSeatVO {
    private int fid;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @Override
    public String toString() {
        return "FrontSeatVO{" +
                "fid=" + fid +
                '}';
    }
}
