import { MatAutocompleteModule, MatBadgeModule, MatBottomSheetModule, MatButtonModule, MatButtonToggleModule, MatCardModule,
  MatCheckboxModule, MatChipsModule, MatDatepickerModule, MatDialogModule, MatDividerModule, MatExpansionModule, MatGridListModule,
  MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatNativeDateModule, MatPaginatorModule, MatProgressBarModule,
  MatProgressSpinnerModule, MatRadioModule, MatRippleModule, MatSelectModule, MatSidenavModule, MatSliderModule, MatSlideToggleModule,
  MatSnackBarModule, MatSortModule, MatStepperModule, MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule, MatTreeModule,
} from '@angular/material';

import { CovalentCommonModule, CovalentLayoutModule, CovalentMediaModule, CovalentExpansionPanelModule,
  CovalentStepsModule, CovalentLoadingModule, CovalentDialogsModule, CovalentSearchModule, CovalentPagingModule,
  CovalentNotificationsModule, CovalentMenuModule, CovalentDataTableModule, CovalentMessageModule } from '@covalent/core';

import {FormsModule} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { routing } from './app.routing';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BasicAuthInterceptor } from './auth/auth.interceptor';
import { ErrorInterceptor } from './auth/error.interceptor';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';

import { ClipboardModule } from 'ngx-clipboard';

import { AppComponent } from './app.component';
import { LoginService } from './auth/login.service';
import { LessonService } from './itinerary/lesson/lesson.service';
import { ViewComponent } from './view/view.component';
import { CardComponent } from './card/card.component';
import { LessonEditorComponent } from './itinerary/lesson/lessonEditor/lesson-editor.component';
import { CardService } from './card/card.service';
import { SlideService } from './slide/slide.service';
import { QuestionComponent } from './question/question.component';
import { UnitService } from './unit/unit.service';
import { UnitComponent } from './unit/unit.component';
import { DefinitionQuestionService } from './question/definitionQuestion/definitionQuestion.service';
import { ListQuestionService } from './question/listQuestion/listQuestion.service';
import { ModuleProgressComponent } from './progress/module-progress/module-progress.component';
import { ClassProgressComponent } from './progress/class-progress/class-progress.component';
import { ProgressService } from './progress/progress.service';
import {QuestionService} from './question/question.service';
import {TestQuestionService} from './question/testQuestion/testQuestion.service';
import {MyCoursesComponent} from './course/myCourses.component';
import {CourseComponent} from './course/course.component';
import {CourseService} from './course/course.service';
import {TabService} from './tab/tab.service';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {NewCourseComponent} from './course/newCourse.component';
import {LoginComponent} from './login/login.component';
import {MenuComponent} from './menu/menu.component';
import {UnitsCardsToolComponent} from './itinerary/lesson/lessonTools/units-cards-tool.component';
import {LessonFormComponent} from './itinerary/lesson/lessonForm/lesson-form.component';
import {AnswerQuestionDialogComponent} from './question/answerQuestionDialog/answerQuestionDialog.component';
import {HomeComponent} from './home/home.component';
import {LessonComponent} from './itinerary/lesson/lesson.component';
import {ConfirmActionComponent} from './confirmAction/confirm-action.component';
import {AddQuestionDialogComponent} from './question/addQuestionDialog/addQuestionDialog.component';
import {ModuleComponent} from './itinerary/module/module.component';
import {ModuleEditorComponent} from './itinerary/module/moduleEditor/module-editor.component';
import {ModuleService} from './itinerary/module/module.service';
import {UnitLessonService} from './itinerary/lesson/unit-lesson.service';
import {UnitModuleService} from './itinerary/module/unit-module.service';
import {ModuleFormComponent} from './itinerary/module/moduleForm/module-form.component';
import {UnitsBlocksToolComponent} from './itinerary/module/moduleEditor/units-blocks-tool.component';
import {ModuleRenameComponent} from './itinerary/module/moduleRename/module-rename.component';
import {ImageComponent} from './images/image.component';
import {ImageService} from './images/image.service';
import {LessonSlidesToolComponent} from './itinerary/lesson/lessonTools/lesson-slides-tool.component';
import {QuestionTrackingComponent} from './question/questionTracking/questionTracking.component';
import {UnitsQuestionsToolComponent} from './itinerary/lesson/lessonTools/units-questions-tool.component';
import {DragDropModule} from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    AppComponent,
    LessonFormComponent,
    LessonComponent,
    LessonSlidesToolComponent,
    ModuleComponent,
    ModuleFormComponent,
    LoginComponent,
    MenuComponent,
    ViewComponent,
    UnitComponent,
    CardComponent,
    ImageComponent,
    LessonEditorComponent,
    ModuleEditorComponent,
    UnitsCardsToolComponent,
    UnitsBlocksToolComponent,
    UnitsQuestionsToolComponent,
    QuestionComponent,
    QuestionTrackingComponent,
    ModuleProgressComponent,
    ClassProgressComponent,
    MyCoursesComponent,
    CourseComponent,
    NewCourseComponent,
    AnswerQuestionDialogComponent,
    AddQuestionDialogComponent,
    HomeComponent,
    ConfirmActionComponent,
    ModuleRenameComponent
  ],
  entryComponents: [
    ConfirmActionComponent,
    ModuleRenameComponent
  ],
    imports: [
        ClipboardModule,
        BrowserAnimationsModule,
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatAutocompleteModule,
        MatBadgeModule,
        MatBottomSheetModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatCardModule,
        MatCheckboxModule,
        MatChipsModule,
        MatDatepickerModule,
        MatDialogModule,
        MatDividerModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatRippleModule,
        MatSelectModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatStepperModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
        MatTreeModule,
        FormsModule,
        routing,
        CovalentCommonModule, CovalentLayoutModule, CovalentMediaModule, CovalentExpansionPanelModule,
        CovalentStepsModule, CovalentLoadingModule, CovalentDialogsModule, CovalentSearchModule, CovalentPagingModule,
        CovalentNotificationsModule, CovalentMenuModule, CovalentDataTableModule, CovalentMessageModule, NgxChartsModule, DragDropModule
    ],
  providers: [LoginService, LessonService, ModuleService, CardService, SlideService, DefinitionQuestionService, ListQuestionService,
    QuestionService, ProgressService, TestQuestionService, CourseService, TabService, UnitService,
    MenuComponent, UnitLessonService, UnitModuleService, ImageService,
    { provide: HTTP_INTERCEPTORS, useClass: BasicAuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})

export class AppModule { }
