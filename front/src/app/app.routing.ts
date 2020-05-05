import { LessonEditorComponent } from './itinerary/lesson/lessonEditor/lesson-editor.component';
import { CardComponent } from './card/card.component';
import { RouterModule } from '@angular/router';
import { ViewComponent } from './view/view.component';
import { QuestionComponent } from './question/question.component';
import { ModuleProgressComponent } from './progress/module-progress/module-progress.component';
import {MyCoursesComponent} from './course/myCourses.component';
import {NewCourseComponent} from './course/newCourse.component';
import {UnitsCardsToolComponent} from './itinerary/lesson/lessonTools/units-cards-tool.component';
import {LessonFormComponent} from './itinerary/lesson/lessonForm/lesson-form.component';
import {AnswerQuestionDialogComponent} from './question/answerQuestionDialog/answerQuestionDialog.component';
import {AddQuestionDialogComponent} from './question/addQuestionDialog/addQuestionDialog.component';
import {UnitComponent} from './unit/unit.component';
import {HomeComponent} from './home/home.component';
import {LessonComponent} from './itinerary/lesson/lesson.component';
import {ModuleComponent} from './itinerary/module/module.component';
import {ModuleEditorComponent} from './itinerary/module/moduleEditor/module-editor.component';
import {CourseComponent} from './course/course.component';
import {ModuleFormComponent} from './itinerary/module/moduleForm/module-form.component';
import {UnitsBlocksToolComponent} from './itinerary/module/moduleEditor/units-blocks-tool.component';
import {ImageComponent} from './images/image.component';
import {LessonSlidesToolComponent} from './itinerary/lesson/lessonTools/lesson-slides-tool.component';
import {QuestionTrackingComponent} from './question/questionTracking/questionTracking.component';
import {UnitsQuestionsToolComponent} from './itinerary/lesson/lessonTools/units-questions-tool.component';
import {ConfirmDeactivateGuard} from "./view/confirm-deactivate-guard";


const appRoutes = [
  { path: '' , component: HomeComponent },
  { path: 'unit' , component: ViewComponent, canDeactivate: [ConfirmDeactivateGuard] },
  { path: 'unit/:unitId', component: UnitComponent },
  { path: 'unitsCardsTool', component: UnitsCardsToolComponent },
  { path: 'lessonSlidesTool', component: LessonSlidesToolComponent },
  { path: 'unitsBlocksTool', component: UnitsBlocksToolComponent },
  { path: 'unitsQuestionsTool', component: UnitsQuestionsToolComponent },
  { path: 'lessonForm', component: LessonFormComponent },
  { path: 'moduleForm', component: ModuleFormComponent },
  { path: 'units/:unitId/cards', component: CardComponent },
  { path: 'units/:unitId/lessons', component: LessonComponent },
  { path: 'units/:unitId/modules', component: ModuleComponent },
  { path: 'units/:unitId/lessons/:lessonId', component: LessonEditorComponent },
  { path: 'units/:unitId/modules/:moduleId/lessons/:lessonId', component: LessonEditorComponent },
  { path: 'course/:courseId/units/:unitId/modules/:moduleId/lessons/:lessonId', component: LessonEditorComponent },
  { path: 'units/:unitId/modules/:moduleId', component: ModuleEditorComponent },
  { path: 'course/:courseId/modules/:moduleId', component: ModuleEditorComponent },
  { path: 'units/:unitId/question', component: QuestionComponent },
  { path: 'unit/:unitId/question/:questionType/:questionId', component: QuestionTrackingComponent },
  { path: 'answerQuestionDialog', component: AnswerQuestionDialogComponent},
  { path: 'addQuestionDialog', component: AddQuestionDialogComponent},
  { path: 'question', component: QuestionComponent},
  { path: 'courses', component: MyCoursesComponent },
  { path: 'progress', component: ModuleProgressComponent },
  { path: 'newCourse', component: NewCourseComponent},
  { path: 'course/:courseId', component: CourseComponent},
  { path: 'images', component: ImageComponent}

];

export const routing = RouterModule.forRoot(appRoutes);
