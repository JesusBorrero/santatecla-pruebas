import { Unit } from '../unit/unit.model';

export interface Relation {
  id?: string;
  relationType: string;
  incoming: string;
  outgoing: string;
}
