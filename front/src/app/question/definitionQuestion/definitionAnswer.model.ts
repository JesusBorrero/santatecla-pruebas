import { User } from '../../auth/login.service';

export interface DefinitionAnswer {
    id?: number;
    answerText: string;
    correct?: boolean;
    corrected?: boolean;
    justification?: string;

    unitId?: number;
    blockId?: number;
    courseId?: number;

    user?: User;
}
