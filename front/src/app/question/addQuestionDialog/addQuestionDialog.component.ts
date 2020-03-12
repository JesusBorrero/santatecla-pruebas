import {Component, Inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from '@angular/material';
import {QuestionComponent} from '../question.component';
import {DefinitionQuestion} from '../definitionQuestion/definitionQuestion.model';
import {ListQuestion} from '../listQuestion/listQuestion.model';
import {TestQuestion} from '../testQuestion/testQuestion.model';
import {LoginService} from '../../auth/login.service';
import {Question} from '../question.model';
import {QuestionService} from '../question.service';

const QUESTION_TYPES = [
  {id: 'DefinitionQuestion', name: 'Definición'},
  {id: 'ListQuestion', name: 'Listado'},
  {id: 'TestQuestion', name: 'Test'},
];

export interface DialogData {
  isEditing: boolean;
  unitId: number;
  question: Question;
}

@Component({
  templateUrl: './addQuestionDialog.component.html',
  styleUrls: ['../question.component.css']
})

export class AddQuestionDialogComponent implements OnInit {

  questionTypes;

  definitionQuestion: DefinitionQuestion;
  listQuestion: ListQuestion;
  testQuestion: TestQuestion;

  // Add Question attributes
  subtype: string;
  questionInput: string;
  answerInput: string;
  possibleAnswers: Map<string, boolean>;
  bindedAnswerKeys: string[];
  correct: boolean;
  correctTestAnswerSelected: boolean;

  constructor(
    private router: Router,
    private questionService: QuestionService,
    public loginService: LoginService,
    private activatedRoute: ActivatedRoute,
    public dialogRef: MatDialogRef<QuestionComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  ngOnInit() {

    this.questionTypes = QUESTION_TYPES;

    // Add Question attributes
    this.subtype = 'DefinitionQuestion';
    this.resetAddQuestionForm();

    if (this.data.isEditing) {
      this.setEditQuestionForm();
    }

  }

  resetAddQuestionForm() {
    this.questionInput = '';
    this.answerInput = '';
    this.possibleAnswers = new Map();
    this.rechargeAnswersKeys();
    this.correct = false;
    this.correctTestAnswerSelected = false;
  }

  setEditQuestionForm() {
    this.subtype = this.data.question.subtype;
    this.questionInput = this.data.question.questionText;
    this.answerInput = '';
    this.correct = false;
    this.possibleAnswers = new Map();

    if (this.subtype === 'ListQuestion') {
      for (const answer of this.data.question.possibleAnswers) {
        this.possibleAnswers.set(answer, this.data.question.correctAnswers.includes(answer));
      }
    }

    if (this.subtype === 'TestQuestion') {
      for (const answer of this.data.question.possibleAnswers) {
        this.possibleAnswers.set(answer, answer === this.data.question.correctAnswer);
        if (!this.correctTestAnswerSelected) {
          this.correctTestAnswerSelected = answer === this.data.question.correctAnswer;
        }
      }
    }
    this.rechargeAnswersKeys();
  }

  setQuestion(subtype: string) {
    this.subtype = subtype;
    this.resetAddQuestionForm();
  }

  rechargeAnswersKeys() {
    this.bindedAnswerKeys = Array.from(this.possibleAnswers.keys());
  }

  sendQuestion() {
    switch (this.subtype) {
      case 'DefinitionQuestion': {
        this.sendDefinitionQuestion();
        break;
      }
      case 'ListQuestion': {
        this.sendListQuestion();
        break;
      }
      case 'TestQuestion': {
        this.sendTestQuestion();
        break;
      }
      default: {
        console.log('Not valid');
        break;
      }
    }
    this.dialogRef.close(1);
  }

  sendDefinitionQuestion() {
    if (this.questionInput === '') {
      // TODO
      console.log('error: inputs cannot be empty');
      return;
    }
    this.definitionQuestion = {
      questionText: this.questionInput,
      subtype: 'DefinitionQuestion'
    };

    if (!this.data.isEditing) {
      this.questionService.addUnitDefinitionQuestion(this.data.unitId, this.definitionQuestion).subscribe(
        (_) => {
          this.resetAddQuestionForm();
        },
        (err) => console.log(err)
      );
    } else {
      this.questionService.editUnitDefinitionQuestion(this.data.unitId, this.data.question.id, this.definitionQuestion).subscribe(
        (_) => {
          this.resetAddQuestionForm();
        },
        (err) => console.log(err)
      );
    }
  }

  sendListQuestion() {
    if (this.questionInput === '') {
      // TODO
      console.log('error: inputs cannot be empty');
      return;
    }
    let ca = [];
    this.possibleAnswers.forEach((value: boolean, key: string) => {
      if (value) {
        ca = ca.concat(key);
      }
    });
    this.listQuestion = {
      questionText: this.questionInput,
      subtype: 'ListQuestion',
      possibleAnswers: Array.from(this.possibleAnswers.keys()),
      correctAnswers: ca
    };

    if (!this.data.isEditing) {
      this.questionService.addUnitListQuestion(this.data.unitId, this.listQuestion).subscribe(
      (_) => {
          this.resetAddQuestionForm();
        },
        (err) => {
          console.log(err);
          this.ngOnInit();
        }
      );
    } else {
      this.questionService.editUnitListQuestion(this.data.unitId, this.data.question.id, this.listQuestion).subscribe(
        (_) => {
          this.resetAddQuestionForm();
        },
        (err) => {
          console.log(err);
          this.ngOnInit();
        }
      );
    }
  }

  sendTestQuestion() {
    if (this.questionInput === '') {
      // TODO
      this.openSnackBar('La pregunta no puede estar vacía', 'Entendido');
      console.log('error: inputs cannot be empty');
      return;
    }
    if (!this.correctTestAnswerSelected) {
      this.openSnackBar('Debe haber una respuesta correcta', 'Entendido');
      return;
    }
    let ca = [];
    this.possibleAnswers.forEach((value: boolean, key: string) => {
      if (value) {
        ca = ca.concat(key);
      }
    });
    this.testQuestion = {
      questionText: this.questionInput,
      subtype: 'TestQuestion',
      possibleAnswers: Array.from(this.possibleAnswers.keys()),
      correctAnswer: ca[0]
    };
    if (!this.data.isEditing) {
      this.questionService.addUnitTestQuestion(this.data.unitId, this.testQuestion).subscribe(
        (_) => {
          this.resetAddQuestionForm();
        },
        (err) => console.log(err)
      );
    } else {
      this.questionService.editUnitTestQuestion(this.data.unitId, this.data.question.id, this.testQuestion).subscribe(
        (_) => {
          this.resetAddQuestionForm();
        },
        (err) => console.log(err)
      );
    }
  }

  addPossibleListAnswer() {
    if (this.answerInput === '') {
      // TODO
      this.openSnackBar('La respuesta no puede estar vacía', 'Entendido');
      return;
    }
    if (this.possibleAnswers.has(this.answerInput)) {
      this.openSnackBar('La respuesta está repetida', 'Entendido');
      return;
    }
    this.possibleAnswers = this.possibleAnswers.set(this.answerInput, this.correct);
    this.rechargeAnswersKeys();
    this.answerInput = '';
  }

  addPossibleTestAnswer() {
    if (this.answerInput === '') {
      // TODO
      this.openSnackBar('La respuesta no puede estar vacía', 'Entendido');
      return;
    }
    if (this.possibleAnswers.has(this.answerInput)) {
      this.openSnackBar('La respuesta está repetida', 'Entendido');
      return;
    }
    if (!this.correctTestAnswerSelected && this.correct) {
      this.possibleAnswers.set(this.answerInput, true);
      this.correct = false;
      this.correctTestAnswerSelected = true;
    } else {
      this.possibleAnswers.set(this.answerInput, false);
    }
    this.rechargeAnswersKeys();
    this.answerInput = '';
  }

  deletePossibleAnswer(answer: string) {
    if (this.possibleAnswers.get(answer)) {
      this.correctTestAnswerSelected = false;
    }
    this.possibleAnswers.delete(answer);
    this.rechargeAnswersKeys();
  }

  onNoClick(): void {
    this.dialogRef.close(2);
  }

  changeTextArea(event: Event) {
    this.fitContent(event.target as HTMLTextAreaElement);
  }

  fitContent(textArea: HTMLTextAreaElement) {
    textArea.style.overflow = 'hidden';
    textArea.style.height = '0px';
    textArea.style.height = textArea.scrollHeight + 'px';
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }
}
