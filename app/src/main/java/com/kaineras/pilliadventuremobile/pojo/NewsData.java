package com.kaineras.pilliadventuremobile.pojo;

/**
 * Created the first version by kaineras on 19/03/15.
 */
public class NewsData {
    private String name;
    private String date;
    private String entry;
    private String image;

    public NewsData(String name,String dateEntry,String entry,String image){
        setName(name);
        setImage(image);
        setDate(dateEntry);
        setEntry(entry);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString(){
        return getName()+":"+getImage();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
