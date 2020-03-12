import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Unit } from './unit.model';
import {Relation} from '../relation/relation.model';

@Injectable()
export class UnitService {

  baseUrl = '/api/units/';

  constructor(private http: HttpClient) {}

  getUnits() {
    return this.http.get(this.baseUrl);
  }

  getUnit(id: number) {
    return this.http.get(this.baseUrl + id);
  }

  updateUnit(id: number, unit: Unit) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put<Unit>(this.baseUrl + id, unit, { headers });
  }

  searchByNameContaining(name: string) {
    const params = new HttpParams().set('name', name);
    return this.http.get(this.baseUrl + 'search/', { params: params });
  }

  createUnit(unit: Unit) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<Unit>(this.baseUrl, unit, { headers });
  }

  saveUnits(units: Unit[]) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put<Unit[]>(this.baseUrl, units, { headers });
  }

  validName(unit: Unit) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<Unit>(this.baseUrl + 'valid/', unit, { headers });
  }

  deleteUnit(id: number) {
    return this.http.delete<Unit>(this.baseUrl + id);
  }

  deleteRelation(id: number) {
    return this.http.delete<Relation>(this.baseUrl + 'relations/' + id);
  }

  getCard(cardId: number, unitId: number) {
    return this.http.get(this.baseUrl + unitId + '/cards/' + cardId);
  }

  getUnitName(id: number) {
    return this.http.get(this.baseUrl + id + '/name');
  }

}
