import {Component, OnInit, ViewChild} from '@angular/core';
import {Unit} from '../unit/unit.model';
import {ActivatedRoute, Router} from '@angular/router';
import {UnitService} from './unit.service';
import {TabService} from '../tab/tab.service';
import {Tab} from '../tab/tab.model';
import {CardComponent} from "../card/card.component";
import {QuestionComponent} from "../question/question.component";
import {LessonComponent} from "../itinerary/lesson/lesson.component";
import {ModuleComponent} from "../itinerary/module/module.component";

@Component({
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.css']
})

export class UnitComponent implements OnInit {

  unit: Unit;
  activeBreadcrumb = 0;
  showMenu = true;

  @ViewChild('cards') cardComponent: CardComponent;
  @ViewChild('questions') questionComponent: QuestionComponent;
  @ViewChild('lessons') lessonComponent: LessonComponent;
  @ViewChild('modules') moduleComponent: ModuleComponent;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private unitService: UnitService, private tabService: TabService) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      const unitId = params.unitId;
      this.unitService.getUnit(unitId).subscribe((data: Unit) => {
        this.tabService.addTab(new Tab('Unidad', unitId, data.name, unitId, null, null));
        this.unit = data;
        if (this.cardComponent) { this.cardComponent.ngOnInit(); }
        if (this.questionComponent) { this.questionComponent.ngOnInit(); }
        if (this.lessonComponent) { this.lessonComponent.ngOnInit(); }
        if (this.moduleComponent) { this.moduleComponent.ngOnInit(); }
      }, error => { console.log(error); });
    });
  }

  activateBreadcrumb(breadcrumb: number) {
    this.activeBreadcrumb = breadcrumb;
  }

  setShowMenu(showMenu: boolean) {
    this.showMenu = showMenu;
  }

}
