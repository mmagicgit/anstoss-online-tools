package de.mmagic.anstoss.anstosstransfermarket.model;

import de.mmagic.anstoss.anstosstransfermarket.store.PlayerStore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = PlayerStore.COLLECTION_NAME)
public class Player {

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
