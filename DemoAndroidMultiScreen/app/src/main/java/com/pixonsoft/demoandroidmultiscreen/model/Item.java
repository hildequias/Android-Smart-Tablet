package com.pixonsoft.demoandroidmultiscreen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobile6 on 1/19/16.
 */
public class Item {

    private String name;
    private String version;

    public Item(){

    }

    public Item(String name, String version){
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString (){
        return name;
    }

    public static List<Item> getItens(){
        List<Item> itens = new ArrayList<>();

        itens.add(new Item("Kit Kat","Versão 4"));
        itens.add(new Item("Lollipop","Versão 5"));
        itens.add(new Item("Marshmellow","Versão 6"));

        return itens;
    }
}
