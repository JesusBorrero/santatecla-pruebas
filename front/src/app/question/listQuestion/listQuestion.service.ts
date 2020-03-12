import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ListQuestion} from './listQuestion.model';
import {ListAnswer} from './listAnswer.model';
import {DefinitionQuestion} from '../definitionQuestion/definitionQuestion.model';

@Injectable()
export class ListQuestionService {
  constructor(private http: HttpClient) {
  }

  getListQuestion(id: number) {
    return this.http.get('api/list/' + id);
  }

  addListQuestion(listQuestion: ListQuestion) {
    const body = JSON.stringify(listQuestion);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<DefinitionQuestion>('/api/list/', body, {headers});
  }

  addAnswer(id: number, listAnswer: ListAnswer): Observable<ListQuestion> {
    const body = JSON.stringify(listAnswer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<ListQuestion>('api/list/' + id, body, {headers});
  }

  getUserAnswers(questionId: number, userId: number) {
    return this.http.get('api/list/' + questionId + '/answer/user/' + userId);
  }
}
