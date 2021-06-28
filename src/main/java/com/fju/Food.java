package com.fju;

/**
 * 餐點基礎類
 * @author User
 *
 */
public class Food {

    int expireHour;
    int price;
    String name;
    int discount;

    public int getExpireHour() {
        return expireHour;
    }
    public void setExpireHour(int expireHour) {
        this.expireHour = expireHour;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }


    public static class ButtonEditor {
    }
}
