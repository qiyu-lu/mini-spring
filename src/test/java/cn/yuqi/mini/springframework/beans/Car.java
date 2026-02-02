package cn.yuqi.mini.springframework.beans;

public class Car {
    private String brand;
    public Car(){}
    public Car(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
