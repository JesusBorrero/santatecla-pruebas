import {Block} from '../block.model';

export interface Module extends Block {
  id?: number;
  blocks?: Block[];
}
