package com.a.EnGround.vo;

public class AdVO {
    private int adNo;
    private String imageUrl;
    private String targetUrl;

    // adNo에 대한 getter 및 setter 추가
    public int getAdNo() {
        return adNo;
    }

    public void setAdNo(int adNo) {
        this.adNo = adNo;
    }

    // 기존 imageUrl과 targetUrl에 대한 getter 및 setter
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
