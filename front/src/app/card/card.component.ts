import {Unit } from '../unit/unit.model';
import {CardService} from './card.service';
import {Router, ActivatedRoute} from '@angular/router';
import {AfterViewChecked, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Card} from './card.model';
import {UnitService} from '../unit/unit.service';
import {MatDialog} from '@angular/material/dialog';
import {ConfirmActionComponent} from '../confirmAction/confirm-action.component';
import Asciidoctor from 'asciidoctor';
import {ImageComponent} from '../images/image.component';
import {MatBottomSheet} from '@angular/material/bottom-sheet';
import {ImageService} from '../images/image.service';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {MatAccordion} from '@angular/material/expansion';

function convertToHTML(text) {
  const asciidoctor = Asciidoctor();
  const html = asciidoctor.convert(text);
  return(html);
}

@Component({
  selector: 'app-cards',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})

export class CardComponent implements OnInit {

  @ViewChild('cardList') cardList: ElementRef;

  unitId: number;
  cards: Card[] = [];
  unit: Unit;
  showSpinner = false;

  cardsView: boolean[];
  prettyCards: any[];

  confirmText = 'Se eliminará la ficha permanentemente';
  button1 = 'Cancelar';
  button2 = 'Borrar';

  cardsCounter: number;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private cardService: CardService,
              private unitService: UnitService,
              private dialog: MatDialog,
              private bottomSheet: MatBottomSheet,
              private imageService: ImageService) { }

  ngOnInit() {
    this.cardsCounter = 0;
    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
      this.showSpinner = true;
      this.cardsView = [];
      this.prettyCards = [];
      this.unitService.getUnit(this.unitId).subscribe((data: Unit) => {
        this.unit = data;
        this.cards = this.unit.cards;
        this.cards.forEach((card, index) => {
          this.cardsView.push(true);
          this.prettyCards.push('');
          this.cardContentProcessor(card.content, index);
        });
      });
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousIndex !== event.currentIndex) {
      let choosen;
      const prevPretty = this.prettyCards[event.previousIndex];
      if (!this.cardsView[event.previousIndex]) {
        choosen = false;
      } else {
        choosen = true;
      }
      if (event.previousIndex > event.currentIndex) {
        if ((event.previousIndex - event.currentIndex) > 1) {
          for (let i = event.previousIndex; i > event.currentIndex; i--) {
            this.cardsView[i] = this.cardsView[i - 1];
            this.prettyCards[i] = this.prettyCards[i - 1];
          }
        } else {
          this.cardsView[event.previousIndex] = this.cardsView[event.currentIndex];
          this.prettyCards[event.previousIndex] = this.prettyCards[event.currentIndex];
        }
      } else {
        if ((event.currentIndex - event.previousIndex) > 1) {
          for (let i = event.previousIndex; i < event.currentIndex; i++) {
            this.cardsView[i] = this.cardsView[i + 1];
            this.prettyCards[i] = this.prettyCards[i + 1];
          }
        } else {
          this.cardsView[event.previousIndex] = this.cardsView[event.currentIndex];
          this.prettyCards[event.previousIndex] = this.prettyCards[event.currentIndex];
        }
      }
      if (!choosen) {
        this.cardsView[event.currentIndex] = false;
      } else {
        this.cardsView[event.currentIndex] = true;
      }
      this.prettyCards[event.currentIndex] = prevPretty;
    }
    moveItemInArray(this.cards, event.previousIndex, event.currentIndex);
    this.unit.cards = this.cards;
    this.unitService.updateUnit(+this.unit.id, this.unit).subscribe();
  }

  cardContentProcessor(content: string, index: number) {
    const contentCount = this.contentCounterFunction(content);
    const extractedData = [];
    const position = [];
    let counter = 0;
    let contentCounter = 0;
    const lineas = content.split('\n');
    lineas.forEach((line) => {
      const words = line.split('.');
      if (words[0] === 'insert') {
        const parameters = words[1].split('/');
        if (parameters[0] === 'image') {
          position.push(counter);
          this.getEmbebedContent(Number(parameters[1]), content, contentCounter, 'image', extractedData, position, index, contentCount);
          contentCounter = contentCounter + 1;
        }
      }
      this.addExtractedData(content, extractedData, position, index, contentCount);
      counter = counter + 1;
    });
  }

  addExtractedData(content: string, extractedData: any, position: any, index: number, contentCount: number) {
    let componentsChecker = 0;
    let cardContentExtended = '';
    const lines = content.split('\n');
    for (let i = 0; i < position.length; i ++) {
      lines[position[i]] = extractedData[i];
    }
    lines.forEach((line: string) => {
      cardContentExtended = cardContentExtended + line + '\n';
    });
    extractedData.forEach((component: string) => {
      if (component !== '') {
        componentsChecker = componentsChecker + 1;
      }
    });
    if (componentsChecker === contentCount) {
      this.prettyCards[index] = convertToHTML(cardContentExtended);
      this.cardsCounter = this.cardsCounter + 1;
      if (this.cardsCounter === this.cards.length) {
        this.showSpinner = false;
      }
    }
  }

  async getEmbebedContent(content1: any, content: string, contentCounter: number, type: string, extractedData: any, position: any, index: number, contentCount) {
    let contentEmbebed;

    if (type === 'image') {
      contentEmbebed = await this.imageService.getImage(content1).toPromise();
      if (typeof contentEmbebed !== 'undefined') {
        const image = this.convertImage(contentEmbebed.image);
        const img = '++++\n' +
          '<img class="img-lesson" src="' + image + '">\n' +
          '++++\n' +
          '\n';
        extractedData.splice(contentCounter, 1, img);
      } else {
        extractedData.splice(contentCounter, 1, 'ERROR\n');
      }
    }
    this.addExtractedData(content, extractedData, position, index, contentCount);
  }

  contentCounterFunction(content: string) {
    let contentCount = 0;
    let lines: string[];
    lines = content.split('\n');
    lines.forEach((line: string) => {
      let words: string[];
      words = line.split('.');
      if (words[0] === 'insert') {
        contentCount = contentCount + 1;
      }
    });
    return contentCount;
  }

  convertImage(bytes: any) {
    return 'data:image/png;base64,' + btoa(new Uint8Array(bytes).reduce((data, byte) =>
      data + String.fromCharCode(byte),
      ''));
  }

  editCard(index: number) {
    this.cardsView[index] = false;
  }

  viewCard(index: number) {
    this.cardsView[index] = true;
  }

  addCard() {
    if ((this.cards.length === 0) || (this.cards[0].id !== 0) || (this.cards[0].name && this.cards[0].content)) {
      this.cards.unshift({
        id: 0,
        name: '',
        content: ''
      });
      this.prettyCards.unshift(convertToHTML(''));
      this.cardsView.unshift(false);
    }
    this.focusNewCard();
  }

  focusNewCard() {
    window.scrollTo({ left: 0, top: 0, behavior: 'smooth' });
  }

  getCardIndex(id: number): number {
    let index = -1;
    this.cards.forEach((card, i) => {
      if (card.id === id) {
        index = i;
      }
    });
    return index;
  }

  deleteCard(id: number, index: number) {
    const dialogRef = this.dialog.open(ConfirmActionComponent, {
      data: {confirmText: this.confirmText, button1: this.button1, button2: this.button2}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 1) {
        this.showSpinner = true;
        if (id === 0) {
          this.cards.splice(0, 1);
          this.prettyCards.splice(0, 1);
          this.cardsView.splice(0, 1);
          this.showSpinner = false;
        } else {
          this.cardService.deleteCard(this.unitId, id).subscribe(() => {
            this.cards.splice(this.getCardIndex(id), 1);
            this.prettyCards.splice(index, 1);
            this.cardsView.splice(index, 1);
            this.showSpinner = false;
          });
        }
      }
    });
  }

  save(card: Card, index: number) {
    if (card.name && card.content) {
      this.showSpinner = true;
      if (card.id === 0) {
        this.cardService.create(this.unitId, card).subscribe((createdCard) => {
          card.id = createdCard.id;
          this.cardContentProcessor(card.content, index);
          this.showSpinner = false;
        });
      } else {
        this.cardService.save(this.unitId, card).subscribe(() => {
          this.cardContentProcessor(card.content, index);
          this.showSpinner = false;
        }, error => {
          console.error(error);
        });
      }
    }
  }

  changeTextArea(event: Event) {
    this.fitContent(event.target as HTMLTextAreaElement);
  }

  fitContent(textArea: HTMLTextAreaElement) {
    textArea.style.overflow = 'hidden';
    textArea.style.height = '0px';
    textArea.style.height = textArea.scrollHeight + 'px';
  }

  openImageBottomSheet(): void {
    this.bottomSheet.open(ImageComponent);
  }

}
