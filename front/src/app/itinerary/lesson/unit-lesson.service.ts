import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Lesson } from './lesson.model';

@Injectable()
export class UnitLessonService {

  baseUrl = '/api/units/';

  constructor(private http: HttpClient) {}

  getSlideFormLesson(slideId: number, lessonId: number, unitId: number) {
    return this.http.get(this.baseUrl + unitId + '/lessons/' + lessonId + '/slides/' + slideId);
  }

  addLesson(unitId: number, lesson: Lesson) {
    const body = JSON.stringify(lesson);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<Lesson>(this.baseUrl + unitId + '/lessons', body, { headers });
  }

  deleteLesson(unitId: number, lessonId: number) {
    return this.http.delete<Lesson>(this.baseUrl + unitId + '/lessons/' + lessonId);
  }

}
