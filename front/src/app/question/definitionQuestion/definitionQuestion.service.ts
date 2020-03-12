import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DefinitionAnswer} from './definitionAnswer.model';
import {DefinitionQuestion} from './definitionQuestion.model';

@Injectable()
export class DefinitionQuestionService {
  constructor(private http: HttpClient) {
  }

  getDefinitionQuestion(id: number) {
    return this.http.get('api/definition/' + id);
  }

  addDefinitionQuestion(question: DefinitionQuestion) {
    const body = JSON.stringify(question);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<DefinitionQuestion>('/api/definition/', body, {headers});
  }

  addDefinitionAnswer(id: number, answer: DefinitionAnswer): Observable<DefinitionAnswer> {
    const body = JSON.stringify(answer);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<DefinitionAnswer>('/api/definition/' + id, body, {headers});
  }
}
