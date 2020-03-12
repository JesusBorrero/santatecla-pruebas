import { Unit } from '../unit/unit.model';
import { UnitService } from '../unit/unit.service';
import { Component, ViewChild, OnInit, AfterContentInit, ElementRef, HostListener, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Relation } from '../relation/relation.model';
import { RelationType } from '../relation/relation.type';
import { TdDialogService } from '@covalent/core';
import { TabService } from '../tab/tab.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmActionComponent } from '../confirmAction/confirm-action.component';

declare var mermaid: any;

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})

export class ViewComponent implements OnInit, AfterContentInit, OnDestroy {

  UNIT_NAME_SEPARATOR = '/';

  searchField = '';
  results: Unit[] = [];

  parentLevel = 1;
  childrenLevel = 3;
  selectLevelOptions = [0, 1, 2, 3, -1];

  focusedUnits: Set<string> = new Set<string>();
  units: Map<string, Unit> = new Map<string, Unit>();
  relations = new Map<string, Relation>();
  remainingUnits = 0;
  remainingFocusedUnits = 0;

  showMenu = true;

  newUnitId = 0;
  newRelationId = 0;

  changed = false;
  unitNameErrors = false;
  ableToSave = false;
  showSpinner = false;

  showDiagram = false;
  @ViewChild('uml') umlDiv;

  selectedTarget: HTMLInputElement;
  @ViewChild('umlNodeOptions') umlNodeOptions: ElementRef;
  showUmlNodeOptions = false;
  @ViewChild('umlPathOptions') umlPathOptions: ElementRef;
  showUmlPathOptions = false;
  selectedRelationType = '';
  @ViewChild('umlNewPath') umlNewPath: ElementRef;
  creatingRelation = null;


  constructor(private router: Router, private unitService: UnitService, private dialogService: TdDialogService,
              private tabService: TabService, private dialog: MatDialog) {}

  ngOnInit() {
    if (this.tabService.unitId) {
      this.unitService.getUnit(this.tabService.unitId).subscribe((unit: Unit) => {
        this.addFocusedUnit(this.tabService.unitId.toString(), unit);
        this.init();
      });
    } else {
      this.init();
    }
  }

  init() {
    this.search();
    this.tabService.setUnits();
    window.scroll(0, 0);
    window.document.body.style.overflow = 'hidden';
    this.focusUnit();
  }

  ngAfterContentInit() {
    mermaid.initialize({
      theme: 'default',
      logLevel: 3,
      flowchart: { curve: 'basis' },
      gantt: { axisFormat: '%m/%d/%Y' },
      sequence: { actorMargin: 50 },
    });
  }

  ngOnDestroy() {
    window.document.body.style.overflow = 'auto';
  }



  // Data

  focusUnit() {
    if (this.ableToSave) {
      this.save(null, null, null);
    } else if (this.changed) {
      const dialogRef = this.reloadDialog();
      dialogRef.afterClosed().subscribe(result => {
        window.scroll(0, 0);
        if (result === 1) {
          this.loadUnits();
        }
      });
    } else {
      this.loadUnits();
    }
  }

  loadUnits() {
    this.units.clear();
    this.relations.clear();
    this.deleteNewUnits();
    this.changed = false;
    if (this.focusedUnits.size === 0) {
      this.showSpinner = false;
      this.showDiagram = false;
    } else {
      this.showSpinner = true;
      this.remainingUnits = 0;
      this.remainingFocusedUnits = this.focusedUnits.size;
      this.focusedUnits.forEach((id) => {
        this.remainingFocusedUnits--;
        this.getUnitAndUpdateUml(+id, new Set<number>(), this.parentLevel, this.childrenLevel);
        if ((this.remainingUnits === 0) && (this.remainingFocusedUnits === 0)) {
          this.updateUml();
        }
      });
    }
  }

