export interface Question {
  id?: number;
  questionText: string;
  subtype: string;

  possibleAnswers?: string[];
  correctAnswers?: string[];
  correctAnswer?: string;

  lessonIds?: number[];
}
