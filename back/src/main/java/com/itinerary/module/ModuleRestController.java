package com.itinerary.module;

import com.GeneralRestController;
import com.itinerary.block.Block;
import com.itinerary.lesson.Lesson;
import com.slide.Slide;
import com.unit.Unit;
import com.unit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
public class ModuleRestController extends GeneralRestController implements ModuleController{

    @Autowired
    protected UnitService unitService;

    @GetMapping(value="/")
    public MappingJacksonValue modules(){
        MappingJacksonValue result = new MappingJacksonValue(this.moduleService.findAll());
        return result;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Module> module(@PathVariable long id){
        Optional<Module> module = this.moduleService.findOne(id);
        if (module.isPresent()) {
            return new ResponseEntity<>(module.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable long id, @RequestBody Module module){

        Optional<Module> m = this.moduleService.findOne(id);

        if(m.isPresent()){
            m.get().update(module);
            this.moduleService.save(m.get());
            return new ResponseEntity<>(m.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{moduleId}")
    public ResponseEntity<Block> addBlock(@RequestBody Block block, @PathVariable long moduleId) {
        if(block.getId() != moduleId) {

            Optional<Module> module = this.moduleService.findOne(moduleId);
            Optional<Block> b = this.blockService.findOne(block.getId());
            if(module.isPresent() && b.isPresent()) {
                if(!this.moduleService.containsRecursiveParent(module.get(), block.getId())) {
                    module.get().getBlocks().add(block);
                    this.moduleService.save(module.get());

                    b.get().getParentsId().add(moduleId);
                    this.blockService.save(b.get());
                    return new ResponseEntity<>(block, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/{moduleId}/blocks/{blockId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Module> deleteBlockFromModule(@PathVariable long moduleId, @PathVariable long blockId) {

        Optional<Module> module = this.moduleService.findOne(moduleId);
        Optional<Block> block = this.blockService.findOne(blockId);

        if (module.isPresent() && block.isPresent()) {
            module.get().getBlocks().remove(block.get());
            this.moduleService.save(module.get());

            block.get().getParentsId().remove(moduleId);
            this.blockService.save(block.get());

            return new ResponseEntity<>(module.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
