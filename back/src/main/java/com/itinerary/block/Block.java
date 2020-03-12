package com.itinerary.block;

import com.google.gson.annotations.SerializedName;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("blockId")
    protected long id;

    protected String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    protected List<Long> parentsId;

    public Block() {
        this.parentsId = new ArrayList<>();
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

    public List<Long> getParentsId() {
        return parentsId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentsId(List<Long> parentsId) {
        this.parentsId = parentsId;
    }

}
