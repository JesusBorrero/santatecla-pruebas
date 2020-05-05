import {ProgressInfo} from './progressInfo.model';

export interface ProgressNode {
  value: ProgressInfo;
  children: ProgressNode[];
}
