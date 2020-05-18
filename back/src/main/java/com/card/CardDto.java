package com.card;

public class CardDto {

    protected long id;
    private String name;
    private String content;

    public CardDto() {}

    public CardDto(String name, String content) {
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

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
}