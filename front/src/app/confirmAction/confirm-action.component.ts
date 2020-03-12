import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Component, Inject} from '@angular/core';

export interface DialogConfirmData {
  confirmText: string;
  button1: string;
  button2: string;
}

@Component({
  templateUrl: 'confirm-action.component.html',
  styleUrls: ['./confirm-action.component.css']
})

export class ConfirmActionComponent {

  constructor(public dialogRef: MatDialogRef<ConfirmActionComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogConfirmData) {}

  onNoClick() {
    this.dialogRef.close(2);
  }

  saveChanges() {
    this.dialogRef.close(1);
  }

}
