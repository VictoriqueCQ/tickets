package cn.tickets.vo;

import java.util.List;

public class MemberStatisticsVO {
    private List<BookOrderVO> bookOrderVOS;
    private List<UnsubscribeOrderVO> unsubscribeOrderVOS;
    private List<CompleteOrderVO> completeOrderVOS;

    public List<BookOrderVO> getBookOrderVOS() {
        return bookOrderVOS;
    }

    public void setBookOrderVOS(List<BookOrderVO> bookOrderVOS) {
        this.bookOrderVOS = bookOrderVOS;
    }

    public List<UnsubscribeOrderVO> getUnsubscribeOrderVOS() {
        return unsubscribeOrderVOS;
    }

    public void setUnsubscribeOrderVOS(List<UnsubscribeOrderVO> unsubscribeOrderVOS) {
        this.unsubscribeOrderVOS = unsubscribeOrderVOS;
    }

    public List<CompleteOrderVO> getCompleteOrderVOS() {
        return completeOrderVOS;
    }

    public void setCompleteOrderVOS(List<CompleteOrderVO> completeOrderVOS) {
        this.completeOrderVOS = completeOrderVOS;
    }
}
