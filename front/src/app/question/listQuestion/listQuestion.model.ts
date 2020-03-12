export interface ListQuestion {
  id?: number;
  questionText: string;
  subtype: string;
  possibleAnswers: string[];
  correctAnswers: string[];
  totalCorrectAnswers ?: number;
  totalWrongAnswers ?: number;
}
