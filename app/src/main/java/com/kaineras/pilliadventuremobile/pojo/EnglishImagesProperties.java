package com.kaineras.pilliadventuremobile.pojo;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created the first version by kaineras on 9/02/15.
 */
public class EnglishImagesProperties {

    private final static String ID = "_id";
    private final static String NAME = "name";
    private final static String DESC = "desc";

    @DatabaseField(generatedId = true, columnName = ID)
    private int _id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = DESC)
    private String desc;

    EnglishImagesProperties() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