  getUnitAndUpdateUml(id: number, visited: Set<number>, remainingParentLevel: number, remainingChildrenLevel: number) {
    this.remainingUnits--;
    visited.add(id);
    this.unitService.getUnit(id).subscribe((data: Unit) => {
      this.addUnit(data);
      this.remainingUnits += data.incomingRelations.length + data.outgoingRelations.length + 1;

      data.incomingRelations.forEach((relation: Relation) => {
        this.remainingUnits--;
        if (!this.getRelationById(relation.id.toString())) {
          const outgoing = +relation.outgoing;
          if (remainingChildrenLevel !== 0) {
            if (!visited.has(outgoing)) {
              this.getUnitAndUpdateUml(outgoing, visited, 0, remainingChildrenLevel - 1);
            }
            this.addRelation(relation);
          }
        }
      });

      data.outgoingRelations.forEach((relation: Relation) => {
        this.remainingUnits--;
        if (!this.getRelationById(relation.id.toString())) {
          const incoming = +relation.incoming;
          if (remainingParentLevel !== 0) {
            if (!visited.has(incoming)) {
              this.getUnitAndUpdateUml(incoming, visited, remainingParentLevel - 1, 0);
            }
            this.addRelation(relation);
          }
        }
      });

      if ((this.remainingUnits === 0) && (this.remainingFocusedUnits === 0)) {
        this.updateUml();
      }

    }, error => {
      console.log(error);
    });
  }

  getFocusedUnits(): Unit[] {
    return Array.from(this.units.values()).filter((unit: Unit) => this.focusedUnits.has(unit.id.toString()));
  }

  reloadLevels(newParentLevel, newChildrenLevel) {
    if ((newParentLevel != null) && (newParentLevel !== this.parentLevel)) {
      this.parentLevel = newParentLevel;
      this.focusUnit();
    }
    if ((newChildrenLevel != null) && (newChildrenLevel !== this.childrenLevel)) {
      this.childrenLevel = newChildrenLevel;
      this.focusUnit();
    }
  }

  addFocusedUnit(id, unit) {
    this.focusedUnits.add(id.toString());
    if (unit) {
      this.addUnit(unit);
    }
  }

  addUnit(unit: Unit) {
    this.units.set(unit.id.toString(), unit);
  }

  getUnitById(id: string): Unit {
    return this.units.get(id.toString());
  }

  addRelation(relation: Relation) {
    this.relations.set(relation.id.toString(), relation);
  }

  getRelationById(id: string): Relation {
    return this.relations.get(id);
  }

  getNewUnitId() {
    return '0' + this.newUnitId++;
  }

  getNewRelationId(): string {
    return '0' + this.newRelationId++;
  }

  isNewId(id: string): boolean {
    return id.toString().substring(0, 1) === '0';
  }

  save(goToUnit, deleteUnit, deleteRelation) {
    if (this.ableToSave) {
      this.ableToSave = false;
      this.changed = false;
      this.showSpinner = true;

      const unitsToCreate: Unit[] = [];
      this.focusedUnits.forEach((focusedUnitId) => {
        if (this.isNewId(focusedUnitId.toString())) {
          unitsToCreate.push(this.getUnitById(focusedUnitId.toString()));
        }
      });

      let i = 0;
      unitsToCreate.forEach((unit: Unit) => {
        const unitToCreate = {
          id: '0',
          name: unit.name,
          outgoingRelations: [],
          incomingRelations: [],
          itineraries: [],
          definitionQuestions: [],
          listQuestions: [],
          testQuestions: []
        };
        this.unitService.createUnit(unitToCreate).subscribe((data: Unit) => {
          const oldId = unit.id.toString();
          unit.id = data.id;
          unit.name = data.name;
          unit.incomingRelations.forEach((relation: Relation) => {
            relation.incoming = data.id.toString();
          });
          unit.outgoingRelations.forEach((relation: Relation) => {
            relation.outgoing = data.id.toString();
          });
          if (this.focusedUnits.has(oldId)) {
            this.focusedUnits.delete(oldId);
            this.units.delete(oldId);
            this.addFocusedUnit(unit.id.toString(), unit);
          }

          i++;
          if (i === unitsToCreate.length) {
            this.saveUnitsAndRelations(goToUnit, deleteUnit, deleteRelation);
          }
        }, error => {
          this.saveError(error);
        });
      });
      if (unitsToCreate.length === 0) {
        this.saveUnitsAndRelations(goToUnit, deleteUnit, deleteRelation);
      }
    }
  }

  saveUnitsAndRelations(goToUnit, deleteUnit, deleteRelation) {
    const unitsToSave: Unit[] = [];
    this.units.forEach((unit: Unit) => {
      const unitToSave = {
        id: unit.id,
        name: unit.name,
        outgoingRelations: unit.outgoingRelations,
        incomingRelations: unit.incomingRelations,
        itineraries: [],
        definitionQuestions: [],
        listQuestions: [],
        testQuestions: []
      };
      unitToSave.incomingRelations.forEach((relation: Relation) => {
        if (this.isNewId(relation.id.toString())) {
          relation.id = '0';
        }
      });
      unitToSave.outgoingRelations.forEach((relation: Relation) => {
        if (this.isNewId(relation.id.toString())) {
          relation.id = '0';
        }
      });
      unitsToSave.push(unitToSave);
    });
    this.unitService.saveUnits(unitsToSave).subscribe(() => {
      if (goToUnit) {
        this.goToUnit(goToUnit);
      } else if (deleteUnit) {
        this.deleteUnit(deleteUnit);
      } else if (deleteRelation) {
        this.deleteRelation(deleteRelation);
      } else {
        this.focusUnit();
      }
    }, error => {
      this.saveError(error);
    });
  }

