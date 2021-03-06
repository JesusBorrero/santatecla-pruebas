import {Component, ElementRef, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Unit} from '../../../unit/unit.model';
import {UnitService} from '../../../unit/unit.service';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import { ClipboardService } from 'ngx-clipboard';
import {LoginService} from '../../../auth/login.service';
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  templateUrl: './units-questions-tool.component.html',
  styleUrls: ['./units-questions-tool.component.css']
})

export class UnitsQuestionsToolComponent implements OnInit {

  @ViewChild('input') input: ElementRef;
  showSpinner = false;

  units: Unit[];
  unitsResult: Unit[];

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private unitService: UnitService,
              private bottomSheetRef: MatBottomSheetRef<UnitsQuestionsToolComponent>,
              public loginService: LoginService,
              private clipboardService: ClipboardService,
              private snackBar: MatSnackBar,
              @Optional() @Inject(MAT_BOTTOM_SHEET_DATA) public data: any) {
  }

  ngOnInit() {
    this.showSpinner = true;
    this.unitService.getUnits().subscribe((data: Unit[]) => {
      this.units = [];
      this.unitsResult = [];
      data.forEach((unit: Unit) => {
        this.units.push({
          id: unit.id,
          name: unit.name,
          definitionQuestions: unit.definitionQuestions,
          listQuestions: unit.listQuestions,
          testQuestions: unit.testQuestions
        });
        this.unitsResult = this.units;
        this.showSpinner = false;
        this.input.nativeElement.focus();
      });
    });
  }

  applyFilterUnits(value: string) {
    this.showSpinner = true;
    this.unitsResult = [];
    for (let result of this.units) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.unitsResult.push(result);
      }
    }
    this.showSpinner = false;
  }

  openLink(event: MouseEvent, text: string): void {
    this.clipboardService.copyFromContent(text);
    this.snackBar.open('El insert ha sido copiado al portapapeles', 'Entendido', {
      duration: 3000,
    });
    this.data = text;
    this.bottomSheetRef.dismiss(this.data);
    event.preventDefault();
  }

}
