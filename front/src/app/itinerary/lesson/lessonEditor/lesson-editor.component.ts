import { Unit } from '../../../unit/unit.model';
import { SlideService } from '../../../slide/slide.service';
import { Lesson } from '../lesson.model';
import {Component, OnInit} from '@angular/core';
import { TdDialogService } from '@covalent/core';
import { Router, ActivatedRoute } from '@angular/router';
import {MatBottomSheet} from '@angular/material/bottom-sheet';

import { LessonService } from '../lesson.service';
import { LoginService } from '../../../auth/login.service';
import { UnitService } from '../../../unit/unit.service';

import Asciidoctor from 'asciidoctor';
import {Slide} from '../../../slide/slide.model';
import {DefinitionQuestionService} from '../../../question/definitionQuestion/definitionQuestion.service';
import {UnitsCardsToolComponent} from '../lessonTools/units-cards-tool.component';
import {UnitLessonService} from '../unit-lesson.service';
import {CourseService} from '../../../course/course.service';
import {ModuleService} from '../../module/module.service';
import {Module} from '../../module/module.model';
import {ImageService} from '../../../images/image.service';
import {ImageComponent} from '../../../images/image.component';
import {CardService} from '../../../card/card.service';
import {LessonSlidesToolComponent} from '../lessonTools/lesson-slides-tool.component';
import {DefinitionQuestion} from '../../../question/definitionQuestion/definitionQuestion.model';
import {ListQuestion} from '../../../question/listQuestion/listQuestion.model';
import {TestQuestion} from '../../../question/testQuestion/testQuestion.model';
import {QuestionService} from '../../../question/question.service';
import {MatDialog} from '@angular/material/dialog';
import {AnswerQuestionDialogComponent} from '../../../question/answerQuestionDialog/answerQuestionDialog.component';
import {Question} from '../../../question/question.model';
import {UnitsQuestionsToolComponent} from '../lessonTools/units-questions-tool.component';
import {ClipboardService} from 'ngx-clipboard';
import {TabService} from '../../../tab/tab.service';
import {Tab} from '../../../tab/tab.model';
import {TestAnswer} from '../../../question/testQuestion/testAnswer.model';
import {ListAnswer} from '../../../question/listQuestion/listAnswer.model';
import {DefinitionAnswer} from '../../../question/definitionQuestion/definitionAnswer.model';


function convertToHTML(text) {
  const asciidoctor = Asciidoctor();
  const html = asciidoctor.convert(text);
  return(html);
}

enum State {
  Correct = 0,
  Wrong,
  Uncorrected,
}

@Component({
  selector: 'app-lesson-editor',
  templateUrl: './lesson-editor.component.html',
  styleUrls: ['./lesson-editor.component.css']
})
export class LessonEditorComponent implements OnInit {

  contentHTML: any[];
  contentSlide: number;
  progress: number;

  index: string;

  lessonContent: any;
  lessonContentExtended: string;

  slidesContentExtended: string[];

  extractedData: string[];
  position: number[];

  unit: Unit;
  lesson: Lesson;

  unitId: number;
  moduleId: number;
  lessonId: number;
  courseId: number;

  contentCount: number;

  showSpinner = false;
  componentsChecker: number;

  subSlide: boolean;
  subSlideCount: number;

  newQuestionsIds: number[];
  questions: Question[];
  questionsCount: number;

  // Map questionId - Question
  mapQuestions: Map<number, Question>;
  // Map Question - Boolean question done
  mapQuestionsDone: Map<Question, State>;

  cursorPosition: number;

  extraExtend = true;

  actualTab: Tab;

  constructor(private slideService: SlideService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private dialogService: TdDialogService,
              public loginService: LoginService,
              private lessonService: LessonService,
              private definitionQuestionService: DefinitionQuestionService,
              private unitService: UnitService,
              private courseService: CourseService,
              private moduleService: ModuleService,
              private bottomSheet: MatBottomSheet,
              private unitLessonService: UnitLessonService,
              private tabService: TabService,
              private imageService: ImageService,
              private cardService: CardService,
              private questionService: QuestionService,
              public dialog: MatDialog,
              private clipboardService: ClipboardService) {
    this.showSpinner = true;
  }

