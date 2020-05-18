package com.itinerary.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.GeneralRestController;

import com.itinerary.block.Block;
import com.itinerary.block.BlockDto;
import com.unit.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units")
public class UnitModuleRestController extends GeneralRestController implements UnitModuleController {

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{unitId}/modules")
    public ResponseEntity<Module> addModule(@RequestBody ModuleDto moduleDto, @PathVariable long unitId) {
        Optional<Unit> unit = this.unitService.findOne(unitId);

        Module module = convertToEntity(moduleDto);

        if (unit.isPresent()) {
            this.moduleService.save(module);
            unit.get().getModules().add(module);
            this.unitService.save(unit.get());

            return new ResponseEntity<>(module, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{unitId}/modules/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Module> deleteModule(@PathVariable long moduleId, @PathVariable long unitId) {

        Optional<Unit> unit = this.unitService.findOne(unitId);

        if (unit.isPresent()) {
            Optional<Module> module = this.moduleService.findOne(moduleId);
            if (module.isPresent()) {
                unit.get().getModules().remove(module.get());
                Set<Long> parents = module.get().getParentsId();
                if (parents != null) {
                    List<Long> idUsed = new ArrayList<>();
                    for (Long pId : parents) {
                        Optional<Module> m = this.moduleService.findOne(pId);
                        if (m.isPresent() && m.get().getBlocks().contains(module.get())) {
                            m.get().getBlocks().remove(module.get());
                            idUsed.add(pId);
                        }
                    }
                    parents.removeAll(idUsed);
                    List<Block> blocks = module.get().getBlocks();
                    if (blocks != null) {
                        for (Block b : blocks) {
                            Set<Long> iDs = b.getParentsId();
                            if (iDs != null) {
                                List<Long> iDsUsed = new ArrayList<>();
                                for (Long id : iDs) {
                                    if (id == moduleId) {
                                        iDsUsed.add(id);
                                    }
                                }
                                iDs.removeAll(iDsUsed);
                            }
                        }
                    }
                }
                this.blockService.delete(moduleId);
                return new ResponseEntity<>(module.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    private Module convertToEntity(ModuleDto dto) {
        Module module = modelMapper.map(dto, Module.class);
        module.setBlocks(dto.getBlocks().stream()
                .map(this::convertToBlockEntity)
                .collect(Collectors.toList()));
        return module;
    }

    private Block convertToBlockEntity(BlockDto dto) {
        return modelMapper.map(dto, Block.class);
    }
}
