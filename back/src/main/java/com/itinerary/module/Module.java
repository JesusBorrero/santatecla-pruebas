package com.itinerary.module;

import com.google.gson.annotations.SerializedName;
import com.itinerary.block.Block;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module extends Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("moduleId")
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    private List<Block> blocks;

    public Module(){
        this.blocks = new ArrayList<>();
    }

    public Module(String name) {
        super(name);
        this.blocks = new ArrayList<>();
    }

    public void addBlock(Block block) {
        this.blocks.add(block);
        block.getParentsId().add(this.id);
    }

    public void update(Module module) {
        this.name = module.name;
    }

    /********************
     * GETTER AND SETTER *
     ********************/

    public long getId() {
        return id;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
