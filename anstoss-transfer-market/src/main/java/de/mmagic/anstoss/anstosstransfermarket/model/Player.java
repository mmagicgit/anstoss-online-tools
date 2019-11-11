package de.mmagic.anstoss.anstosstransfermarket.model;

public class Player {

    public Player(Integer id, String name, Integer age, Double strength, String position, String country, Long price, Integer days) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.strength = strength;
        this.position = position;
        this.country = country;
        this.price = price;
        this.days = days;
    }

    public final Integer id;
    public final String name;
    public final Integer age;
    public final Double strength;
    public final String position;
    public final String country;
    public final Long price;
    public final Integer days;

}
