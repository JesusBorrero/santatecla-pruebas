import { Card } from './../card/card.model';
import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Slide } from './slide.model';
import { Observable } from 'rxjs';

@Injectable()
export class SlideService {

  constructor(private http: HttpClient) {}

  getSlides() {
    return this.http.get('/api/slides/');
  }

  getSlide(id: number) {
    return this.http.get('/api/slides/' + id);
  }

  getSlideByName(unitName: string, lessonName: string, slideName: string) {
    const params = new HttpParams().set('unitName', unitName).set('lessonName', lessonName).set('slideName', slideName);
    return this.http.get('/api/slides/search', { params: params });
  }

  deleteSlide(slide: Slide): Observable<Slide> {
    return this.http.delete<Slide>('/api/slides/' + slide.id);
  }

}
