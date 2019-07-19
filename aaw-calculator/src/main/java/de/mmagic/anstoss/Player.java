package de.mmagic.anstoss;

import com.google.common.collect.ArrayListMultimap;

import java.math.BigDecimal;

class Player {

    final String name;
    final String position;
    BigDecimal strength;
    final ArrayListMultimap<String, Aaw> data = ArrayListMultimap.create();

    Player(String name, String position) {
        this.name = name;
        this.position = position;
    }

}