  ngOnInit() {
    this.contentSlide = 0;

    this.activatedRoute.params.subscribe(params => {
      this.unitId = params.unitId;
      this.lessonId = params.lessonId;

      this.lessonService.getLesson(this.lessonId).subscribe((lesson: Lesson) => {
        if (this.loginService.isAdmin) {
          this.moduleId = params.moduleId;
          this.unitService.getUnit(this.unitId).subscribe((unit: Unit) => {
              if (typeof this.moduleId === 'undefined') {
                this.tabService.addTab(new Tab('Unidad', +unit.id, unit.name, unit.id, null, null));
                this.tabService.updateActiveTabLink('Lección', this.lessonId, lesson.name, unit.id, null, null);
              } else {
                this.moduleService.getModule(this.moduleId).subscribe((module: Module) => {
                  this.tabService.addTab(new Tab('Unidad', +unit.id, unit.name, unit.id, null, null));
                  this.tabService.updateActiveTabLink('Lección', this.lessonId, lesson.name, unit.id, null, module.id);
                });
            }
          });
        } else {
          this.moduleId = params.moduleId;
          this.courseId = params.courseId;
          const tab = new Tab('Lección', this.lessonId, lesson.name, '' + this.unitId, this.courseId, this.moduleId);
          tab.setIsNotAdmin();
          this.tabService.addTab(tab);
          this.actualTab = this.tabService.activeTab;
          this.contentSlide = this.actualTab.studentLessonSlideNumber;
          this.progress = this.actualTab.studentLessonSlideProgress;
        }
        this.loadItinerary();
      });
    });

  }

  loadItinerary() {
    this.newQuestionsIds = [];
    this.subSlideCount = 0;

    this.lessonService.getLesson(this.lessonId).subscribe((data: Lesson) => {
      this.lesson = {
        id: data.id,
        name: data.name,
        slides: data.slides,
        questionsIds: data.questionsIds
      };
      this.loadQuestions();
      this.lessonContent = '== ' + this.lesson.name + '\n';
      this.lessonContentExtended = '';
      this.slidesToContent(this.lesson.slides);
      this.extendContent(this.lessonContent);
    });
  }

  loadQuestions() {
    this.mapQuestionsDone = new Map<Question, number>();
    this.mapQuestions = new Map<number, Question>();
    this.lesson.questionsIds.forEach((questionId) => {
      this.questionService.getQuestion(questionId).subscribe((data: Question) => {
        if (!this.loginService.isAdmin) {
          if (data.subtype === 'TestQuestion') {
            this.questionService.getTestUserAnswers(this.unitId, questionId, this.loginService.getCurrentUser().id,
              this.lessonId, this.courseId).subscribe((response: TestAnswer[]) => {
                if (response.length === 0) {
                  this.mapQuestionsDone.set(data, null);
                } else {
                  if (response[0].correct) {
                    this.mapQuestionsDone.set(data, State.Correct);
                  } else {
                    this.mapQuestionsDone.set(data, State.Wrong);
                  }
                }
                this.questions = Array.from(this.mapQuestionsDone.keys());
                for (const q of this.questions) {
                  this.mapQuestions.set(q.id, q);
                }
            }, error => {
              console.log(error);
            });
          } else if (data.subtype === 'ListQuestion') {
            this.questionService.getListUserAnswers(this.unitId, questionId, this.loginService.getCurrentUser().id,
              this.lessonId, this.courseId).subscribe((response: ListAnswer[]) => {
              if (response.length === 0) {
                this.mapQuestionsDone.set(data, null);
              } else {
                if (response[0].correct) {
                  this.mapQuestionsDone.set(data, State.Correct);
                } else {
                  this.mapQuestionsDone.set(data, State.Wrong);
                }
              }
              this.questions = Array.from(this.mapQuestionsDone.keys());
              for (const q of this.questions) {
                this.mapQuestions.set(q.id, q);
              }
            }, error => {
              console.log(error);
            });
          } else {
            this.questionService.getDefinitionUserAnswers(this.unitId, questionId, this.loginService.getCurrentUser().id,
              this.lessonId, this.courseId).subscribe((response: DefinitionAnswer[]) => {
              if (response.length === 0) {
                this.mapQuestionsDone.set(data, null);
              } else {
                if (response[0].corrected) {
                  if (response[0].correct) {
                    this.mapQuestionsDone.set(data, State.Correct);
                  } else {
                    this.mapQuestionsDone.set(data, State.Wrong);
                  }
                } else {
                  this.mapQuestionsDone.set(data, State.Uncorrected);
                }
              }
              this.questions = Array.from(this.mapQuestionsDone.keys());
              for (const q of this.questions) {
                this.mapQuestions.set(q.id, q);
              }
            }, error => {
              console.log(error);
            });
          }
        }
      });
    });
  }

