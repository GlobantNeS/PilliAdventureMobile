package com.kaineras.pilliadventuremobile.pojo;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created the first version by kaineras on 9/02/15.
 */
public class ImagesProperties {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LANG = "lang";
    private static final String EXIST = "exist";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = LANG)
    private String lang;
    @DatabaseField(columnName = EXIST)
    private String exist;

    public ImagesProperties() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getExist() {
        return exist;
    }

    public void setExist(String exist) {
        this.exist = exist;
    }
}