  saveError(error) {
    this.showSpinner = false;
    if (error.status === 409) {
      const dialogRef = this.saveErrorDialog();
      dialogRef.afterClosed().subscribe(() => {
        window.scroll(0, 0);
        this.updateUml();
      });
    } else {
      console.log(error);
    }
  }

  checkUnitNames() {
    this.unitNameErrors = false;
    let i = 0;
    this.units.forEach((unit) => {
      const isNewId = (this.isNewId(unit.id.toString()));
      const unitToCheck: Unit = {
        id: ((isNewId) ? '0' : unit.id),
        name: unit.name,
        incomingRelations: ((isNewId) ? [] : unit.incomingRelations),
        outgoingRelations: ((isNewId) ? [] : unit.outgoingRelations)
      };
      this.unitService.validName(unitToCheck).subscribe((valid) => {
        i++;
        if ((!valid) || (isNewId && this.isNewRepeated(unit))) {
          this.ableToSave = false;
          this.unitNameErrors = true;
          this.findUnitTarget(unit.id.toString()).id = 'E' + unit.id.toString();
        }
        if (i === this.units.size) {
          this.ableToSave = ((this.changed) && (!this.unitNameErrors));
          this.showSpinner = false;
          this.showDiagram = true;
        }
      });
    });
  }

  isNewRepeated(unit: Unit) {
    let repeated = false;
    this.focusedUnits.forEach((focusedUnitId) => {
      if ((!repeated) && (this.isNewId(focusedUnitId.toString())) && (focusedUnitId.toString() !== unit.id) && (this.getUnitById(focusedUnitId.toString()).name === unit.name)) {
        repeated = true;
      }
    });
    return repeated;
  }

  deleteNewUnits() {
    this.focusedUnits.forEach((focusedUnitId) => {
      if (this.isNewId(focusedUnitId)) {
        this.focusedUnits.delete(focusedUnitId);
        this.units.delete(focusedUnitId);
      }
    });
  }



  // Uml

  updateUml() {
    this.showSpinner = true;
    const element: any = this.umlDiv.nativeElement;
    element.innerHTML = '';
    try {
      const uml = this.parseUml(this.relations);
      mermaid.render('uml', uml, (svgCode, bindFunctions) => {
        element.innerHTML = svgCode;
        this.checkUnitNames();
        this.updateUmlNodeOptions();
      });
    } catch (error) {
      console.log(error);
    }
  }

  getSelectedUnitId(target): string {
    return target.id.toString().substring(1, target.id.length);
  }

  updateUnitName() {
    const selectedUnit: Unit = this.getUnitById(this.getSelectedUnitId(this.selectedTarget));
    const newName = this.umlNodeOptions.nativeElement.firstChild.value;
    this.changed = ((this.changed) || ((newName) && (selectedUnit.name !== newName)));
    selectedUnit.name = (newName ? newName : selectedUnit.name);
    this.units.get(this.getSelectedUnitId(this.selectedTarget)).name =  (newName ? newName : selectedUnit.name);
    this.setShowUmlNodeOptions(false);
    this.setShowUmlPathOptions(false);
    this.updateUml();
  }

  createUnit(relationType): Unit {
    const selectedUnit: Unit = this.getUnitById(this.getSelectedUnitId(this.selectedTarget));
    const newUnitName = 'Nueva unidad';
    const newUnit: Unit = {
      id: this.getNewUnitId().toString(),
      name: newUnitName,
      incomingRelations: [],
      outgoingRelations: []
    };
    const newRelation: Relation = {
      id: this.getNewRelationId().toString(),
      relationType,
      incoming: this.getSelectedUnitId(this.selectedTarget),
      outgoing: newUnit.id.toString()
    };
    newUnit.outgoingRelations.push(newRelation);
    this.addFocusedUnit(newUnit.id.toString(), newUnit);
    selectedUnit.incomingRelations.push(newRelation);
    this.addRelation(newRelation);
    this.updateUml();
    this.changed = true;
    return newUnit;
  }

