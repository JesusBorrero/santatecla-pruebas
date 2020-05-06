package com.image;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The image ID. It is unique.",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The image name.",  required = true)
    private String name;

    @ApiModelProperty(notes = "The unit ID that the image belongs.",  required = true)
    private long unitId;

    @Lob
    @ApiModelProperty(notes = "The content of the image.",  required = true)
    private Byte[] image;

    public Image() {}

    public Image(String name) {
        this();
        this.name = name;
    }

    /********************
     * GETTER AND SETTER *
     ********************/

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }
}
