import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lesson } from './lesson.model';

@Injectable()
export class LessonService {
  constructor(private http: HttpClient) { }

  getLessons() {
    return this.http.get('/api/lessons/');
  }

  getLesson(id: number) {
    return this.http.get('/api/lessons/' + id);
  }

  updateLesson(lesson: Lesson): Observable<Lesson> {
    const body = JSON.stringify(lesson);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<Lesson>('/api/lessons/' + lesson.id , body, {headers});
  }

  removeSlide(itinerary: Lesson, id: number) {
    return this.http.delete('/api/lessons/' + itinerary.id + '/slide/' + id);
  }

}