  createSeparateUnit() {
    const id = this.getNewUnitId().toString();
    const newUnit: Unit = {
      id: id,
      name: 'Nueva unidad',
      incomingRelations: [],
      outgoingRelations: []
    };
    this.addFocusedUnit(id.toString(), newUnit);
    this.updateUml();
    this.changed = true;
  }

  createRelation(relationType, incoming: Unit, outgoing: Unit) {
    const newRelation: Relation = {
      id: this.getNewRelationId().toString(),
      relationType,
      incoming: incoming.id,
      outgoing: outgoing.id
    };
    const duplicateIncoming = this.checkDuplicateRelation(incoming, newRelation);
    const duplicateOutgoing = this.checkDuplicateRelation(outgoing, newRelation);
    if (!(duplicateIncoming && duplicateOutgoing)) {
      if (!duplicateIncoming) {
        incoming.incomingRelations.push(newRelation);
      }
      if (!duplicateOutgoing) {
        outgoing.outgoingRelations.push(newRelation);
      }
      this.addRelation(newRelation);
    }
    this.updateUml();
    this.changed = true;
  }

  initCreatingRelation() {
    this.creatingRelation = {
      outgoing: this.getSelectedUnitId(this.selectedTarget),
      boundingClientRect: this.selectedTarget.getBoundingClientRect(),
      relationType: null
    };
  }

  checkDuplicateRelation(unit: Unit, relation: Relation): boolean {
    let duplicate = false;
    unit.incomingRelations.forEach((incomingRelation: Relation) => {
      if ((!duplicate) && (incomingRelation.incoming.toString() === relation.incoming.toString()) && (incomingRelation.outgoing.toString() === relation.outgoing.toString())) {
        incomingRelation.relationType = relation.relationType;
        duplicate = true;
      }
    });
    unit.outgoingRelations.forEach((outgoingRelation: Relation) => {
      if ((!duplicate) && (outgoingRelation.incoming.toString() === relation.incoming.toString()) && (outgoingRelation.outgoing.toString() === relation.outgoing.toString())) {
        outgoingRelation.relationType = relation.relationType;
        duplicate = true;
      }
    });
    return duplicate;
  }

  confirmDeleteUnit() {
    const id = this.getSelectedUnitId(this.selectedTarget).toString();
    if (this.isNewId(id)) {
      this.deleteNewUnit(id);
      this.setShowUmlNodeOptions(false);
      this.updateUml();
    } else  if (this.changed && (!this.ableToSave)) {
      const dialogRef = this.reloadDialog();
      dialogRef.afterClosed().subscribe(result => {
        window.scroll(0, 0);
        if (result === 1) {
          this.deleteUnit(id);
        }
      });
    } else {
      const dialogRef = this.confirmDeleteUnitDialog();
      dialogRef.afterClosed().subscribe(result => {
        window.scroll(0, 0);
        if (result === 1) {
          if (this.changed) {
            this.save(null, id, null);
          } else {
            this.deleteUnit(id);
          }
        }
      });
    }
  }

  deleteUnit(id: string) {
    this.unitService.deleteUnit(+id).subscribe(() => {
      this.changed = false;
      this.focusedUnits.delete(id);
      this.units.delete(id);
      this.focusUnit();
    });
  }

  deleteNewUnit(id: string) {
    const unit = this.getUnitById(id);
    unit.incomingRelations.forEach((relation) => {
      this.deleteNewIncomingRelation(relation);
    });
    unit.outgoingRelations.forEach((relation) => {
      this.deleteNewOutgoingRelation(relation);
    });
    this.focusedUnits.delete(id);
    this.units.delete(id);
  }

  deleteNewIncomingRelation(relation: Relation) {
    this.relations.delete(relation.id.toString());
    const outgoingRelations = this.getUnitById(relation.outgoing.toString()).outgoingRelations;
    const index = outgoingRelations.indexOf(relation, 0);
    if (index > -1) {
      outgoingRelations.splice(index, 1);
    }
  }

  deleteNewOutgoingRelation(relation: Relation) {
    this.relations.delete(relation.id.toString());
    const outgoingRelations = this.getUnitById(relation.incoming.toString()).incomingRelations;
    const index = outgoingRelations.indexOf(relation, 0);
    if (index > -1) {
      outgoingRelations.splice(index, 1);
    }
  }

