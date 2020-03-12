package com.unit;

import java.util.*;

import com.GeneralRestController;
import com.card.Card;
import com.relation.Relation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units")
public class UnitRestController extends GeneralRestController {

    @GetMapping(value = "/")
    public ResponseEntity<List<Unit>> getUnits() {
        return new ResponseEntity<>(this.unitService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Unit> getUnit(@PathVariable int id) {
        Optional<Unit> unit = this.unitService.findOne(id);
        if (unit.isPresent()) {
            return new ResponseEntity<>(unit.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
                this.unitService.save(savedUnit.get());
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
        savedUnit.setName(unit.getName());
        for (Relation relation : unit.getIncomingRelations()) {
            if ((relation.getIncoming() != 0) && (relation.getOutgoing() != 0)) {
                if (relation.getId() > 0) {
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
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Unit> deleteUnit(@PathVariable long id) {
        Optional<Unit> unit = unitService.findOne(id);
        if (unit.isPresent()) {
            for (Relation relation : unit.get().getOutgoingRelations()) {
                relationService.delete(relation.getId());
            }
            for (Relation relation : unit.get().getIncomingRelations()) {
                relationService.delete(relation.getId());
            }
            unitService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/relations/{id}")
    public ResponseEntity<Relation> deleteRelation(@PathVariable long id) {
        Optional<Relation> relation = relationService.findOne(id);
        if (relation.isPresent()) {
            relationService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/search")
    public ResponseEntity<List<Unit>> searchUnits(@RequestParam String name) {
        List<Unit> units = this.unitService.findByNameContaining(name);
        return new ResponseEntity<>(units, HttpStatus.OK);
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

}