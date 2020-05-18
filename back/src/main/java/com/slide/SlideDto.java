package com.slide;

public class SlideDto {

    protected long id;
    private String name;
    private String content;

    public SlideDto() {}

    public SlideDto(String name) {
        this();
        this.name = name;
        this.content = "=== " + name + "\n";
    }

    public SlideDto(String name, String content) {
        this();
        this.name = name;
        this.content = content;
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