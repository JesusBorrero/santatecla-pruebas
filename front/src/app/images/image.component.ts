import {Component, ElementRef, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Image} from './image.model';
import {ImageService} from './image.service';
import { ClipboardService } from 'ngx-clipboard';
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {MatDialog, MatSnackBar} from '@angular/material';
import {ConfirmActionComponent} from '../confirmAction/confirm-action.component';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})

export class ImageComponent implements OnInit {

  @ViewChild('input') input: ElementRef;
  showSpinner = false;

  images: Image[];
  imagesResult: Image[];

  newImage: Image;

  unitId: number;

  confirmText = 'Se eliminará la imagen permanentemente';
  button1 = 'Cancelar';
  button2 = 'Borrar';

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private imageService: ImageService,
              private bottomSheetRef: MatBottomSheetRef<ImageComponent>,
              private clipboardService: ClipboardService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog,
              @Optional() @Inject(MAT_BOTTOM_SHEET_DATA) public data: any) { }

  ngOnInit() {
    this.showSpinner = true;
    this.unitId = this.data.unitId;
    this.imageService.getImages(this.unitId).subscribe((data: Image[]) => {
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
      this.showSpinner = false;
      this.input.nativeElement.focus();
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
      this.imageService.addImage(imageFile, this.unitId).subscribe(
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
    this.showSpinner = true;
    this.imagesResult = [];
    for (let result of this.images) {
      if (result.name.toLowerCase().includes(value.toLowerCase())) {
        this.imagesResult.push(result);
      }
    }
    this.showSpinner = false;
  }

  getUrl(id: any) {
    const text = 'insert.image/' + this.unitId + '/' + id;
    this.clipboardService.copyFromContent(text);
    this.data = text;
    this.bottomSheetRef.dismiss(this.data);
    this.snackBar.open('El insert ha sido copiada al portapapeles', 'Entendido', {
      duration: 3000,
    });
    event.preventDefault();
  }

  deleteImage(imageId: number) {
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmText, button1: this.button1, button2: this.button2}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 1) {
        this.showSpinner = true;
        this.imageService.deleteImage(this.unitId, imageId).subscribe((data: any) => {
          this.ngOnInit();
        }, error => {
          console.log(error);
        });
      }
    });
  }

}
