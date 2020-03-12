import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { ProgressService } from '../progress.service';
import {LoginService, User} from '../../auth/login.service';
import {Course} from '../../course/course.model';
import {UserResult} from '../items/userResult.model';
import {ActivatedRoute} from '@angular/router';
import {CourseService} from '../../course/course.service';
import {BrowserModule, DomSanitizer} from '@angular/platform-browser';
import {MatIconRegistry} from '@angular/material';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-module-progress',
  templateUrl: './module-progress.component.html',
  styleUrls: ['./module-progress.component.css']
})

export class ModuleProgressComponent implements OnInit {
  courseId: number;
  course: Course;
  moduleResults: UserResult[];
  columnsToDisplay = ['name', 'realization', 'average'];
  moduleResultsReady = false;
  histogram = [];
  xAxisLabel = 'Módulo';
  yAxisLabel = 'Media';
  showingModuleResults: UserResult[];

  constructor(private courseService: CourseService,
              public loginService: LoginService,
              private activatedRoute: ActivatedRoute,
              private progressService: ProgressService) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.courseId = params.courseId;
      this.courseService.getCourse(this.courseId).subscribe((data: Course) => {
        this.course = data;
      }, error => {console.log(error); });

      this.progressService.getModuleProgress(this.courseId).subscribe((data: UserResult[]) => {
        this.moduleResults = data;
        this.showingModuleResults = this.moduleResults;
        this.buildHistogramData();
        this.moduleResultsReady = true;
      }, error => {console.log(error); });
    });
  }

  buildHistogramData() {
    for (let module of this.moduleResults) {
      this.histogram.push({name: module.name, value: module.points[1]});
    }
  }

  applyFilterModule(value: string) {
    this.showingModuleResults = [];
    for (let result of this.moduleResults) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.showingModuleResults.push(result);
      }
    }
  }


}
