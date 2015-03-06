package com.kaineras.pilliadventuremobile.pojo;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created the first version by kaineras on 9/02/15.
 */
public class EnglishImagesProperties {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESC = "desc";

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = DESC)
    private String desc;

    EnglishImagesProperties() {

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
