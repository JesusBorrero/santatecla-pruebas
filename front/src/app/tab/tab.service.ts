import { Injectable } from '@angular/core';
import {LoginService} from '../auth/login.service';

@Injectable()
export class TabService {

  units = false;
  unit = '';
  unitId: number;

  courses = false;
  course = '';
  courseId: number;

  lessons = false;
  lesson = '';

  modules = false;
  module = '';
  moduleId: number;

  questions = false;
  questionId: number;
  question = '';

  constructor(public loginService: LoginService) {}

  emptyAll() {
    this.units = false;
    this.unit = '';
    this.courses = false;
    this.course = '';
    this.lessons = false;
    this.lesson = '';
    this.modules = false;
    this.module = '';
    this.questions = false;
    this.question = '';
  }

  setHome() {
    this.emptyAll();
  }

  setUnits() {
    this.emptyAll();
    this.units = true;
  }

  setUnit(unitName: string, unitId) {
    this.emptyAll();
    this.units = true;
    this.unit = unitName;
    this.unitId = unitId;
  }

  setCourses() {
    this.emptyAll();
    this.courses = true;
  }

  setCourse(courseName: string, courseId: number) {
    this.emptyAll();
    this.courses = true;
    this.course = courseName;
    this.courseId = courseId;
  }

  setLesson(unitName: string, unitId: number, lessonName: string) {
    this.emptyAll();
    this.units = true;
    this.unit = unitName;
    this.unitId = unitId;
    this.lessons = true;
    this.lesson = lessonName;
  }

  setLessonInModule(name: string, id: number, moduleName: string, moduleId: number, lessonName: string) {
    this.emptyAll();
    if (this.loginService.isAdmin) {
      this.units = true;
      this.unit = name;
      this.unitId = id;
    } else {
      this.courses = true;
      this.course = name;
      this.courseId = id;
    }
    this.module = moduleName;
    this.moduleId = moduleId;
    this.lessons = true;
    this.lesson = lessonName;
  }

  setUnitModule(name: string, unitId: number, moduleName: string, moduleId: number) {
    this.emptyAll();
    this.units = true;
    this.unit = name;
    this.unitId = unitId;
    this.modules = true;
    this.module = moduleName;
    this.moduleId = moduleId;
  }

  setCourseModule(moduleId: number, courseId: number, name: string) {
    this.emptyAll();
    this.moduleId = moduleId;
    this.courseId = courseId;
    this.courses = true;
    this.course = name;
  }

  setQuestion(questionId: number, question: string, unit: string, unitId: number) {
    this.emptyAll();
    this.units = true;
    this.unit = unit;
    this.unitId = unitId;
    this.questions = true;
    this.questionId = questionId;
    this.question = question;
  }

}
