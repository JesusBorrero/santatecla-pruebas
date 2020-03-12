import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Module} from './module.model';

@Injectable()
export class UnitModuleService {

  baseUrl = '/api/units/';

  constructor(private http: HttpClient) {}

  addModule(unitId: number, module: Module) {
    const body = JSON.stringify(module);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<Module>(this.baseUrl + unitId + '/modules', body, { headers });
  }

  deleteModule(unitId: number, moduleId: number) {
    return this.http.delete<Module>(this.baseUrl + unitId + '/modules/' + moduleId);
  }

}
