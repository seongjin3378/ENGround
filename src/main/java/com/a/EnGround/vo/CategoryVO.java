package com.a.EnGround.vo; 

public class CategoryVO { 
    private String categoryNo;  // 카테고리 번호
    private String categoryName;  // 카테고리 이름
    private String imagePath;  // 카테고리 이미지 경로 추가

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) { 
        this.categoryNo = categoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
