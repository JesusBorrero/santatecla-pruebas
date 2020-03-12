export interface DefinitionQuestion{
  id?: number;
  questionText: string;
  subtype: string;
  correctAnswer?: string;
  totalCorrectAnswers ?: number;
  totalWrongAnswers ?: number;
}
