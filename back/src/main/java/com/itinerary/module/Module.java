package com.itinerary.module;

import com.google.gson.annotations.SerializedName;
import com.itinerary.block.Block;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module extends Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("moduleId")
    @ApiModelProperty(notes = "The module ID. It is unique", required = true)
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    @ApiModelProperty(notes = "The blocks inside the module", required = true)
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

    @Override
    public long getId() {
        return id;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
