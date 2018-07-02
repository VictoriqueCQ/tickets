package cn.tickets.vo;

public class MemberOrderAnalysisVO {
    private String refund;
    private int average;
    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "MemberOrderAnalysisVO{" +
                "refund='" + refund + '\'' +
                ", average=" + average +
                ", month='" + month + '\'' +
                '}';
    }
}