  confirmDeleteRelation() {
    const splittedRelation: string[] = this.selectedTarget.id.split('-');
    const incoming = splittedRelation[0];
    const outgoing = splittedRelation[1];
    const relationType = this.getRelationTypeEquivalent(splittedRelation[2]);
    this.units.get(incoming).incomingRelations.forEach((relation: Relation) => {
      if ((relation.outgoing.toString() === outgoing) && (relation.relationType === relationType)) {
        if (this.isNewId(relation.id)) {
          this.deleteNewIncomingRelation(relation);
          this.deleteNewOutgoingRelation(relation);
          this.setShowUmlPathOptions(false);
          this.updateUml();
        } else if (this.changed && (!this.ableToSave)) {
          const dialogRef = this.reloadDialog();
          dialogRef.afterClosed().subscribe(result => {
            window.scroll(0, 0);
            if (result === 1) {
              this.deleteRelation(relation);
            }
          });
        } else {
          const dialogRef = this.confirmDeleteRelationDialog();
          dialogRef.afterClosed().subscribe(result => {
            window.scroll(0, 0);
            if (result === 1) {
              if (this.changed) {
                this.save(null, null, relation);
              } else {
                this.deleteRelation(relation);
              }
            }
          });
        }
      }
    });
  }

  deleteRelation(relation: Relation) {
    this.unitService.deleteRelation(+relation.id).subscribe(() => {
      this.changed = false;
      this.focusUnit();
    });
  }

  findUnitTarget(id: string): HTMLInputElement {
    let found = false;
    let target: HTMLInputElement = null;
    this.umlDiv.nativeElement.firstChild.childNodes.forEach((childNode) => {
      const firstChild = childNode.firstChild as HTMLInputElement;
      if ((!found) && (firstChild) && (firstChild.id) && (this.getSelectedUnitId(firstChild) === id.toString())) {
        target = firstChild;
        found = true;
      }
    });
    return target;
  }

  drawUmlNodeOptions() {
    const input = this.umlNodeOptions.nativeElement.firstChild;
    const padding = '0.5rem';
    input.style.left = 'calc(' + (this.selectedTarget.getBoundingClientRect().left + window.pageXOffset) + 'px + ' + padding + ')';
    input.style.width = 'calc(' + (this.selectedTarget.getBoundingClientRect().width) + 'px - ' + padding + ' - ' + padding + ')';
    input.style.top = (this.selectedTarget.getBoundingClientRect().top + window.pageYOffset) + 'px';
    input.style.height = (this.selectedTarget.getBoundingClientRect().height) + 'px';
    const text = (this.selectedTarget.nextSibling as HTMLInputElement);
    if (text.innerHTML) { input.value = text.innerHTML; }
    text.innerHTML = '';
    input.focus();
    input.setSelectionRange(0, input.value.length);
    const optionsStyle = this.umlNodeOptions.nativeElement.lastChild.style;
    const left = (this.selectedTarget.getBoundingClientRect().right + window.pageXOffset);
    const right = (this.selectedTarget.getBoundingClientRect().left + window.pageXOffset);
    if (left < (window.innerWidth / 1.25)) {
      optionsStyle.left = (left + 'px');
      optionsStyle.right = '';
    } else {
      optionsStyle.left = '';
      optionsStyle.right = ((window.innerWidth - right) + 'px');
    }
    const top = (this.selectedTarget.getBoundingClientRect().top + window.pageYOffset);
    const bottom = (this.selectedTarget.getBoundingClientRect().bottom + window.pageYOffset);
    if (top < (window.innerHeight / 1.25)) {
      optionsStyle.top = (top + 'px');
      optionsStyle.bottom = '';
    } else {
      optionsStyle.top = 'auto';
      optionsStyle.bottom = ((window.innerHeight - bottom) + 'px');
    }
  }

  updateUmlNodeOptions() {
    if (this.showUmlNodeOptions) {
      this.drawUmlNodeOptions();
    } else {
      this.umlNodeOptions.nativeElement.firstChild.style.top = '120%';
      this.umlNodeOptions.nativeElement.lastChild.style.top = '120%';
    }
  }

  setShowUmlNodeOptions(showUmlNodeOptions: boolean) {
    this.showUmlNodeOptions = showUmlNodeOptions;
  }

  drawUmlPathOptions() {
    let relationType;
    if (this.selectedTarget.attributes[3]) { relationType = this.selectedTarget.attributes[3].nodeValue; }
    this.selectedRelationType = this.getRelationTypeEquivalent(relationType);
    const optionsStyle = this.umlPathOptions.nativeElement.firstChild.style;
    optionsStyle.left = (this.selectedTarget.getBoundingClientRect().right + window.pageXOffset) + 'px';
    optionsStyle.top = (this.selectedTarget.getBoundingClientRect().top + window.pageYOffset) + 'px';
  }

