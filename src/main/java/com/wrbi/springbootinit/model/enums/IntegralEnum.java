package com.wrbi.springbootinit.model.enums;

public enum IntegralEnum {

    TEN(100, 10),
    FIFTY(500, 45),
    HUNDRED(1000, 80);

    private Integer integralNum;
    private Integer price;

    private IntegralEnum(Integer integralNum, Integer price) {
        this.integralNum = integralNum;
        this.price = price;
    }

    public Integer getValue() {
        return integralNum;
    }
    public Integer getPrice() {
        return price;
    }

    public static boolean containsPrice(int price){
        for(IntegralEnum integralEnum : IntegralEnum.values()){
            if(integralEnum.getPrice().equals(price)){
                return true;
            }
        }
        return false;
    }

    public static Integer getIntegralNumByPrice(Integer price){
        for(IntegralEnum integralEnum : IntegralEnum.values()){
            if(integralEnum.getPrice().equals(price)){
                return integralEnum.getValue();
            }
        }
        return null;
    }
}
