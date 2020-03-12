import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Component, Inject} from '@angular/core';

export interface DialogData {
  name: string;
}

@Component({
  templateUrl: 'module-rename.component.html',
  styleUrls: ['./module-rename.component.css']
})

export class ModuleRenameComponent {

  constructor(public dialogRef: MatDialogRef<ModuleRenameComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick() {
    this.dialogRef.close();
  }

  saveChanges() {
    this.dialogRef.close(this.data.name);
  }

}
