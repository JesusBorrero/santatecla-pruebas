import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Unit } from './unit.model';
import {Relation} from '../relation/relation.model';

@Injectable()
export class UnitService {

  UNIT_NAME_SEPARATOR = '/';
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

  getUnambiguousName(id: number) {
    return this.http.get(this.baseUrl + id + '/unambiguousName');
  }

  getUnitPrefixName(completeName: string) {
    const nameLength = completeName.split(this.UNIT_NAME_SEPARATOR)[completeName.split(this.UNIT_NAME_SEPARATOR).length - 1].length;
    return completeName.substring(0, completeName.length - nameLength);
  }

  getUnitRealName(completeName: string) {
    return completeName.split(this.UNIT_NAME_SEPARATOR)[completeName.split(this.UNIT_NAME_SEPARATOR).length - 1];
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

  getCard(cardId: number, unitId: number) {
    return this.http.get(this.baseUrl + unitId + '/cards/' + cardId);
  }

  getUnitName(id: number) {
    return this.http.get(this.baseUrl + id + '/name');
  }

  getModuleUnit(moduleId: number) {
    return this.http.get(this.baseUrl + 'module/' + moduleId);
  }

}
