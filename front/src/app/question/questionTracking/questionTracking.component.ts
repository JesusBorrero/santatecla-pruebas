import {Component, OnInit, ViewChild} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {QuestionService} from '../question.service';
import {Question} from '../question.model';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {UnitService} from '../../unit/unit.service';
import {DefinitionAnswer} from '../definitionQuestion/definitionAnswer.model';
import {Tab} from '../../tab/tab.model';
import {TabService} from '../../tab/tab.service';


@Component({
  selector: 'app-question-tracking',
  templateUrl: './questionTracking.component.html',
  styleUrls: ['../question.component.css']
})

export class QuestionTrackingComponent implements OnInit {
  unitId: number;
  questionId: number;
  questionType: string;

  question: Question;
  unitName: string;

  displayedColumnsNotCorrected: string[] = ['answer', 'correct', 'wrong'];
  dataSourceNotCorrected;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumnsCorrected: string[] = ['result', 'answer'];
  dataSourceCorrected;

  correctCount: number;
  wrongCount: number;
  uncorrectedCount = 0;

  resultsReady = false;

  pieChartResults = [];
  barChartResults = [];

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private questionService: QuestionService,
    private unitService: UnitService,
    public tabService: TabService) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
      this.questionId = params.questionId;
      this.questionType = params.questionType;

      if (this.questionType === 'TestQuestion') {
        this.getCorrectWrongAnswerCount();
        this.questionService.getTestQuestionWrongAnswerCount(this.unitId, this.questionId).subscribe((data: any) => {
          for (const element of data) {
            this.barChartResults.push({name: element[0], value: element[1]});
          }
        }, error => { console.log(error); });

        this.questionService.getUnitTestQuestion(this.unitId, this.questionId).subscribe((unitTestQuestion: any) => {
          this.question = unitTestQuestion;
          this.unitService.getUnitName(this.unitId).subscribe((unitName: any) => {
            this.unitName = unitName.response;
            this.tabService.addTab(new Tab('Unidad', +this.unitId, this.unitName, '' + this.unitId, null, null));
            this.tabService.updateActiveTabLink('TestQuestion', this.questionId, '', '' + this.unitId, null, null);
          }, error => { console.log(error); });
        }, error => { console.log(error); });
      } else if (this.questionType === 'ListQuestion') {
        this.getCorrectWrongAnswerCount();
        this.questionService.getListQuestionWrongAnswerCount(this.unitId, this.questionId).subscribe((data: any) => {
          for (const element of data) {
            this.barChartResults.push({name: element[0], value: element[1]});
          }
        }, error => { console.log(error); });

        this.questionService.getUnitListQuestion(this.unitId, this.questionId).subscribe((unitListQuestion: any) => {
          this.question = unitListQuestion;
          this.unitService.getUnitName(this.unitId).subscribe((unitName: any) => {
            this.unitName = unitName.response;
            this.tabService.addTab(new Tab('Unidad', +this.unitId, this.unitName, '' + this.unitId, null, null));
            this.tabService.updateActiveTabLink('ListQuestion', this.questionId, '', '' + this.unitId, null, null);
          }, error => { console.log(error); });
        }, error => { console.log(error); });
      } else if (this.questionType === 'DefinitionQuestion') {

        this.questionService.getQuestionCorrectCount(this.unitId, this.questionId).subscribe((questionCorrectCount: number) => {
          this.correctCount = questionCorrectCount;
          this.questionService.getQuestionWrongCount(this.unitId, this.questionId).subscribe((questionWrongCount: number) => {
            this.wrongCount = questionWrongCount;
            this.questionService.getUncorrectedDefinitionAnswers(this.unitId, this.questionId).subscribe((uncorrectedDefinitionAnswers: number) => {
              this.uncorrectedCount = uncorrectedDefinitionAnswers;
              this.buildCorrectWrongChart();
              this.resultsReady = true;
            }, error => { console.log(error); });
          }, error => { console.log(error); });
        }, error => { console.log(error); });

        // init data source
        this.dataSourceNotCorrected = new MatTableDataSource<DefinitionAnswer>();
        this.dataSourceCorrected = new MatTableDataSource<DefinitionAnswer>();

        this.questionService.getUnitDefinitionQuestion(this.unitId, this.questionId).subscribe((unitDefinitionQuestion: any) => {
          this.question = unitDefinitionQuestion;
          this.unitService.getUnitName(this.unitId).subscribe((unitName: any) => {
            this.unitName = unitName.response;
            this.tabService.addTab(new Tab('Unidad', +this.unitId, this.unitName, '' + this.unitId, null, null));
            this.tabService.updateActiveTabLink('DefinitionQuestion', this.questionId, '', '' + this.unitId, null, null);
          }, error => { console.log(error); });
        }, error => { console.log(error); });

        this.questionService.getUnitDefinitionAnswersNotCorrected(this.unitId, this.questionId).subscribe((data: DefinitionAnswer[]) => {
          this.dataSourceNotCorrected.data = data;
        }, error => { console.log(error); });

        this.questionService.getUnitDefinitionAnswersCorrected(this.unitId, this.questionId).subscribe((data: DefinitionAnswer[]) => {
          this.dataSourceCorrected.data = data;
        }, error => { console.log(error); });
      }
    });
  }

  getUnitNameAndSetTab() {
    this.unitService.getUnitName(this.unitId).subscribe((data: any) => {
      this.unitName = data.response;
    }, error => { console.log(error); });
  }

  buildCorrectWrongChart() {
    this.pieChartResults = [];
    this.pieChartResults.push({name: 'Respuestas Correctas', value: this.correctCount});
    this.pieChartResults.push({name: 'Respuestas Incorrectas', value: this.wrongCount});
    this.pieChartResults.push({name: 'Respuestas Sin Corregir', value: this.uncorrectedCount});
  }

  getCorrectWrongAnswerCount() {
    this.questionService.getQuestionCorrectCount(this.unitId, this.questionId).subscribe((questionCorrectCount: number) => {
      this.correctCount = questionCorrectCount;
      this.questionService.getQuestionWrongCount(this.unitId, this.questionId).subscribe((questionWrongCount: number) => {
        this.wrongCount = questionWrongCount;
        this.resultsReady = true;
        this.buildCorrectWrongChart();
      }, error => { console.log(error); });
    }, error => { console.log(error); });
  }

  correctAnswer(answer: DefinitionAnswer) {
    answer.correct = true;
    answer.corrected = true;
    this.questionService.editUnitDefinitionAnswer(this.unitId, this.questionId, answer.id, answer).subscribe(
      (_) => {
        // TODO Remove it
        this.ngOnInit();
      },
        (error) => {
          console.log(error);
          this.ngOnInit();
        }
    );
  }

  wrongAnswer(answer: DefinitionAnswer) {
    answer.correct = false;
    answer.corrected = true;
    this.questionService.editUnitDefinitionAnswer(this.unitId, this.questionId, answer.id, answer).subscribe(
      (_) => {
        // TODO Remove it
        this.ngOnInit();
      },
      (error) => {
        console.log(error);
        this.ngOnInit();
      }
    );
  }

  applyFilterCorrected(filterValue: string) {
    this.dataSourceCorrected.filter = filterValue.trim().toLowerCase();
  }

  applyFilterNotCorrected(filterValue: string) {
    this.dataSourceNotCorrected.filter = filterValue.trim().toLowerCase();
  }
}
