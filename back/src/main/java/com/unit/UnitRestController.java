package com.unit;

import java.util.*;
import java.util.stream.Collectors;

import com.GeneralRestController;
import com.card.Card;
import com.image.Image;
import com.itinerary.module.Module;
import com.relation.Relation;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/units")
public class UnitRestController extends GeneralRestController implements UnitController {

    @GetMapping(value = "/")
    public ResponseEntity<List<Unit>> getUnits() {
        return new ResponseEntity<>(this.unitService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Unit> getUnit(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/")
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit) {
        Unit savedUnit = new Unit();
        try {
            updateUnit(savedUnit, unit);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        this.unitService.save(savedUnit);
        return new ResponseEntity<>(savedUnit, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Unit> updateUnitCards(@PathVariable int id, @RequestBody Unit unit) {
        Optional<Unit> u = this.unitService.findOne(id);
        if (u.isPresent()) {
            u.get().setCards((List<Card>) unit.getCards());
            this.unitService.save(u.get());
            return new ResponseEntity<>(u.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<List<Unit>> updateUnits(@RequestBody List<Unit> units) {
        List<Unit> savedUnits = new ArrayList<>();
        for (Unit unit : units) {
            Optional<Unit> savedUnit = this.unitService.findOne(unit.getId());
            if (savedUnit.isPresent()) {
                try {
                    updateUnit(savedUnit.get(), unit);
                } catch (Exception e) {
                     return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                savedUnits.add(savedUnit.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(savedUnits, HttpStatus.OK);
    }

    private void updateUnit(Unit savedUnit, Unit unit) throws Exception {
        if (!unitService.isValidName(unit)) {
            throw new Exception("Invalid name");
        }
        if(!unit.getName().equals(savedUnit.getName())){
            this.slideService.updateAllSlidesUnitName(savedUnit.getName(), unit.getName());
        }
        savedUnit.setName(unit.getName());
        savedUnit.getIncomingRelations().removeAll(savedUnit.getIncomingRelations().stream().filter(relation -> !unit.getIncomingRelations().contains(relation)).collect(Collectors.toList()));
        savedUnit.getOutgoingRelations().removeAll(savedUnit.getOutgoingRelations().stream().filter(relation -> !unit.getOutgoingRelations().contains(relation)).collect(Collectors.toList()));
        for (Relation relation : unit.getIncomingRelations()) {
            if ((relation.getIncoming() != 0) && (relation.getOutgoing() != 0)) {
                if (relation.getId() > 0) {
                    relationService.save(relation);
                    savedUnit.addIncomingRelation(relation);
                } else {
                    Relation r;
                    Optional<Relation> relationByIncomingAndOutgoing = relationService.findRelationByIncomingAndOutgoing(relation.getIncoming(), relation.getOutgoing());
                    r = relationByIncomingAndOutgoing.orElseGet(() -> new Relation(relation.getRelationType(), relation.getIncoming(), relation.getOutgoing()));
                    relationService.save(r);
                    savedUnit.addIncomingRelation(r);
                }
            }
        }
        for (Relation relation : unit.getOutgoingRelations()) {
            if ((relation.getIncoming() != 0) && (relation.getOutgoing() != 0)) {
                if (relation.getId() > 0) {
                    relationService.save(relation);
                    savedUnit.addOutgoingRelation(relation);
                } else {
                    Relation r;
                    Optional<Relation> relationByIncomingAndOutgoing = relationService.findRelationByIncomingAndOutgoing(relation.getIncoming(), relation.getOutgoing());
                    r = relationByIncomingAndOutgoing.orElseGet(() -> new Relation(relation.getRelationType(), relation.getIncoming(), relation.getOutgoing()));
                    relationService.save(r);
                    savedUnit.addOutgoingRelation(r);
                }
            }
        }
        this.unitService.save(savedUnit);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Unit> deleteUnit(@PathVariable long id) {
        Optional<Unit> unit = unitService.findOne(id);
        if (unit.isPresent()) {
            if (unitService.ableToDeleteUnit(unit.get())) {
                for (Relation relation : unit.get().getOutgoingRelations()) {
                    unitService.findOne(relation.getIncoming()).map(value -> value.getIncomingRelations().remove(relation));
                    relationService.delete(relation.getId());
                }
                for (Relation relation : unit.get().getIncomingRelations()) {
                    unitService.findOne(relation.getOutgoing()).map(value -> value.getOutgoingRelations().remove(relation));
                    relationService.delete(relation.getId());
                }
                unitService.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/search")
    public ResponseEntity<List<Unit>> searchUnits(@RequestParam String name) {
        List<Unit> units = this.unitService.findByNameContaining(name);
        return new ResponseEntity<>(units, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/unambiguousName")
    public ResponseEntity<Unit> getUnitUnambiguousName(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(new Unit(unitService.getUnambiguousName(value)), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id}/absoluteName")
    public ResponseEntity<Unit> getUnitAbsoluteName(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(new Unit(unitService.getAbsoluteName(value)), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value="/{id}/parent")
    public ResponseEntity<Unit> getUnitParent(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(unitService.getParent(value, new HashSet<>()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value="/valid")
    public ResponseEntity<Boolean> validName(@RequestBody Unit unit) {
        return new ResponseEntity<>(unitService.isValidName(unit), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}/name")
    public ResponseEntity<Object> getUnitName(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        if (unit.isPresent()) {
            return new ResponseEntity<>(Collections.singletonMap("response", unit.get().getName()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}/images")
    public ResponseEntity<MappingJacksonValue> getUnitImages(@PathVariable int id) {
        Optional<Unit> optionalUnit = this.unitService.findOne(id);
        if (optionalUnit.isPresent()) {
            MappingJacksonValue result = new MappingJacksonValue(optionalUnit.get().getImages());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/{unitId}/images/{imageId}")
    public ResponseEntity<Image> getImage(@PathVariable int unitId, @PathVariable int imageId) {
        Optional<Unit> optionalUnit = this.unitService.findOne(unitId);
        if (optionalUnit.isPresent()) {
            Optional<Image> image = this.imageService.findOne(imageId);
            return (image.isPresent() && image.get().getUnitId() == unitId)?(new ResponseEntity<>(image.get(), HttpStatus.OK)):(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/{unitId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> addImage(@RequestParam(value = "imageFile") MultipartFile imageFile, @PathVariable int unitId) {
        Optional<Unit> optionalUnit = this.unitService.findOne(unitId);
        if (optionalUnit.isPresent()) {
            Image newImage = new Image(imageFile.getOriginalFilename());
            newImage.setUnitId(unitId);
            this.imageService.save(newImage);
            this.imageService.setImage(newImage, imageFile);
            this.imageService.save(newImage);

            optionalUnit.get().addImage(newImage);
            this.unitService.save(optionalUnit.get());

            return new ResponseEntity<>(newImage, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/{unitId}/images/{imageId}")
    public ResponseEntity<Image> deleteImage(@PathVariable int unitId, @PathVariable int imageId) {
        Optional<Unit> optionalUnit = this.unitService.findOne(unitId);
        if (optionalUnit.isPresent()) {
            Optional<Image> image = this.imageService.findOne(imageId);
            if(image.isPresent()) {
                this.imageService.delete(imageId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value="/module/{id}")
    public ResponseEntity<Unit> getModuleUnit(@PathVariable long id){
        Optional<Module> module = this.moduleService.findOne(id);
        if (module.isPresent()) {
            Optional<Unit> result = this.unitService.findOne(this.unitService.findModuleUnit(id));
            if(result.isPresent()){
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}