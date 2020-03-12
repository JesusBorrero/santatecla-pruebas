export interface TestQuestion{
  id?: number;
  questionText: string;
  subtype: string;
  possibleAnswers: string[];
  correctAnswer: string;
  totalCorrectAnswers?: number;
  totalWrongAnswers ?: number;
}
