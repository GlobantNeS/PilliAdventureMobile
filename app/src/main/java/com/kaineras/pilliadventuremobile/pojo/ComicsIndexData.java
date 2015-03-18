package com.kaineras.pilliadventuremobile.pojo;

/**
 * Created the first version by kaineras on 3/02/15.
 */
public class ComicsIndexData {
    private String name;
    private String urlImage;
    private String urlDest;

    public ComicsIndexData(String name, String urlImage, String urlDest) {
        this.setUrlImage(urlImage);
        this.setUrlDest(urlDest);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlDest() {
        return urlDest;
    }

    public void setUrlDest(String urlDest) {
        this.urlDest = urlDest;
    }
}
