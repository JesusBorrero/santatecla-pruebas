import {Component, OnInit, ViewChild} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {Question} from './question.model';
import {QuestionService} from './question.service';
import {DefinitionQuestion} from './definitionQuestion/definitionQuestion.model';
import {ListQuestion} from './listQuestion/listQuestion.model';
import {TestQuestion} from './testQuestion/testQuestion.model';
import {Lesson} from '../itinerary/lesson/lesson.model';
import {LoginService} from '../auth/login.service';
import {MatDialog} from '@angular/material/dialog';
import {DefinitionAnswer} from './definitionQuestion/definitionAnswer.model';
import {AnswerQuestionDialogComponent} from './answerQuestionDialog/answerQuestionDialog.component';
import {ConfirmActionComponent} from '../confirmAction/confirm-action.component';
import {AddQuestionDialogComponent} from './addQuestionDialog/addQuestionDialog.component';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';

const QUESTION_TYPES = [
  {id: 'DefinitionQuestion', name: 'Definición'},
  {id: 'ListQuestion', name: 'Listado'},
  {id: 'TestQuestion', name: 'Test'},
];

@Component({
  selector: 'app-questions',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css']
})

export class QuestionComponent implements OnInit {

  displayedColumns: string[] = ['question', 'subtype', 'edit', 'delete'];
  dataSource;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  showSpinner = false;

  questions: Question[];
  definitionQuestions: DefinitionQuestion[];
  listQuestions: ListQuestion[];
  testQuestions: TestQuestion[];

  definitionQuestion: DefinitionQuestion;
  listQuestion: ListQuestion;
  testQuestion: TestQuestion;

  questionTypes: Map<string, string>;

  answerInput: string;

  confirmDialog = {
    text: 'Se eliminará el ejercicio permanentemente',
    button1: 'Cancelar',
    button2: 'Borrar'
  };

  unitId: number;
  itinerariesTabs: Lesson[];

  constructor(
    public loginService: LoginService,
    public dialog: MatDialog,
    private questionService: QuestionService,
    private activatedRoute: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit() {
    this.showSpinner = true;

    this.questions = [];
    this.questionTypes = new Map();
    for (const entry of QUESTION_TYPES) {
      this.questionTypes.set(entry.id, entry.name);
    }

    this.answerInput = '';

    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
    });

    // init data source
    this.dataSource = new MatTableDataSource<Question>();

    this.getQuestions();
    // this.getAllQuestions();
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getQuestions() {
    this.questionService.getUnitQuestions(this.unitId).subscribe((data: Question[]) => {
      this.questions = data;
      this.dataSource.data = data;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.showSpinner = false;
    });
  }

  // Get questions type by type
  getAllQuestions() {
    this.questionService.getUnitDefinitionQuestions(this.unitId).subscribe((data: DefinitionQuestion[]) => {
      this.definitionQuestions = data;
      this.questions = this.questions.concat(data);
      // question table
      this.dataSource = new MatTableDataSource(this.questions);
      this.dataSource.sort = this.sort;
      this.showSpinner = false;
    });

    this.questionService.getUnitListQuestions(this.unitId).subscribe((data: ListQuestion[]) => {
      this.listQuestions = data;
      this.questions = this.questions.concat(data);
      // question table
      this.dataSource = new MatTableDataSource(this.questions);
      this.dataSource.sort = this.sort;
      this.showSpinner = false;
    });

    this.questionService.getUnitTestQuestions(this.unitId).subscribe((data: TestQuestion[]) => {
      this.testQuestions = data;
      this.questions = this.questions.concat(data);
      // question table
      this.dataSource = new MatTableDataSource(this.questions);
      this.dataSource.sort = this.sort;
      this.showSpinner = false;
    });
  }

  editQuestion(questionID: number, subtype: string) {
    switch (subtype) {
      case 'DefinitionQuestion': {
        this.getDefinitionQuestion(questionID);
        break;
      }
      case 'ListQuestion': {
        this.getListQuestion(questionID);
        break;
      }
      case 'TestQuestion': {
        this.getTestQuestion(questionID);
        break;
      }
      default: {
        console.log('Not valid');
        break;
      }
    }
  }

  getDefinitionQuestion(questionID: number) {
    this.questionService.getUnitDefinitionQuestion(this.unitId, questionID).subscribe(
      (data: DefinitionQuestion) => {
        this.openEditQuestionDialog(data);
      }
    );
  }

  getListQuestion(questionID: number) {
    this.questionService.getUnitListQuestion(this.unitId, questionID).subscribe(
      (data: ListQuestion) => {
        this.openEditQuestionDialog(data);
      }
    );
  }

  getTestQuestion(questionID: number) {
    this.questionService.getUnitTestQuestion(this.unitId, questionID).subscribe(
      (data: TestQuestion) => {
        this.openEditQuestionDialog(data);
      }
    );
  }

  deleteQuestion(questionID: number) {
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmDialog.text, button1: this.confirmDialog.button1, button2: this.confirmDialog.button2}
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 1) {
        this.questionService.deleteUnitQuestion(this.unitId, questionID).subscribe(
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
    });
  }

  sendDefinitionAnswer(questionID: number) {
    if (this.answerInput === '') {
      // TODO
      console.log('error: inputs cannot be empty');
      return;
    }
    const answer: DefinitionAnswer = {
      answerText: this.answerInput,
      user: this.loginService.getCurrentUser(),
      unitId: this.unitId
    };
    this.questionService.addUnitDefinitionAnswer(this.unitId, questionID, answer).subscribe(
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

  openEditQuestionDialog(editingQuestion) {
    const dialogRef = this.dialog.open(AddQuestionDialogComponent, {
      width: '600px',
      data: {isEditing: true, unitId: this.unitId, question: editingQuestion}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 1) {
        this.ngOnInit();
      }
    });
  }

  openAddQuestionDialog() {
    const dialogRef = this.dialog.open(AddQuestionDialogComponent, {
      width: '600px',
      data: {isEditing: false, unitId: this.unitId}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 1) {
        this.ngOnInit();
      }
    });
  }

  openAnswerDialog(q: Question): void {
    const dialogRef = this.dialog.open(AnswerQuestionDialogComponent, {
      width: '250px',
      data: {question: q, answerInput: this.answerInput}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.answerInput = result;
      if ((typeof this.answerInput !== 'undefined') && (this.answerInput !== '')) {
        this.sendDefinitionAnswer(q.id);
      }
    });
  }
}
