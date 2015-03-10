package com.kaineras.pilliadventuremobile.pojo;

/**
 * Created the first version by kaineras on 9/03/15.
 */
public class PageModel {

    private int index;
    private String text;

    public PageModel(int index) {
        this.index = index;
        setIndex(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        setText(index);
    }

    public String getText() {
        return text;
    }

    private void setText(int index) {
        this.text = String.format("Page %s", index);
    }
}