  viewHTMLVersion() {
    this.contentHTML = [];
    if (this.subSlideCount > 50) {
      this.contentHTML.push('<h2>ERROR</h2><h3>Se ha intruducido una slide dentro de si misma (Recursividad infinita)</h3>');
    } else {
      this.slidesContentExtended = this.lessonContentExtended.split('===');
      if (!this.loginService.isAdmin) {
        this.index = '=== Índice\n\n';
        this.lesson.slides.forEach( (slide: Slide) => {
          this.index = this.index + '. ' + slide.name + '\n';
        });
        this.contentHTML.push(convertToHTML(this.index));
      }
      let counter = 0;
      this.slidesContentExtended.forEach( (slide: string) => {
        if (counter !== 0) {
          this.contentHTML.push(convertToHTML('=== ' + slide + '\n'));
        }
        counter = counter + 1;
      });
    }
  }

  slidesToContent(slides: Slide[]) {
    if (slides.length === 0) {
      this.showSpinner = false;
    } else {
      slides.forEach((slide: Slide) => {
        this.lessonContent = this.lessonContent + slide.content + '// ' + slide.id + '\n\n';
      });
    }
  }

  async getEmbeddedContent(content1: any, content2: any, unit: any, content: string, contentCounter: number, type: string) {
    let contentEmbedded;
    if (+unit) {
      if (type === 'card') {
        contentEmbedded = await this.unitService.getCard(+content1, +unit).toPromise().catch((error) => {});
        if (typeof contentEmbedded !== 'undefined') {
          this.extractedData.splice(contentCounter, 1, contentEmbedded.content);
        } else {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        }
      } else if (type === 'slide') {
        this.subSlide = true;
        contentEmbedded = await this.unitLessonService.getSlideFormLesson(+content1, +content2, +unit).toPromise().catch((error) => {});
        if (typeof contentEmbedded !== 'undefined') {
          this.extractedData.splice(contentCounter, 1, contentEmbedded.content.split('=== ')[1]);
        } else {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        }
      } else if (type === 'question') {
        contentEmbedded = await this.questionService.getUnitQuestion(unit, content1).toPromise().catch((error) => {});
        if (typeof contentEmbedded !== 'undefined') {
          this.extractedData.splice(contentCounter, 1, '*Ejercicio ' + content2 + '* ' + contentEmbedded.questionText);
          let exist = false;
          this.newQuestionsIds.forEach((question) => {
            if (question === contentEmbedded.id) {
              exist = true;
              return;
            }
          });
          if (!exist) { this.newQuestionsIds.splice(content2, 0, contentEmbedded.id); }
        } else {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        }
      } else if (type === 'image') {
        contentEmbedded = await this.imageService.getImage(unit, content1).toPromise().catch((error) => {});
        if (typeof contentEmbedded !== 'undefined') {
          const image = this.convertImage(contentEmbedded.image);
          const img = '++++\n' +
            '<img class="img-lesson" src="' + image + '">\n' +
            '++++\n' +
            '\n';
          this.extractedData.splice(contentCounter, 1, img);
        } else {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        }
      }
    } else {
      if (type === 'card') {
        contentEmbedded = await this.cardService.getCardByName(unit, content1).toPromise().catch((error) => {});
        if ((typeof contentEmbedded === 'undefined') || (contentEmbedded.length > 1)) {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        } else {
          this.extractedData.splice(contentCounter, 1, contentEmbedded[0].content);
        }
      } else if (type === 'slide') {
        this.subSlide = true;
        contentEmbedded = await this.slideService.getSlideByName(unit, content2, content1).toPromise().catch((error) => {});
        if ((typeof contentEmbedded === 'undefined') || (contentEmbedded.length > 1)) {
          this.extractedData.splice(contentCounter, 1, 'ERROR\n');
        } else {
          this.extractedData.splice(contentCounter, 1, contentEmbedded[0].content.split('=== ')[1]);
        }
      }
    }

    this.addExtractedData(content);
  }

  convertImage(bytes: any) {
    return 'data:image/png;base64,' + btoa(new Uint8Array(bytes).reduce((data, byte) =>
      data + String.fromCharCode(byte),
      ''));
  }

  contentCounterFunction(content: string) {
    this.contentCount = 0;
    this.questionsCount = 0;
    let lines: string[];
    lines = content.split('\n');
    lines.forEach((line: string) => {
      let words: string[];
      words = line.split('.');
      if (words[0] === 'insert') {
        this.contentCount = this.contentCount + 1;
        if (words[1].split('/')[0] === 'question') {
          this.questionsCount = this.questionsCount + 1;
        }
      }
    });
  }

