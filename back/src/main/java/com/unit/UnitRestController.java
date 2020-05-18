package com.unit;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.GeneralRestController;
import com.card.Card;
import com.card.CardDto;
import com.image.Image;
import com.itinerary.module.Module;
import com.relation.Relation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/units")
public class UnitRestController extends GeneralRestController implements UnitController {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/")
    public ResponseEntity<List<UnitDto>> getUnits() {
        return new ResponseEntity<>(this.unitService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(convertToDto(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/")
    public ResponseEntity<UnitDto> createUnit(@RequestBody UnitDto unitDto) {
        Unit savedUnit = new Unit();
        Unit unit = convertToEntity(unitDto);
        try {
            updateUnit(savedUnit, unit);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        this.unitService.save(savedUnit);
        return new ResponseEntity<>(convertToDto(savedUnit), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UnitDto> updateUnitCards(@PathVariable int id, @RequestBody UnitDto unitDto) {
        Optional<Unit> u = this.unitService.findOne(id);
        Unit unit = convertToEntity(unitDto);
        if (u.isPresent()) {
            u.get().setCards((List<Card>) unit.getCards());
            this.unitService.save(u.get());
            return new ResponseEntity<>(convertToDto(u.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<List<UnitDto>> updateUnits(@RequestBody List<UnitDto> unitDtos) {
        List<Unit> savedUnits = new ArrayList<>();
        List<Unit> units = unitDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
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
        return new ResponseEntity<>(savedUnits.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    private void updateUnit(Unit savedUnit, Unit unit) throws IOException {
        if (!unitService.isValidName(unit)) {
            throw new IOException("Invalid name");
        }
        if (!unit.getName().equals(savedUnit.getName())) {
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
    public ResponseEntity<UnitDto> deleteUnit(@PathVariable long id) {
        Optional<Unit> unit = unitService.findOne(id);
        if (unit.isPresent()) {
            if (unitService.ableToDeleteUnit(unit.get())) {
                Optional<Unit> optional;
                for (Relation relation : unit.get().getOutgoingRelations()) {
                    optional = unitService.findOne(relation.getIncoming());
                    if (optional.isPresent()) {
                        optional.get().getIncomingRelations().remove(relation);
                        relationService.delete(relation.getId());
                    }
                }
                for (Relation relation : unit.get().getIncomingRelations()) {
                    optional = unitService.findOne(relation.getOutgoing());
                    if (optional.isPresent()) {
                        optional.get().getOutgoingRelations().remove(relation);
                        relationService.delete(relation.getId());
                    }
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

    @GetMapping(value = "/search")
    public ResponseEntity<List<UnitDto>> searchUnits(@RequestParam String name) {
        List<Unit> units = this.unitService.findByNameContaining(name);
        return new ResponseEntity<>(units.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/unambiguousName")
    public ResponseEntity<UnitDto> getUnitUnambiguousName(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(
                convertToDto(new Unit(unitService.getUnambiguousName(value))), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id}/absoluteName")
    public ResponseEntity<UnitDto> getUnitAbsoluteName(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(
                convertToDto(new Unit(unitService.getAbsoluteName(value))), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id}/parent")
    public ResponseEntity<UnitDto> getUnitParent(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        return unit.map(value -> new ResponseEntity<>(
                convertToDto(unitService.getParent(value, new HashSet<>())), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/valid")
    public ResponseEntity<Boolean> validName(@RequestBody UnitDto unitDto) {
        Unit unit = convertToEntity(unitDto);
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

    @GetMapping(value = "/{unitId}/images/{imageId}")
    public ResponseEntity<Image> getImage(@PathVariable int unitId, @PathVariable int imageId) {
        Optional<Unit> optionalUnit = this.unitService.findOne(unitId);
        if (optionalUnit.isPresent()) {
            Optional<Image> image = this.imageService.findOne(imageId);
            return (image.isPresent() && image.get().getUnitId() == unitId) ? (new ResponseEntity<>(image.get(), HttpStatus.OK)) : (new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
            Image imageToReturn = modelMapper.map(newImage, Image.class);
            return new ResponseEntity<>(imageToReturn, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{unitId}/images/{imageId}")
    public ResponseEntity<Image> deleteImage(@PathVariable int unitId, @PathVariable int imageId) {
        Optional<Unit> optionalUnit = this.unitService.findOne(unitId);
        if (optionalUnit.isPresent()) {
            Optional<Image> image = this.imageService.findOne(imageId);
            if (image.isPresent()) {
                this.imageService.delete(imageId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/module/{id}")
    public ResponseEntity<UnitDto> getModuleUnit(@PathVariable long id) {
        Optional<Module> module = this.moduleService.findOne(id);
        if (module.isPresent()) {
            Optional<Unit> result = this.unitService.findOne(this.unitService.findModuleUnit(id));
            return result.map(unit -> new ResponseEntity<>(convertToDto(unit), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private UnitDto convertToDto(Unit unit) {
        UnitDto dto = modelMapper.map(unit, UnitDto.class);
        dto.setCards(StreamSupport.stream(unit.getCards().spliterator(), false)
                .map(this::convertToCardDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private Unit convertToEntity(UnitDto dto) {
        return modelMapper.map(dto, Unit.class);
    }

    private CardDto convertToCardDto(Card card) {
        return modelMapper.map(card, CardDto.class);
    }
}