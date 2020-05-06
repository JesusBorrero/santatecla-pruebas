import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DefinitionAnswer} from '../definitionQuestion/definitionAnswer.model';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from '../../auth/login.service';
import {UnitService} from '../../unit/unit.service';
import {DefinitionQuestionService} from '../definitionQuestion/definitionQuestion.service';
import {Question} from '../question.model';
import {ListAnswer} from '../listQuestion/listAnswer.model';
import {TestAnswer} from '../testQuestion/testAnswer.model';
import {QuestionService} from '../question.service';
import {ListQuestionService} from '../listQuestion/listQuestion.service';
import {TestQuestionService} from '../testQuestion/testQuestion.service';
import {LessonEditorComponent} from '../../itinerary/lesson/lessonEditor/lesson-editor.component';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

const QUESTION_TYPES = [
  {id: 'DefinitionQuestion', name: 'Definición'},
  {id: 'ListQuestion', name: 'Listado'},
  {id: 'TestQuestion', name: 'Test'},
];

export interface DialogData {
  unitId: number;
  blockId: number;
  courseId: number;
  question: Question;
}

@Component({
  templateUrl: './answerQuestionDialog.component.html',
  styleUrls: ['../question.component.css']
})

export class AnswerQuestionDialogComponent implements OnInit {

  questionTypes;

  definitionAnswer: DefinitionAnswer;
  listAnswer: ListAnswer;
  testAnswer: TestAnswer;

  definitionAnswers: DefinitionAnswer[];
  listAnswers: ListAnswer[];
  testAnswers: TestAnswer[];

  subtype: string;
  spinner: boolean;

  // Form attributes
  availableListAnswers: string[];
  chosenListAnswers: string[];
  questionDone: boolean;
  answerSent: boolean;
  answerSelected: string;

  unitId: number;

  constructor(
    private router: Router,
    private unitService: UnitService,
    private questionService: QuestionService,
    private definitionQuestionService: DefinitionQuestionService,
    private listQuestionService: ListQuestionService,
    private testQuestionService: TestQuestionService,
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    public dialogRef: MatDialogRef<LessonEditorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  ngOnInit() {
    this.questionTypes = QUESTION_TYPES;
    this.unitId = this.data.unitId;
    this.spinner = true;
    this.initAnswers();
    this.getPreviousUserAnswers();
  }

  initAnswers() {
    this.definitionAnswer = {answerText: ''};
    this.listAnswer = {answer: [], correct: false};
    this.testAnswer = {answerText: '', correct: false};
    this.definitionAnswers = [];
    this.listAnswers = [];
    this.testAnswers = [];

    this.subtype = this.data.question.subtype;
    this.availableListAnswers = this.data.question.possibleAnswers;
    this.chosenListAnswers = [];
    this.answerSent = false;
    this.answerSelected = '';
  }

  private updateQuestionDone() {
    this.questionDone = this.definitionAnswers.length > 0 || this.listAnswers.length > 0 || this.testAnswers.length > 0;
  }

  getPreviousUserAnswers() {
    switch (this.subtype) {
      case 'DefinitionQuestion': {
        this.questionService.getDefinitionUserAnswers(this.unitId, this.data.question.id, this.loginService.getCurrentUser().id,
          this.data.blockId, this.data.courseId).subscribe(
          (data: DefinitionAnswer[]) => {
            this.definitionAnswers = data;
            this.updateQuestionDone();
            this.spinner = false;
          });
        break;
      }
      case 'ListQuestion': {
        this.questionService.getListUserAnswers(this.unitId, this.data.question.id, this.loginService.getCurrentUser().id,
          this.data.blockId, this.data.courseId).subscribe(
          (data: ListAnswer[]) => {
            this.listAnswers = data;
            this.updateQuestionDone();
            this.spinner = false;
          });
        break;
      }
      case 'TestQuestion': {
        this.questionService.getTestUserAnswers(this.unitId, this.data.question.id, this.loginService.getCurrentUser().id,
          this.data.blockId, this.data.courseId).subscribe(
          (data: TestAnswer[]) => {
            this.testAnswers = data;
            this.updateQuestionDone();
            this.spinner = false;
          });
        break;
      }
      default: {
        console.log('Not valid');
        break;
      }
    }
  }

  sendAnswer() {
    switch (this.subtype) {
      case 'DefinitionQuestion': {
        this.sendDefinitionAnswer();
        break;
      }
      case 'ListQuestion': {
        this.sendListAnswer();
        break;
      }
      case 'TestQuestion': {
        this.sendTestAnswer();
        break;
      }
      default: {
        console.log('Not valid');
        break;
      }
    }
  }

  closeDialog() {
    this.dialogRef.close(1);
  }

  sendDefinitionAnswer() {
    if (this.definitionAnswer.answerText === '') {
      // TODO
      console.log('error: inputs cannot be empty');
      return;
    }

    this.definitionAnswer = {
      answerText: this.definitionAnswer.answerText,
      correct: false,
      corrected: false,
      justification: '',
      unitId: this.unitId,
      blockId: this.data.blockId,
      courseId: this.data.courseId,
      user: this.loginService.getCurrentUser()
    };

    this.questionService.addUnitDefinitionAnswer(this.unitId, this.data.question.id, this.definitionAnswer).subscribe(
      () => {
        this.ngOnInit();
        this.answerSent = true;
      },
      (err) => console.log(err)
    );
  }

  sendListAnswer() {
    if (this.chosenListAnswers.length === 0) {
      // TODO
      console.log('error: answer cannot be empty');
      return;
    }

    const answersToCompare1 = this.chosenListAnswers;
    const answersToCompare2 = this.data.question.correctAnswers;
    answersToCompare1.sort();
    answersToCompare2.sort();
    const isCorrect = JSON.stringify(answersToCompare1) === JSON.stringify(answersToCompare2);

    this.listAnswer = {
      answer: this.chosenListAnswers,
      correct: isCorrect,
      unitId: this.unitId,
      blockId: this.data.blockId,
      courseId: this.data.courseId,
      user: this.loginService.getCurrentUser()
    };

    this.questionService.addUnitListAnswer(this.unitId, this.data.question.id, this.listAnswer).subscribe(
      () => {
        this.ngOnInit();
        this.answerSent = true;
      },
      (err) => console.log(err)
    );
  }

  sendTestAnswer() {
    if (this.answerSelected === '') {
      // TODO
      console.log('error: answer cannot be empty');
      return;
    }

    const isCorrect = this.answerSelected === this.data.question.correctAnswer;

    this.testAnswer = {
      answerText: this.answerSelected,
      correct: isCorrect,
      unitId: this.unitId,
      blockId: this.data.blockId,
      courseId: this.data.courseId,
      user: this.loginService.getCurrentUser()
    };

    this.questionService.addUnitTestAnswer(this.unitId, this.data.question.id, this.testAnswer).subscribe(
      () => {
        this.ngOnInit();
        this.answerSent = true;
      },
      (err) => console.log(err)
    );
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  changeTextArea(event: Event) {
    this.fitContent(event.target as HTMLTextAreaElement);
  }

  fitContent(textArea: HTMLTextAreaElement) {
    textArea.style.overflow = 'hidden';
    textArea.style.height = '0px';
    textArea.style.height = textArea.scrollHeight + 'px';
  }

  onNoClick(): void {
    this.dialogRef.close(2);
  }
}
