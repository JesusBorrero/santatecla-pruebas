import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Block} from '../block.model';
import {Module} from './module.model';

@Injectable()
export class ModuleService {
  constructor(private http: HttpClient) { }

  getModules() {
    return this.http.get('/api/modules/');
  }

  getModule(id: number) {
    return this.http.get('/api/modules/' + id);
  }

  updateModule(module: Module): Observable<Module> {
    const body = JSON.stringify(module);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<Module>('/api/modules/' + module.id , body, {headers});
  }

  addBlock(moduleId: number, block: Block) {
    const body = JSON.stringify(block);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<Block>('/api/modules/' + moduleId, body, { headers });
  }

  deleteBlock(moduleId: number, blockId: number) {
    return this.http.delete('/api/modules/' + moduleId + '/blocks/' + blockId);
  }

}
