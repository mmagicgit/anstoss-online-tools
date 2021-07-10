package de.mmagic.anstoss.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.List;
import java.util.Map;

@BsonDiscriminator
public class Player {

    @SuppressWarnings("unused")
    //used by database
    public Player() {
    }

    public Player(Integer id, String name, Integer age, Double strength, String position, String country, Long price, Integer days, Map<String, List<Integer>> aaw, Long importedAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.strength = strength;
        this.position = position;
        this.country = country;
        this.price = price;
        this.days = days;
        this.aaw = aaw;
        this.importedAt = importedAt;
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
    public Long importedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Map<String, List<Integer>> getAaw() {
        return aaw;
    }

    public void setAaw(Map<String, List<Integer>> aaw) {
        this.aaw = aaw;
    }

    public Long getImportedAt() {
        return importedAt;
    }

    public void setImportedAt(Long importedAt) {
        this.importedAt = importedAt;
    }
}
