package com.kaineras.pilliadventuremobile.pojo;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class OptionsMenu {
    private String name;
    private String text;
    private int ico;

    public OptionsMenu(String text, String name, int ico) {
        this.setText(text);
        this.setName(name);
        this.setIco(ico);
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    public int getIco() {
        return ico;
    }

    void setIco(int ico) {
        this.ico = ico;
    }
}
