import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ListQuestion} from './listQuestion/listQuestion.model';
import {DefinitionQuestion} from './definitionQuestion/definitionQuestion.model';
import {TestQuestion} from './testQuestion/testQuestion.model';
import {DefinitionAnswer} from './definitionQuestion/definitionAnswer.model';
import {ListAnswer} from './listQuestion/listAnswer.model';
import {TestAnswer} from './testQuestion/testAnswer.model';
import {Unit} from '../unit/unit.model';

@Injectable()
export class QuestionService {

  baseUrl = '/api/units/';

  constructor(private http: HttpClient) {}

  getUnitQuestions(id: number) {
    return this.http.get(this.baseUrl + id + '/question');
  }

  getQuestion(id: number) {
    return this.http.get('/api/questions/' + id);
  }

  getUnitQuestion(unitID: number, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/' + questionID);
  }

  getUnitDefinitionQuestions(id: number) {
    return this.http.get(this.baseUrl + id + '/question/definition');
  }

  getUnitDefinitionQuestion(unitID: number, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID);
  }

  getUnitListQuestions(id: number) {
    return this.http.get(this.baseUrl + id + '/question/list');
  }

  getUnitListQuestion(unitID: number, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/list/' + questionID);
  }

  getUnitTestQuestions(id: number) {
    return this.http.get(this.baseUrl + id + '/question/test');
  }

  getUnitTestQuestion(unitID: number, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/test/' + questionID);
  }

  addUnitDefinitionQuestion(id: number, question: DefinitionQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<DefinitionQuestion>(this.baseUrl + id + '/question/definition', body, {headers});
  }

  editUnitDefinitionQuestion(unitID: number, questionID: number, question: DefinitionQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<DefinitionQuestion>(this.baseUrl + unitID + '/question/definition/' + questionID, body, {headers});
  }

  addUnitListQuestion(id: number, question: ListQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<ListQuestion>(this.baseUrl + id + '/question/list', body, {headers});
  }

  editUnitListQuestion(unitID: number, questionID: number, question: ListQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<ListQuestion>(this.baseUrl + unitID + '/question/list/' + questionID, body, {headers});
  }

  addUnitListAnswer(unitID, questionID: number, answer: ListAnswer) {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(this.baseUrl + unitID + '/question/list/' + questionID + '/answer', body, {headers});
  }

  addUnitTestQuestion(id: number, question: TestQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<TestQuestion>(this.baseUrl + id + '/question/test', body, {headers});
  }

  editUnitTestQuestion(unitID: number, questionID: number, question: TestQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<TestQuestion>(this.baseUrl + unitID + '/question/test/' + questionID, body, {headers});
  }

  addUnitTestAnswer(unitID, questionID: number, answer: TestAnswer) {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(this.baseUrl + unitID + '/question/test/' + questionID + '/answer', body, {headers});
  }

  deleteUnitQuestion(unitID, questionID: number) {
    return this.http.delete(this.baseUrl + unitID + '/question/' + questionID);
  }

  addUnitDefinitionAnswer(unitID, questionID: number, answer: DefinitionAnswer) {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer', body, {headers});
  }

  editUnitDefinitionAnswer(unitID, questionID, answerID: number, answer: DefinitionAnswer) {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer/' + answerID, body, {headers});
  }

  getUnitDefinitionAnswers(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer');
  }

  getUnitDefinitionAnswersCorrected(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer?corrected=true');
  }

  getUnitDefinitionAnswersNotCorrected(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer?corrected=false');
  }

  getQuestionCorrectCount(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/' + questionID + '/correctCount');
  }

  getQuestionWrongCount(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/' + questionID + '/wrongCount');
  }

  getTestQuestionWrongAnswerCount(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/test/' + questionID + '/chosenWrongAnswersCount');
  }

  getListQuestionWrongAnswerCount(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/list/' + questionID + '/chosenWrongAnswersCount');
  }

  getUncorrectedDefinitionAnswers(unitID, questionID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID + '/uncorrectedCount');
  }

  getDefinitionUserAnswers(unitID, questionID, userID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/definition/' + questionID + '/answer/user/' + userID);
  }

  getListUserAnswers(unitID, questionID, userID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/list/' + questionID + '/answer/user/' + userID);
  }

  getTestUserAnswers(unitID, questionID, userID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/test/' + questionID + '/answer/user/' + userID);
  }

  addBlockToQuestion(unitID, questionID, blockID: number,) {
    return this.http.get(this.baseUrl + unitID + '/question/' + questionID + '/addBlock/' + blockID);
  }

  deleteQuestionBlock(unitID, questionID, blockID: number) {
    return this.http.get(this.baseUrl + unitID + '/question/' + questionID + '/deleteBlock/' + blockID);
  }
}
