import {Component, OnInit} from '@angular/core';
import {Course} from './course.model';
import {LoginService} from '../auth/login.service';
import {CourseService} from './course.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NestedTreeControl} from '@angular/cdk/tree';
import {Module} from '../itinerary/module/module.model';
import {MatDialog, MatSnackBar, MatTreeNestedDataSource} from '@angular/material';
import {TabService} from '../tab/tab.service';
import {NewCourseComponent} from './newCourse.component';

@Component({
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})

export class CourseComponent implements OnInit {
  course: Course;
  id: number;

  treeControl = new NestedTreeControl<Module>(node => !!node && node.blocks);
  dataSource = new MatTreeNestedDataSource<Module>();

  showMenu = true;
  activeTab = 0;

  constructor(public loginService: LoginService,
              private courseService: CourseService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private tabService: TabService,
              private snackBar: MatSnackBar,
              public dialog: MatDialog) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.id = params.courseId;
      if (this.loginService.isAdmin) {
        this.courseService.getCourse(this.id).subscribe((data: Course) => {
          this.course = data;
          this.dataSource.data = this.course.module.blocks;
          this.tabService.setCourse(this.course.name, this.course.id);
        }, error => {
          console.log(error);
        });
      } else {
        this.courseService.addNewStudent(this.id, this.loginService.getCurrentUser()).subscribe((data: any) => {
          this.router.navigate(['/courses']);
        }, error => { console.log(error); } );
      }
    });
  }

  hasChild = (_: number, node: Module) => !!node && !!node.blocks && node.blocks.length > 0;

  private activateTab(tab: number) {
    this.activeTab = tab;
  }

  private setShowMenu(showMenu: boolean) {
    this.showMenu = showMenu;
  }

  openEditCourseDialog() {
    const dialogRef = this.dialog.open(NewCourseComponent, {
      width: '600px',
      data: {data: this.course.id}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }

  copyUrl() {
    const url = window.location.href;
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', (url));
      e.preventDefault();
      document.removeEventListener('copy', null);
    });
    document.execCommand('copy');
  }

  addStudents() {
    this.copyUrl();
    this.snackBar.open('La URL para acceder al curso ha sido copiada al portapapeles', 'Entendido', {
      duration: 4000,
    });
  }
}
