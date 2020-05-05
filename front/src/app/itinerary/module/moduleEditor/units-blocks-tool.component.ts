import {Component, Inject, OnInit, Optional} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Unit} from '../../../unit/unit.model';
import {UnitService} from '../../../unit/unit.service';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import { ClipboardService } from 'ngx-clipboard';
import {Block} from '../../block.model';
import {ModuleService} from '../module.service';
import {LoginService} from '../../../auth/login.service';
import {MatSnackBar} from '@angular/material';


@Component({
  templateUrl: './units-blocks-tool.component.html',
  styleUrls: ['./units-blocks-tool.component.css']
})

export class UnitsBlocksToolComponent implements OnInit {

  units: Unit[];
  unitsResult: Unit[];

  copyInfoPosition = 'after';

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private unitService: UnitService,
              private moduleService: ModuleService,
              private bottomSheetRef: MatBottomSheetRef<UnitsBlocksToolComponent>,
              public loginService: LoginService,
              private clipboardService: ClipboardService,
              private snackBar: MatSnackBar,
              @Optional() @Inject(MAT_BOTTOM_SHEET_DATA) public data: any) {
  }

  ngOnInit() {
    this.unitService.getUnits().subscribe((data: Unit[]) => {
      this.units = [];
      this.unitsResult = [];
      data.forEach((unit: Unit) => {
        this.units.push({
          id: unit.id,
          name: unit.name,
          lessons: unit.lessons,
          modules: unit.modules
        });
        this.unitsResult = this.units;
      });
    });
  }

  applyFilterUnits(value: string) {
    this.unitsResult = [];
    for (let result of this.units) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.unitsResult.push(result);
      }
    }
  }

  addBlockInToModule(event: MouseEvent, block: Block): void {
    this.moduleService.addBlock(this.data.id, block).subscribe( () => {
      this.bottomSheetRef.dismiss();
      event.preventDefault();
    }, error => {
      this.snackBar.open('No es posible introducir este itinerario porque contiene el mismo en el que se está intentando insertar o alguno de sus padres.',
        'Entendido', {
      duration: 5000,
    }); });
  }

  addModuleIntoCourse(event: MouseEvent, block: Block) {
    this.bottomSheetRef.dismiss(block);
    event.preventDefault();
  }

}
