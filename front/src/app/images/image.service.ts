import { Image } from './image.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ImageService {

  constructor(private http: HttpClient) {}

  getImages() {
    return this.http.get('/api/images/');
  }

  getImage(id: number) {
    return this.http.get('/api/images/' + id);
  }

  addImage(imageFile: any) {
    const formData = new FormData();
    formData.append('imageFile', imageFile);

    return this.http.post('/api/images/', formData);
  }
}
