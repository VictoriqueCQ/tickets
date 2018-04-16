package cn.tickets.vo;

public class BackSeatVO {
    private int bid;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "BackSeatVO{" +
                "bid=" + bid +
                '}';
    }
}
