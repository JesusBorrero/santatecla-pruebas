import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UnitComponent} from '../../../unit/unit.component';
import {Module} from '../module.model';

export interface DialogData {
  itinerary: Module;
}

@Component({
  templateUrl: './module-form.component.html',
  styleUrls: ['./module-form.component.css']
})

export class ModuleFormComponent {

  constructor(
    public dialogRef: MatDialogRef<UnitComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
