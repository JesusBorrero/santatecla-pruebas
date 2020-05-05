package com.slide;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The slide ID. It is unique", required = true)
    protected long id;

    @ApiModelProperty(notes = "The name of the slide", required = true)
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    @ApiModelProperty(notes = "The content of the slide. It is written with AsciiDoc", required = true)
    private String content;

    public Slide() {}

    public Slide(String name) {
        this();
        this.name = name;
        this.content = "=== " + name + "\n";
    }

    public Slide(String name, String content) {
        this();
        this.name = name;
        this.content = content;
    }

    public void update(Slide slide) {
        setName(slide.getName());
        setContent(slide.getContent());
    }

    public void addContent(String content) {
        this.content = this.content + content;
    }

    /********************
     * GETTER AND SETTER *
     ********************/

    public long getId() {
        return id;
    }

    public String getName() { return name; }

    public String getContent() { return content; }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) { this.name = name; }

    public void setContent(String content) { this.content = content; }

}