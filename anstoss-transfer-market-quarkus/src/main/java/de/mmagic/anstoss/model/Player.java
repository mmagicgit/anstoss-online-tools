package de.mmagic.anstoss.model;

import java.util.List;
import java.util.Map;

public class Player {

    @SuppressWarnings("unused")
    //used by database
    public Player() {
    }

    public Player(Integer id, String name, Integer age, Double strength, String position, String country, Long price, Integer days, Map<String, List<Integer>> aaw) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.strength = strength;
        this.position = position;
        this.country = country;
        this.price = price;
        this.days = days;
        this.aaw = aaw;
    }

    public Integer id;
    public String name;
    public Integer age;
    public Double strength;
    public String position;
    public String country;
    public Long price;
    public Integer days;
    public Map<String, List<Integer>> aaw;

}
