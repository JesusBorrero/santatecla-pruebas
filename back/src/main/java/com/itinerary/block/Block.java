package com.itinerary.block;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("blockId")
    @ApiModelProperty(notes = "The block ID. It is unique",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The block name", required = true)
    protected String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @ApiModelProperty(notes = "A set with the ids of the block parents", required = true)
    protected Set<Long> parentsId;

    public Block() {
        this.parentsId = new HashSet<>();
    }

    public Block(String name){
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

    public Set<Long> getParentsId() {
        return parentsId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentsId(Set<Long> parentsId) {
        this.parentsId = parentsId;
    }

}
