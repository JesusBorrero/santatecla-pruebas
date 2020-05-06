package com.card;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The card ID. It is unique.",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The card name.",  required = true)
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    @ApiModelProperty(notes = "The content of the card. It is in AsciiDoc format.",  required = true)
    private String content;

    public Card() {}

    public Card(String name, String content) {
        this();
        this.name = name;
        this.content = content;
    }

    public void update(Card card) {
        if (StringUtils.isNotBlank(card.getName())) {
            this.name = card.getName();
        }
        if (StringUtils.isNotBlank(card.getContent())) {
            this.content = card.getContent();
        }
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