package cn.tickets.vo;

public class MemberSituationVO {
    private String time;
    private String refund;
    private int priceSum;
    private int average;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public int getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(int priceSum) {
        this.priceSum = priceSum;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "MemberSituationVO{" +
                "time='" + time + '\'' +
                ", refund='" + refund + '\'' +
                ", priceSum=" + priceSum +
                ", average=" + average +
                '}';
    }
}
