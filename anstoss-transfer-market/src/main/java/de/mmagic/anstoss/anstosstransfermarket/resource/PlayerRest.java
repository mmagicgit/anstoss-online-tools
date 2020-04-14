package de.mmagic.anstoss.anstosstransfermarket.resource;

public class PlayerRest {

    public PlayerRest(Integer id, String name, Integer age, Double strength, String position, String country, Long price, Integer days) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.strength = strength;
        this.position = position;
        this.country = country;
        this.price = price;
        this.days = days;
    }

    public Integer id;
    public String name;
    public Integer age;
    public Double strength;
    public String position;
    public String country;
    public Long price;
    public Integer days;

}
