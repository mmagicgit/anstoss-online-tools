package de.mmagic.anstoss;

import com.google.common.collect.ArrayListMultimap;

class Player {

    final String name;
    final ArrayListMultimap<String, Aaw> data = ArrayListMultimap.create();

    Player(String name) {
        this.name = name;
    }

}
