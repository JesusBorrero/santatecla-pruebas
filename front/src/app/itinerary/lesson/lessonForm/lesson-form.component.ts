import {Component, Inject} from '@angular/core';
import {Lesson} from '../lesson.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UnitComponent} from '../../../unit/unit.component';

export interface DialogData {
  itinerary: Lesson;
}

@Component({
  templateUrl: './lesson-form.component.html',
  styleUrls: ['./lesson-form.component.css']
})

export class LessonFormComponent {

  constructor(
    public dialogRef: MatDialogRef<UnitComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