  setShowUmlPathOptions(showUmlNodeOptions: boolean) {
    this.showUmlPathOptions = showUmlNodeOptions;
  }

  changeRelationType(id: string, newRelationType: string) {
    const splittedRelation: string[] = id.split('-');
    const incoming = splittedRelation[0];
    const outgoing = splittedRelation[1];
    const relationType = this.getRelationTypeEquivalent(splittedRelation[2]);
    this.units.get(incoming).incomingRelations.forEach((relation: Relation) => {
      if ((relation.outgoing.toString() === outgoing) && (relation.relationType === relationType)) {
        relation.relationType = newRelationType;
      }
    });
    this.units.get(outgoing).outgoingRelations.forEach((relation: Relation) => {
      if ((relation.incoming.toString() === incoming) && (relation.relationType === relationType)) {
        relation.relationType = newRelationType;
      }
    });
    this.setShowUmlPathOptions(false);
    this.updateUml();
    this.changed = true;
  }

  getRelationTypeEquivalent(relationType: string): string {
    let equivalent = RelationType.USE;
    if (relationType) {
      if (relationType.includes('composition')) {
        equivalent = RelationType.COMPOSITION;
      } else if (relationType.includes('extension')) {
        equivalent = RelationType.INHERITANCE;
      } else if (relationType.includes('aggregation')) {
        equivalent = RelationType.AGGREGATION;
      } else if (relationType.includes('dependency')) {
        equivalent = RelationType.ASSOCIATION;
      }
    }
    return equivalent;
  }



  // Key event listener

  @HostListener('window:keydown', ['$event'])
  onKeyPress($event: KeyboardEvent) {
    if (($event.metaKey || $event.ctrlKey) && ($event.key === 's')) {
      $event.preventDefault();
      this.save(null, null, null);
    }
  }



  // Mouse event listener

  @HostListener('document:click', ['$event'])
  documentClick(event: Event): void {
    const target = event.target as HTMLInputElement;

    if (!this.creatingRelation) {

      // Uml
      if ((target.id === 'composition-incoming-button') || (target.parentElement.id === 'composition-incoming-button')) {
        this.createUnit(RelationType.COMPOSITION).id;
      } else if ((target.id === 'inheritance-incoming-button') || (target.parentElement.id === 'inheritance-incoming-button')) {
        this.createUnit(RelationType.INHERITANCE).id;
      } else if ((target.id === 'aggregation-incoming-button') || (target.parentElement.id === 'aggregation-incoming-button')) {
        this.createUnit(RelationType.AGGREGATION).id;
      } else if ((target.id === 'association-incoming-button') || (target.parentElement.id === 'association-incoming-button')) {
        this.createUnit(RelationType.ASSOCIATION).id;
      } else if ((target.id === 'use-incoming-button') || (target.parentElement.id === 'use-incoming-button')) {
        this.createUnit(RelationType.USE).id;
      } else if ((target.id === 'inheritance-outgoing-button') || (target.parentElement.id === 'inheritance-outgoing-button')) {
        this.initCreatingRelation();
        this.creatingRelation.relationType = RelationType.INHERITANCE;
      } else if ((target.id === 'composition-outgoing-button') || (target.parentElement.id === 'composition-outgoing-button')) {
        this.initCreatingRelation();
        this.creatingRelation.relationType = RelationType.COMPOSITION;
      } else if ((target.id === 'aggregation-outgoing-button') || (target.parentElement.id === 'aggregation-outgoing-button')) {
        this.initCreatingRelation();
        this.creatingRelation.relationType = RelationType.AGGREGATION;
      } else if ((target.id === 'association-outgoing-button') || (target.parentElement.id === 'association-outgoing-button')) {
        this.initCreatingRelation();
        this.creatingRelation.relationType = RelationType.ASSOCIATION;
      } else if ((target.id === 'use-outgoing-button') || (target.parentElement.id === 'use-outgoing-button')) {
        this.initCreatingRelation();
        this.creatingRelation.relationType = RelationType.USE;
      } else if ((target.id === 'composition-relation-button') || (target.parentElement.id === 'composition-relation-button')) {
        this.changeRelationType(this.selectedTarget.id.toString(), RelationType.COMPOSITION);
      } else if ((target.id === 'inheritance-relation-button') || (target.parentElement.id === 'inheritance-relation-button')) {
        this.changeRelationType(this.selectedTarget.id.toString(), RelationType.INHERITANCE);
      } else if ((target.id === 'aggregation-relation-button') || (target.parentElement.id === 'aggregation-relation-button')) {
        this.changeRelationType(this.selectedTarget.id.toString(), RelationType.AGGREGATION);
      } else if ((target.id === 'association-relation-button') || (target.parentElement.id === 'association-relation-button')) {
        this.changeRelationType(this.selectedTarget.id.toString(), RelationType.ASSOCIATION);
      } else if ((target.id === 'use-relation-button') || (target.parentElement.id === 'use-relation-button')) {
        this.changeRelationType(this.selectedTarget.id.toString(), RelationType.USE);
      }
      if ((target.id !== 'uml-edit-input') && (target.id !== 'uml-node-options')) {
        this.closeUmlNodeOptions();
      }
    } else {
      if ((target.tagName === 'rect') || (target.tagName === 'text')) {
        if (this.creatingRelation.relationType === RelationType.INHERITANCE) {
          this.createRelation(this.creatingRelation.relationType, this.getUnitById(this.getSelectedUnitId(target)), this.getUnitById(this.creatingRelation.outgoing));
        } else {
          this.createRelation(this.creatingRelation.relationType, this.getUnitById(this.creatingRelation.outgoing), this.getUnitById(this.getSelectedUnitId(target)));
        }
      }
      this.creatingRelation = null;
      this.umlNewPath.nativeElement.setAttribute('d','');
    }
  }

