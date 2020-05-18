package com.itinerary.module;

import com.itinerary.block.BlockDto;

import java.util.ArrayList;
import java.util.List;

public class ModuleDto extends BlockDto {

    protected long id;
    private List<BlockDto> blocks;

    public ModuleDto(){
        this.blocks = new ArrayList<>();
    }

    public ModuleDto(String name) {
        super(name);
        this.blocks = new ArrayList<>();
    }

    /********************
     * GETTER AND SETTER *
     ********************/

    @Override
    public long getId() {
        return id;
    }

    public List<BlockDto> getBlocks() {
        return blocks;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public void setBlocks(List<BlockDto> blocks) {
        this.blocks = blocks;
    }
}
