package com.itinerary.block;

import java.util.HashSet;
import java.util.Set;

public class BlockDto {

    protected long id;
    protected String name;
    protected Set<Long> parentsId;

    public BlockDto() {
        this.parentsId = new HashSet<>();
    }

    public BlockDto(String name){
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