  @HostListener('document:dblclick', ['$event'])
  documentDoubleClick(event: Event): void {
    this.creatingRelation = null;
    const target = event.target as HTMLInputElement;
    if ((!this.showUmlNodeOptions) && ((target.tagName === 'rect') || (target.tagName === 'text'))) {
      const id = this.getSelectedUnitId(target);
      if (!this.isNewId(id.toString())) {
        if (this.changed && this.ableToSave) {
          const dialogRef = this.changedDialog();
          dialogRef.afterClosed().subscribe(result => {
            window.scroll(0, 0);
            if (result === 1) {
              this.goToUnit(id);
            } else if (result === 2) {
              this.save(id, null, null);
            }
          });
        } else if (!this.changed) {
          this.goToUnit(id);
        }
      }
    }
  }

  @HostListener('document:contextmenu', ['$event'])
  documentRightClick(event: Event): void {
    event.preventDefault();
    this.creatingRelation = null;
    const target = event.target as HTMLInputElement;
    if ((!this.showUmlNodeOptions) && ((target.tagName === 'rect') || (target.tagName === 'text') || (target.tagName === 'path'))) {
      this.selectedTarget = target;
      if (target.tagName === 'path') {
        this.setShowUmlPathOptions(true);
        this.drawUmlPathOptions();
      } else {
        this.setShowUmlPathOptions(false);
        if (target.tagName === 'text') {
          this.selectedTarget = target.previousElementSibling as HTMLInputElement;
        }
        this.setShowUmlNodeOptions(true);
        this.drawUmlNodeOptions();
      }
    } else if ((target.id !== 'uml-edit-input') && (target.id !== 'uml-node-options')) {
      this.closeUmlNodeOptions();
    }
  }

  closeUmlNodeOptions() {
    if (this.showUmlNodeOptions) {
      this.updateUnitName();
    }
    this.setShowUmlNodeOptions(false);
    this.setShowUmlPathOptions(false);
    this.updateUmlNodeOptions();
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    if (this.creatingRelation) {
      this.umlNewPath.nativeElement.setAttribute('d',
        'M' + (this.creatingRelation.boundingClientRect.right + window.pageXOffset - (this.creatingRelation.boundingClientRect.width / 2)) +
        ' ' + (this.creatingRelation.boundingClientRect.top + window.pageYOffset + (this.creatingRelation.boundingClientRect.height / 2)) +
        ' L' + event.clientX + ' ' + event.clientY);
    } else {
      this.umlNewPath.nativeElement.setAttribute('d','');
    }
  }




  // Menu

  setShowMenu(showMenu: boolean) {
    this.showMenu = showMenu;
  }



  // Search

  search() {
    this.unitService.searchByNameContaining(this.searchField).subscribe((data: any) => {
      this.results = data;
    }, error => {
      console.log(error);
    });
  }

