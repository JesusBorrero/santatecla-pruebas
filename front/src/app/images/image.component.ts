import {Component, Inject, OnInit, Optional} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Image} from './image.model';
import {ImageService} from './image.service';
import { ClipboardService } from 'ngx-clipboard';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})

export class ImageComponent implements OnInit {

  images: Image[];
  imagesResult: Image[];

  newImage: Image;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private imageService: ImageService,
              private bottomSheetRef: MatBottomSheetRef<ImageComponent>,
              private clipboardService: ClipboardService,
              @Optional() @Inject(MAT_BOTTOM_SHEET_DATA) public data: any) { }

  ngOnInit() {
    this.imageService.getImages().subscribe((data: Image[]) => {
      this.images = [];
      this.imagesResult = [];
      data.forEach((image: Image) => {
        this.images.push({
          id: image['id'],
          name: image['name'],
          image: (image['image']) ? this.convertImage(image['image']) : ''
        });
      });
      this.imagesResult = this.images;
    });
  }

  convertImage(bytes: any) {
      return 'data:image/png;base64,' + btoa(new Uint8Array(bytes).reduce((data, byte) =>
        data + String.fromCharCode(byte),
        ''));
  }

  saveImage(fileInput: any) {
    const imageFile: File = fileInput.files[0];
    const reader = new FileReader();
    reader.addEventListener('load', (event: any) => {
      this.imageService.addImage(imageFile).subscribe(
        _ => {
          this.ngOnInit();
        }, (error: Error) => {
          console.error('Error creating new image: ' + error);
        }
      );
    });
    reader.readAsDataURL(imageFile);
  }

  applyFilterImages(value: string) {
    this.imagesResult = [];
    for (let result of this.images) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.imagesResult.push(result);
      }
    }
  }

  getUrl(id: any) {
    const text = 'insert.image/' + id;
    this.clipboardService.copyFromContent(text);
    this.data = text;
    this.bottomSheetRef.dismiss(this.data);
    event.preventDefault();
  }

}
