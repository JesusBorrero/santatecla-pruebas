import { Image } from './image.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ImageService {

  constructor(private http: HttpClient) {}

  getImages(unitId: number) {
    return this.http.get('/api/units/' + unitId + '/images');
  }

  getImage(unitId: number, imageId: number) {
    return this.http.get('/api/units/' + unitId + '/images/' + imageId);
  }

  addImage(imageFile: any, unitId: number) {
    const formData = new FormData();
    formData.append('imageFile', imageFile);

    return this.http.post('/api/units/' + unitId + '/images', formData);
  }

  deleteImage(unitId: number, imageId: number) {
    return this.http.delete('/api/units/' + unitId + '/images/' + imageId);
  }
}
