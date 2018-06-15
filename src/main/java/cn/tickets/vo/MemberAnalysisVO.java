package cn.tickets.vo;

public class MemberAnalysisVO {
    private String averagePerMonth;
    private String averagePerYear;
    private String mostActivity;
    private String mostVenue;
    private String refundRatio;
    private String orderRatio;

    public String getAveragePerMonth() {
        return averagePerMonth;
    }

    public void setAveragePerMonth(String averagePerMonth) {
        this.averagePerMonth = averagePerMonth;
    }

    public String getAveragePerYear() {
        return averagePerYear;
    }

    public void setAveragePerYear(String averagePerYear) {
        this.averagePerYear = averagePerYear;
    }

    public String getMostActivity() {
        return mostActivity;
    }

    public void setMostActivity(String mostActivity) {
        this.mostActivity = mostActivity;
    }

    public String getMostVenue() {
        return mostVenue;
    }

    public void setMostVenue(String mostVenue) {
        this.mostVenue = mostVenue;
    }

    public String getRefundRatio() {
        return refundRatio;
    }

    public void setRefundRatio(String refundRatio) {
        this.refundRatio = refundRatio;
    }

    public String getOrderRatio() {
        return orderRatio;
    }

    public void setOrderRatio(String orderRatio) {
        this.orderRatio = orderRatio;
    }
}
