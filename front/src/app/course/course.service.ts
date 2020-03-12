import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {User} from '../auth/login.service';
import {Course} from './course.model';

@Injectable()
export class CourseService {
  constructor(private http: HttpClient) {}

  getCourse(id: number) {
    return this.http.get('/api/course/' + id);
  }

  addNewStudent(id: number, student: User) {
    const body = JSON.stringify(student);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<User>('/api/course/' + id + '/students/', body, {headers});
  }

  getUserCourses(id: number) {
    return this.http.get('/api/course/user/' + id);
  }

  getTeacherCourses(id: number) {
    return this.http.get('/api/course/teacher/' + id);
  }

  deleteCourse(id: number) {
    return this.http.delete('api/course/' + id);
  }

  searchByNameContaining(name: string) {
    return this.http.get('api/course/search/' + name);
  }

  postCourse(course: Course) {
    const body = JSON.stringify(course);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<Course>('/api/course/', body, {headers});
  }

  putCourse(course: Course, id: number) {
    const body = JSON.stringify(course);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<Course>('/api/course/' + id, body, {headers});
  }

  searchUserByNameContaining(name: string) {
    return this.http.get('/api/student/search/' + name);
  }
}
