package com.ring.exercice.helpers;

/**
 * Created by Vladislav Kulasov on 28.01.2018.
 */
public enum ReferenceRover {
    CURIOSITY("curiosity");

    private String name;

    ReferenceRover(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
