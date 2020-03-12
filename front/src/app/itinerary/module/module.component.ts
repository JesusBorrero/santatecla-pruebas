import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../auth/login.service';
import { Router, ActivatedRoute } from '@angular/router';
import {Unit} from '../../unit/unit.model';
import {UnitService} from '../../unit/unit.service';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmActionComponent} from '../../confirmAction/confirm-action.component';
import {Module} from './module.model';
import {UnitModuleService} from './unit-module.service';
import {ModuleFormComponent} from './moduleForm/module-form.component';
import {ModuleRenameComponent} from "./moduleRename/module-rename.component";
import {ModuleService} from "./module.service";

@Component({
  selector: 'app-modules',
  templateUrl: './module.component.html',
  styleUrls: ['./module.component.css']
})

export class ModuleComponent implements OnInit {

  unit: Unit;
  unitId: number;

  modules: Module[];
  modulesResult: Module[];

  newModule: Module;

  optionInfoPosition = 'after';

  confirmText = 'Se eliminará el modulo permanentemente';
  button1 = 'Cancelar';
  button2 = 'Borrar';

  constructor(public loginService: LoginService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private unitService: UnitService,
              private moduleService: ModuleService,
              private unitModuleService: UnitModuleService,
              public dialog: MatDialog) {
  }

  ngOnInit() {

    this.modules = [];
    this.modulesResult = [];

    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
    });

    this.unitService.getUnit(this.unitId).subscribe((data: Unit) => {
      this.unit = data;
      this.modules = data.modules;
      this.modulesResult = this.modules;
    }, error => {console.log(error); });

  }

  editModule(moduleId: number) {
    this.router.navigate(['/units/' + this.unitId + '/modules/' + moduleId]);
  }

  searchModule(value: string) {
    this.modulesResult = [];
    for (let result of this.modules) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.modulesResult.push(result);
      }
    }
  }

  addModule(): void {
    this.newModule = {name: ''};
    const dialogRef = this.dialog.open(ModuleFormComponent, {
      data: {itinerary: this.newModule}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.newModule = result;
      if (typeof this.newModule !== 'undefined') {
        if (this.newModule.name !== '') {
          this.unitModuleService.addModule(this.unitId, this.newModule).subscribe((data) => {
            this.router.navigate(['/units/' + this.unitId + '/modules/' + data.id]);
          });
        }
      }
    });
  }

  deleteModule(moduleId: number) {
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmText, button1: this.button1, button2: this.button2}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 1) {
        this.unitModuleService.deleteModule(this.unitId, moduleId).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }

  renameModule(module: Module) {
    const dialogRef = this.dialog.open(ModuleRenameComponent, {
      data: { name: module.name }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (typeof result !== 'undefined') {
        if ((result !== '') && (result !== module.name)) {
          module.name = result;
          this.moduleService.updateModule(module).subscribe(() => {
          });
        }
      }
    });
  }

}
