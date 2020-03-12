import { Unit } from '../../../unit/unit.model';
import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import { TdDialogService } from '@covalent/core';
import { Router, ActivatedRoute } from '@angular/router';
import {MatBottomSheet} from '@angular/material/bottom-sheet';
import {NestedTreeControl} from '@angular/cdk/tree';
import {MatTreeFlattener, MatTreeNestedDataSource} from '@angular/material/tree';
import { DOCUMENT } from '@angular/common';

import {Module} from '../module.model';

import { LoginService } from '../../../auth/login.service';
import { UnitService } from '../../../unit/unit.service';
import {TabService} from '../../../tab/tab.service';
import {ModuleService} from '../module.service';
import {CourseService} from '../../../course/course.service';
import {Course} from '../../../course/course.model';
import {UnitsBlocksToolComponent} from './units-blocks-tool.component';
import {MatDialog} from '@angular/material/dialog';
import {Block} from '../../block.model';
import {ConfirmActionComponent} from '../../../confirmAction/confirm-action.component';

@Component({
  selector: 'app-module-editor',
  templateUrl: './module-editor.component.html',
  styleUrls: ['./module-editor.component.css']
})

export class ModuleEditorComponent implements OnInit {

  @ViewChild('tree') tree;

  unitId: number;
  courseId: number;

  module: Module;
  moduleId: number;

  treeControl = new NestedTreeControl<Module>(node => !!node && node.blocks);
  indexTreeControl = new NestedTreeControl<Module>(node => !!node && node.blocks);
  dataSource = new MatTreeNestedDataSource<Module>();

  table: Map<Block, number>;

  showMenu: boolean;
  activeTab = 0;

  viewLessonPosition = 'after';
  optionInfoPosition = 'after';

  confirmText = 'Se eliminará el modulo permanentemente';
  confirmText2 = 'Se eliminará permanentemente';
  button1 = 'Cancelar';
  button2 = 'Borrar';

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private dialogService: TdDialogService,
              public loginService: LoginService,
              private unitService: UnitService,
              private bottomSheet: MatBottomSheet,
              private tabService: TabService,
              private moduleService: ModuleService,
              private courseService: CourseService,
              @Inject(DOCUMENT) document,
              public dialog: MatDialog) {}

  ngOnInit() {
    this.table = new Map<Block, number>();
    this.activatedRoute.params.subscribe(params => {
      if (this.loginService.isAdmin) {
        this.showMenu = false;
      } else {
        this.showMenu = true;
      }
      this.unitId = params.unitId;
      this.courseId = params.courseId;
      this.moduleId = params.moduleId;
      this.moduleService.getModule(this.moduleId).subscribe((module: Module) => {
        this.module = module;
        this.dataSource.data = this.module.blocks;

        for (let block of this.module.blocks) {
          this.completeTable(block, this.module.id);
        }

        this.indexTreeControl.dataNodes = this.dataSource.data;
        this.indexTreeControl.expandAll();

        if (this.unitId !== undefined) {
          this.unitService.getUnit(this.unitId).subscribe((unit: Unit) => {
            this.tabService.setUnitModule(unit.name, this.unitId, module.name, module.id);
          });
        } else {
          this.courseService.getCourse(this.courseId).subscribe((course: Course) => {
            this.tabService.setCourseModule(course.module.id, course.id, course.name);
          });
        }
      });
    });
  }

  hasChild = (_: number, node: Module) => !!node && !!node.blocks && node.blocks.length > 0;

  completeTable(block: Block, id: number) {
    if (typeof (block as Module).blocks !== 'undefined') {
      for (let b of (block as Module).blocks) {
        this.completeTable(b, block.id);
        this.table.set(block, id);
      }
    } else {
      this.table.set(block, id);
    }
  }

  expandNode(node: Module) {
    this.expandParents(this.module, node.id);
    this.treeControl.expand(node);
    window.scrollTo(document.getElementById(node.name).offsetLeft, document.getElementById(node.name).offsetTop);
  }

  expandParents(node: Module, id: number) {
    if (node.id === id) {
      this.treeControl.expand(node);
      return true;
    } else {
      if (node.blocks !== undefined) {
        for (let module of node.blocks) {
          if (this.expandParents(module, id)) {
            this.treeControl.expand(node);
            return true;
          }
        }
        return false;
      } else {
        return false;
      }
    }
  }

  viewLesson(lessonId: number) {
    if (this.loginService.isAdmin) {
      this.router.navigate(['/units/' + this.unitId + '/modules/' + this.moduleId + '/lessons/' + lessonId]);
    } else {
      this.router.navigate(['/units/' + this.courseId + '/modules/' + this.moduleId + '/lessons/' + lessonId]);
    }
  }

  addBlock(node: Module): void {
    this.bottomSheet.open(UnitsBlocksToolComponent, {
      data: node,
    }).afterDismissed().subscribe( () => {
      this.ngOnInit();
    });
  }

  deleteBlock(node: Module, id: number) {
    const parrentId = this.table.get(node);
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmText2, button1: this.button1, button2: this.button2}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 1) {
        this.moduleService.deleteBlock(parrentId, id).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }

}
