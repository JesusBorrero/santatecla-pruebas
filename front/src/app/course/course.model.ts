import {User} from '../auth/login.service';
import {Module} from '../itinerary/module/module.model';

export interface Course {
  id?: number;
  name: string;
  description: string;
  students?: User[];
  module?: Module;
  teacher?: User;
}
