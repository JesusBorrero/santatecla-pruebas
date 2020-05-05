import {Component, Inject, OnInit} from '@angular/core';
import {LoginService, User} from '../auth/login.service';
import {Course} from './course.model';
import {ActivatedRoute, Router} from '@angular/router';
import {UnitService} from '../unit/unit.service';
import {Module} from '../itinerary/module/module.model';
import {MAT_DIALOG_DATA, MatBottomSheet, MatDialog, MatDialogRef, MatSnackBar} from '@angular/material';
import {UnitsBlocksToolComponent} from '../itinerary/module/moduleEditor/units-blocks-tool.component';
import {MyCoursesComponent} from './myCourses.component';
import {CourseComponent} from './course.component';
import {CourseService} from './course.service';

@Component({
  templateUrl: './newCourse.component.html',
  styleUrls: ['./course.component.css']
})

export class NewCourseComponent implements OnInit {
  courseId: number;
  courseName = '';
  courseDescription = '';
  chosenModule: Module;
  course: Course;
  showSpinner = false;

  constructor(private courseService: CourseService,
              public loginService: LoginService, private routing: Router,
              private activatedRoute: ActivatedRoute,
              private unitService: UnitService,
              private bottomSheet: MatBottomSheet,
              private myCoursesDialogRef: MatDialogRef<MyCoursesComponent>,
              private courseDialogRef: MatDialogRef<CourseComponent>,
              private snackBar: MatSnackBar,
              public dialog: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    if (this.data.data !== undefined) {
      this.courseId = this.data.data;

      this.courseService.getCourse(this.courseId).subscribe((data: Course) => {
        this.course = data;
        this.courseName = this.course.name;
        this.courseDescription = this.course.description;
        this.chosenModule = this.course.module;
      }, error => {
        console.log(error);
      });
    }
  }

  save() {
    if (this.courseName === '' || this.courseName === undefined) {
      this.openError('El nombre del curso no puede estar vacío', 'Entendido');
      return;
    }
    if (this.courseDescription === '' || this.courseDescription === undefined) {
      this.openError('La descripción del curso no puede estar vacía', 'Entendido');
      return;
    }
    if (this.chosenModule === undefined) {
      this.openError('Debes elegir un itinerario para el curso', 'Entendido');
      return;
    }

    if (this.courseId === undefined) {
      this.course = {name : this.courseName, description: this.courseDescription};
      this.course.teacher = this.loginService.getCurrentUser();
      this.course.module = this.chosenModule;
      this.courseService.postCourse(this.course).subscribe((data: Course) => {
        this.copyUrl(data.id);
        this.openError('La URL para acceder al curso ha sido copiada al portapapeles', 'Entendido');
        this.myCoursesDialogRef.close(1);
      }, error => {console.log(error); } );

    } else {
      this.course.name = this.courseName;
      this.course.description = this.courseDescription;
      this.course.teacher = this.loginService.getCurrentUser();
      this.course.module = this.chosenModule;
      this.showSpinner = true;
      this.courseService.putCourse(this.course, this.courseId).subscribe((data: Course) => {
        this.courseDialogRef.close(1);
        this.showSpinner = false;
      }, error => {console.log(error); } );
    }
  }

  cancel() {
    if (this.courseId === undefined) {
      this.myCoursesDialogRef.close(2);
    } else {
      this.courseDialogRef.close(2);
    }
  }

  addModule() {
    this.bottomSheet.open(UnitsBlocksToolComponent, {}).afterDismissed().subscribe((data) => {
      this.chosenModule = data;
    });
  }

  openError(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  copyUrl(id: number) {
    const url = window.location.href.substring(0, window.location.href.length - 1) + '/' + id;
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', (url));
      e.preventDefault();
      document.removeEventListener('copy', null);
    });
    document.execCommand('copy');
  }

}
