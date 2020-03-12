import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';

@Injectable()
export class ProgressService {
  constructor(private http: HttpClient) {}

  getModuleProgress(courseId: number) {
    return this.http.get('api/course/' + courseId + '/module/progress');
  }

  getClassProgress(courseId: number) {
    return this.http.get('api/course/' + courseId + '/students/progress');
  }

  getClassGradesGrouped(courseId: number) {
    return this.http.get('api/course/' + courseId + '/students/gradesGroup');
  }
}
