import {Component, OnInit} from '@angular/core';
import { ProgressService } from '../progress.service';
import {LoginService} from '../../auth/login.service';
import {Course} from '../../course/course.model';
import {ActivatedRoute} from '@angular/router';
import {CourseService} from '../../course/course.service';
import {ProgressNode} from '../items/progressNode.model';

@Component({
  selector: 'app-module-progress',
  templateUrl: './module-progress.component.html',
  styleUrls: ['./module-progress.component.css']
})

export class ModuleProgressComponent implements OnInit {
  courseId: number;
  course: Course;
  moduleResults: ProgressNode;

  treeTableOptions = {verticalSeparator: true, capitalisedHeader: true, highlightRowOnHover: true,
    customColumnOrder: ['nombre', 'realizacion', 'media']};

  moduleResultsReady = false;

  histogram = [];
  xAxisLabel = 'Módulo';
  yAxisLabel = 'Media';

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

      this.progressService.getModuleProgress(this.courseId).subscribe((data: ProgressNode) => {
        this.moduleResults = data;
        this.buildHistogramData();
        this.moduleResultsReady = true;
      }, error => {console.log(error); });
    });
  }

  buildHistogramData() {
    for (let child of this.moduleResults.children) {
      this.histogram.push({name: child.value.nombre, value: child.value.media});
    }
  }


}