  extendContent(content: string) {
    this.subSlide = false;
    this.contentCounterFunction(content);
    this.extractedData = [];
    for (let i = 0; i < this.contentCount; i++) {
      this.extractedData.push('');
    }
    for (let i = 0; i < this.questionsCount; i++) {
      this.newQuestionsIds.push();
    }
    this.position = [];
    let counter = 0;
    let contentCounter = 0;
    let questionCounter = 0;
    let lines: string[];
    lines = content.split('\n');
    lines.forEach((line: string) => {
      let words: string[];
      words = line.split('.');
      if (words[0] === 'insert') {
        let parameters: string[];
        parameters = words[1].split('/');
        if (parameters[0] === 'card') {
          this.position.push(counter);
          this.getEmbeddedContent(parameters[2], null, parameters[1], content, contentCounter, 'card');
          contentCounter = contentCounter + 1;
        } else if (parameters[0] === 'slide') {
          this.position.push(counter);
          this.getEmbeddedContent(parameters[3], parameters[2], parameters[1], content, contentCounter, 'slide');
          contentCounter = contentCounter + 1;
        } else if (parameters[0] === 'question') {
          this.position.push(counter);
          this.getEmbeddedContent(Number(parameters[2]), questionCounter, Number(parameters[1]), content, contentCounter, 'question');
          questionCounter = questionCounter + 1;
          contentCounter = contentCounter + 1;
        } else if (parameters[0] === 'image') {
          this.position.push(counter);
          this.getEmbeddedContent(Number(parameters[2]), null, Number(parameters[1]), content, contentCounter, 'image');
          contentCounter = contentCounter + 1;
        }
      }
      this.addExtractedData(content);
      counter = counter + 1;
    });
  }

  addExtractedData(content: string) {
    this.componentsChecker = 0;
    this.lessonContentExtended = '';
    let lines: string[];
    lines = content.split('\n');
    for (let i = 0; i < this.position.length; i ++) {
      lines[this.position[i]] = this.extractedData[i];
    }
    lines.forEach((line: string) => {
      this.lessonContentExtended = this.lessonContentExtended + line + '\n';
    });
    this.extractedData.forEach((component: string) => {
      if (component !== '') {
        this.componentsChecker = this.componentsChecker + 1;
      }
    });
    if (this.componentsChecker === this.contentCount) {
      if (this.subSlide && (this.subSlideCount <= 50)) {
        this.subSlideCount = this.subSlideCount + 1;
        this.extendContent(this.lessonContentExtended);
      } else {
        if (this.extraExtend) {
          this.extraExtend = false;
          this.extendContent(this.lessonContentExtended);
        }
        this.showSpinner = false;
        this.viewHTMLVersion();
        this.updateQuestionsBlocks(this.lesson.questionsIds, this.newQuestionsIds);
        this.contentToItinerary(this.lessonContent);
        if (this.loginService.isAdmin) {
          this.lessonService.updateLesson(this.lesson).subscribe();
        }
        if (this.progress === 0) {
          this.progress = (1 / (this.contentHTML.length)) * 100;
          this.actualTab.studentLessonSlideProgress = this.progress;
        }
      }
    }
  }

  updateQuestionsBlocks(questions, newQuestions) {
    const toAdd = newQuestions.filter(x => !questions.includes(x));
    const toDelete = questions.filter(x => !newQuestions.includes(x));

    toAdd.forEach(q => {
      this.questionService.addBlockToQuestion(this.unitId, q, this.lesson.id).subscribe(
        () => {},
        (err) => console.log(err)
      );
    });

    toDelete.forEach(q => {
      this.questionService.deleteQuestionBlock(this.unitId, q, this.lesson.id).subscribe(
        () => {},
        (err) => console.log(err)
      );
    });
  }

  contentToItinerary(content: string) {
    let slidesContent: string[];
    slidesContent = content.split('=== ');
    if (slidesContent[0].split(' ')[0] === '==') {
      this.lesson.name = '';
      for (let i = 1; i < slidesContent[0].split(' ').length; i ++) {
        this.lesson.name = this.lesson.name + slidesContent[0].split(' ')[i] + ' ';
      }
      this.lesson.name = this.lesson.name.split('\n')[0];
    }
    this.contentToSlides(slidesContent);
  }

