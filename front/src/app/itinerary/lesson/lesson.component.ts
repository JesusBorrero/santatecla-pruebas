import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../auth/login.service';
import {Lesson} from './lesson.model';
import {LessonService} from './lesson.service';
import { Router, ActivatedRoute } from '@angular/router';
import {Unit} from '../../unit/unit.model';
import {UnitService} from '../../unit/unit.service';
import {LessonFormComponent} from './lessonForm/lesson-form.component';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmActionComponent} from '../../confirmAction/confirm-action.component';
import {UnitLessonService} from './unit-lesson.service';

@Component({
  selector: 'app-lessons',
  templateUrl: './lesson.component.html',
  styleUrls: ['./lesson.component.css']
})

export class LessonComponent implements OnInit {

  unit: Unit;
  unitId: number;

  lessons: Lesson[];
  lessonsResult: Lesson[];

  newLesson: Lesson;

  optionInfoPosition = 'after';

  confirmText = 'Se eliminará la lección permanentemente';
  button1 = 'Cancelar';
  button2 = 'Borrar';

  constructor(public loginService: LoginService,
              private lessonService: LessonService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private unitService: UnitService,
              private unitLessonService: UnitLessonService,
              public dialog: MatDialog) {
  }

  ngOnInit() {

    this.lessons = [];
    this.lessonsResult = [];

    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
    });

    this.unitService.getUnit(this.unitId).subscribe((data: Unit) => {
      this.unit = data;
      this.lessons = data.lessons;
      this.lessonsResult = this.lessons;
    }, error => {console.log(error); });

  }

  editLesson(lessonId: number) {
    this.router.navigate(['/units/' + this.unitId + '/lessons/' + lessonId]);
  }

  searchLesson(value: string) {
    this.lessonsResult = [];
    for (let result of this.lessons) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.lessonsResult.push(result);
      }
    }
  }

  addLesson(): void {
    this.newLesson = {name: ''};
    const dialogRef = this.dialog.open(LessonFormComponent, {
      data: {itinerary: this.newLesson}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.newLesson = result;
      if (typeof this.newLesson !== 'undefined') {
        if (this.newLesson.name !== '') {
          this.unitLessonService.addLesson(this.unitId, this.newLesson).subscribe((data) => {
            this.router.navigate(['/units/' + this.unitId + '/lessons/' + data.id]);
          });
        }
      }
    });
  }

  deleteLesson(lessonId: number) {
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmText, button1: this.button1, button2: this.button2}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 1) {
        this.unitLessonService.deleteLesson(this.unitId, lessonId).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }

}
