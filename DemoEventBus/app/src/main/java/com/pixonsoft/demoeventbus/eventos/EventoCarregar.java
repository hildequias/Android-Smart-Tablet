package com.pixonsoft.demoeventbus.eventos;

/**
 * Created by mobile6 on 12/10/15.
 */
public class EventoCarregar {

    private String data;

    public EventoCarregar(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }
}