  contentToSlides(content: string[]) {
    this.lesson.slides = [];
    let slide: Slide;
    for (let i = 1; i < content.length; i ++) {
      slide = { name: '', content: ''};
      let lines: string[];
      lines = content[i].split('\n');
      slide.name = lines[0];
      slide.content = '=== ' + slide.name + '\n';
      for (let j = 1; j < lines.length; j ++) {
        if (j < lines.length - 2) {
          if ((lines[j].split(' ')[0]) !== '//') {
            slide.content = slide.content + lines[j] + '\n';
          }
        } else {
          if ( (lines.length > 2) && ( j >= 2) ) {
            if (lines[j - 2].split(' ')[0] === '//') {
              slide.id = Number(lines[j - 2].split(' ')[1]);
            }
          }
        }
      }
      this.lesson.slides.push(slide);
    }
    this.lesson.questionsIds = this.newQuestionsIds;
  }

  updateHTMLView() {
    this.contentToItinerary(this.lessonContent);
    this.showSpinner = true;
    if (this.loginService.isAdmin) {
      this.lessonService.updateLesson(this.lesson).subscribe((_) => {
        this.loadItinerary();
      }, (error) => {
        console.error(error);
      });
    }
  }

  openCardsBottomSheet(): void {
    this.getCursorPosition();
    this.bottomSheet.open(UnitsCardsToolComponent, {data: ''}).afterDismissed().subscribe(result => {
      this.pasteFromClipboard(result);
    });
  }

  openImageBottomSheet(): void {
    this.getCursorPosition();
    this.bottomSheet.open(ImageComponent, {data: {unitId: this.unitId}}).afterDismissed().subscribe(result => {
      if (result !== undefined) {
        this.pasteFromClipboard(result);
      }
    });
  }

  openSlidesBottomSheet(): void {
    this.getCursorPosition();
    this.bottomSheet.open(LessonSlidesToolComponent, {data: ''}).afterDismissed().subscribe(result => {
      this.pasteFromClipboard(result);
    });
  }

  openQuestionsBottomSheet(): void {
    this.getCursorPosition();
    this.bottomSheet.open(UnitsQuestionsToolComponent, {data: ''}).afterDismissed().subscribe(result => {
      this.pasteFromClipboard(result);
    });
  }

  getCursorPosition() {
    const textArea = document.getElementById('text-area-editor');
    // @ts-ignore
    this.cursorPosition = textArea.selectionStart;
  }

  pasteFromClipboard(result) {
    const newLessonContent = this.lessonContent.split('');
    newLessonContent.splice(this.cursorPosition, 0, result);
    this.lessonContent = '';
    newLessonContent.forEach((letter) => {
      this.lessonContent += letter;
    });
    this.updateHTMLView();
  }

  nextSlide() {
    this.contentSlide++;
    this.actualTab.studentLessonSlideNumber ++;
    this.progress = this.progress + ((1 / (this.contentHTML.length)) * 100);
    this.actualTab.studentLessonSlideProgress += ((1 / (this.contentHTML.length)) * 100);
  }

  prevSlide() {
    this.contentSlide--;
    this.actualTab.studentLessonSlideNumber --;
    this.progress = this.progress - ((1 / (this.contentHTML.length)) * 100);
    this.actualTab.studentLessonSlideProgress -= ((1 / (this.contentHTML.length)) * 100);
  }

  openQuestion(questionID: number, subtype: string) {
    switch (subtype) {
      case 'DefinitionQuestion': {
        this.getDefinitionQuestion(questionID);
        break;
      }
      case 'ListQuestion': {
        this.getListQuestion(questionID);
        break;
      }
      case 'TestQuestion': {
        this.getTestQuestion(questionID);
        break;
      }
      default: {
        console.log('Not valid');
        break;
      }
    }
  }

  getDefinitionQuestion(questionID: number) {
    this.questionService.getUnitDefinitionQuestion(this.unitId, questionID).subscribe(
      (data: DefinitionQuestion) => {
        this.openAnswerQuestionDialog(data);
      }
    );
  }

  getListQuestion(questionID: number) {
    this.questionService.getUnitListQuestion(this.unitId, questionID).subscribe(
      (data: ListQuestion) => {
        this.openAnswerQuestionDialog(data);
      }
    );
  }

  getTestQuestion(questionID: number) {
    this.questionService.getUnitTestQuestion(this.unitId, questionID).subscribe(
      (data: TestQuestion) => {
        this.openAnswerQuestionDialog(data);
      }
    );
  }

  openAnswerQuestionDialog(answeringQuestion) {
    const dialogRef = this.dialog.open(AnswerQuestionDialogComponent, {
      width: '600px',
      data: {unitId: this.unitId, question: answeringQuestion, blockId: this.lesson.id, courseId: this.courseId}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 1) {
        this.ngOnInit();
      }
    });
  }
}
