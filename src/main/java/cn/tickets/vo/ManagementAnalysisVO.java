package cn.tickets.vo;

public class ManagementAnalysisVO {
    private String firstSR;
    private String secondSR;
    private String thirdSR;
    private String fourthSR;
    private String allProfitLastYear;

    public String getFirstSR() {
        return firstSR;
    }

    public void setFirstSR(String firstSR) {
        this.firstSR = firstSR;
    }

    public String getSecondSR() {
        return secondSR;
    }

    public void setSecondSR(String secondSR) {
        this.secondSR = secondSR;
    }

    public String getThirdSR() {
        return thirdSR;
    }

    public void setThirdSR(String thirdSR) {
        this.thirdSR = thirdSR;
    }

    public String getFourthSR() {
        return fourthSR;
    }

    public void setFourthSR(String fourthSR) {
        this.fourthSR = fourthSR;
    }

    public String getAllProfitLastYear() {
        return allProfitLastYear;
    }

    public void setAllProfitLastYear(String allProfitLastYear) {
        this.allProfitLastYear = allProfitLastYear;
    }

    @Override
    public String toString() {
        return "ManagementAnalysisVO{" +
                "firstSR='" + firstSR + '\'' +
                ", secondSR='" + secondSR + '\'' +
                ", thirdSR='" + thirdSR + '\'' +
                ", fourthSR='" + fourthSR + '\'' +
                ", allProfitLastYear=" + allProfitLastYear +
                '}';
    }
}
