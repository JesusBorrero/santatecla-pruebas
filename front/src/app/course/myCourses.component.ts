import {Component, OnInit} from '@angular/core';
import {LoginService} from '../auth/login.service';
import {Course} from './course.model';
import {Router} from '@angular/router';
import {TabService} from '../tab/tab.service';
import {TdDialogService} from '@covalent/core';
import {MatDialog} from '@angular/material';
import {NewCourseComponent} from './newCourse.component';
import {CourseService} from './course.service';

@Component({
  templateUrl: './myCourses.component.html',
  styleUrls: ['./course.component.css']
})

export class MyCoursesComponent implements OnInit {
  courses: Course[];
  searchField = '';
  showingCourses: Course[];

  constructor(public loginService: LoginService,
              private courseService: CourseService,
              private router: Router,
              private tabService: TabService,
              private dialogService: TdDialogService,
              public dialog: MatDialog) {
    this.courses = [];
    this.showingCourses = [];
  }

  ngOnInit() {
    this.tabService.setCourses();
    if (this.loginService.isAdmin) {
      this.courseService.getTeacherCourses(this.loginService.getCurrentUser().id).subscribe((data: Course[]) => {
        this.courses = data;
        this.showingCourses = this.courses;
      }, error => {console.log(error); });
    } else {
      this.courseService.getUserCourses(this.loginService.getCurrentUser().id).subscribe((data: Course[]) => {
        this.courses = data;
        this.showingCourses = this.courses;
      }, error => {console.log(error); });
    }
  }

  deleteCourse(course: Course) {
    this.dialogService.openConfirm({
      message: '¿Seguro que desea eliminar el curso ' + course.name + ' ?',
      title: 'Confirmación'
    }).afterClosed().subscribe((accept: boolean) => {
      if (accept) {
        this.courseService.deleteCourse(course.id).subscribe((_) => {
          this.courses.splice(this.courses.indexOf(course), 1);
        }, error => {console.log(error); });
      }
    });
  }

  search() {
    if (this.searchField !== '') {
      this.courseService.searchByNameContaining(this.searchField).subscribe((data: Course[]) => {
        this.showingCourses = data;
      }, error => {
        console.log(error);
      });
    } else {
      this.showingCourses = this.courses;
    }
  }

  openAddCourseDialog() {
    const dialogRef = this.dialog.open(NewCourseComponent, {
      width: '600px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      if (result === 1) {
        this.ngOnInit();
      }
    });
  }

}
