import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TestAnswer} from './testAnswer.model';
import {DefinitionQuestion} from '../definitionQuestion/definitionQuestion.model';
import {TestQuestion} from './testQuestion.model';

@Injectable()
export class TestQuestionService {
  constructor(private http: HttpClient) {
  }

  getTestQuestion(id: number) {
    return this.http.get('api/test/' + id);
  }

  addTestQuestion(testQuestion: TestQuestion) {
    const body = JSON.stringify(testQuestion);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<DefinitionQuestion>('/api/test/', body, {headers});
  }

  addTestAnswer(id: number, answer: TestAnswer): Observable<TestAnswer> {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<TestAnswer>('/api/test/' + id, body, {headers});
  }

  getUserAnswers(questionId: number, userId: number) {
    return this.http.get('api/test/' + questionId + '/answer/user/' + userId);
  }
}