  getUnitPrefix(completeName: string) {
    const nameLength = completeName.split(this.UNIT_NAME_SEPARATOR)[completeName.split(this.UNIT_NAME_SEPARATOR).length - 1].length;
    return completeName.substring(0, completeName.length - nameLength);
  }

  getUnitName(completeName: string) {
    return completeName.split(this.UNIT_NAME_SEPARATOR)[completeName.split(this.UNIT_NAME_SEPARATOR).length - 1];
  }

  confirmSelectUnit(unit: Unit) {
    if (this.changed && (!this.ableToSave)) {
      const dialogRef = this.reloadDialog();
      dialogRef.afterClosed().subscribe(result => {
        window.scroll(0, 0);
        if (result === 1) {
          this.changed = false;
          this.selectUnit(unit);
        }
      });
    } else {
      this.selectUnit(unit);
    }
  }

  selectUnit(unit: Unit) {
    const id = unit.id.toString();
    if (this.focusedUnits.has(id)) {
      this.focusedUnits.delete(id);
    } else {
      this.addFocusedUnit(id.toString(), this.getUnitById(id));
    }
    this.focusUnit();
  }



  // Dialogs

  changedDialog() {
    return this.dialog.open(ConfirmActionComponent, {
      data: {
        confirmText: 'Se han realizado cambios',
        button1: 'Guardar',
        button2: 'Descartar'}
    });
  }

  reloadDialog() {
    return this.dialog.open(ConfirmActionComponent, {
      data: {
        confirmText: 'Se recargará el diagrama',
        button1: 'Cancelar',
        button2: 'Descartar cambios'
      }
    });
  }

  saveErrorDialog() {
    return this.dialog.open(ConfirmActionComponent, {
      data: {
        confirmText: 'Se ha producido un error al guardar. Hay unidades con nombres repetidos en el contexto actual',
        button1: 'Volver'
      }
    });
  }

  confirmDeleteUnitDialog() {
    return this.dialog.open(ConfirmActionComponent, {
      data: {
        confirmText: 'Se eliminará definitivamente la unidad y su contenido.',
        button1: 'Cancelar',
        button2: 'Eliminar'
      }
    });
  }

  confirmDeleteRelationDialog() {
    return this.dialog.open(ConfirmActionComponent, {
      data: {
        confirmText: 'Se eliminará la relación',
        button1: 'Cancelar',
        button2: 'Eliminar'}
    });
  }



  // Routing

  goToUnit(id) {
    this.router.navigate(['/unit/' + id]);
  }



  // Uml parser

  parseUml(relations: any) {
    const parsedRelations = this.getRelationsDiagram(relations);
    if (parsedRelations !== '') {
      return 'classDiagram\n' + parsedRelations;
    } else {
      throw new Error('Invalid data. Unable to display uml');
    }
  }

  getRelationsDiagram(relations: any): string {
    let uml = '';
    let connector = '';
    relations.forEach((relation: any) => {
      switch (relation.relationType) {
        case RelationType.ASSOCIATION: { connector = '-->'; break; }
        case RelationType.AGGREGATION: { connector = '"1"o-->"many"'; break; }
        case RelationType.COMPOSITION: { connector = '"0"*-->"0..n"'; break; }
        case RelationType.INHERITANCE: { connector = '<|--'; break; }
        case RelationType.USE: { connector = '--'; break; }
        default: { throw new Error('Unrecognized uml relation type'); }
      }
      uml += this.parseUnitName(relation.incoming.toString()) + connector + this.parseUnitName(relation.outgoing.toString()) + '\n';
    });
    this.focusedUnits.forEach((focusedUnitId) => {
      const unit = this.getUnitById(focusedUnitId);
      if (((unit.outgoingRelations.length === 0) && (unit.incomingRelations.length === 0)) ||
        ((this.parentLevel === 0) && (this.childrenLevel === 0)) ||
        ((unit.outgoingRelations.length === 0) && (this.childrenLevel === 0)) ||
        ((this.parentLevel === 0) && (unit.incomingRelations.length === 0))) {
        uml += this.parseUnitName(unit.id.toString()) + '<|--|>' + this.parseUnitName(unit.id.toString()) + '\n';
      }
    });
    return uml;
  }

  parseUnitName(id: string): string {
    let name = "";
    if (this.focusedUnits.has(id.toString())) {
      name = 'F';
    } else {
      name = 'N';
    }
    name += id;
    const unit = this.getUnitById(id);
    if (unit) {
      name += unit.name;
    }
    return name;
  }

}
