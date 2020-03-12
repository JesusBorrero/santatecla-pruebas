import { Slide } from '../../slide/slide.model';
import {Block} from '../block.model';

export interface Lesson extends Block {
  id?: number;
  slides?: Slide[];
  questionsIds?: number[];
}
