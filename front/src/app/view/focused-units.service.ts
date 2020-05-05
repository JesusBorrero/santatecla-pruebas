import { Injectable } from '@angular/core';
import { Unit } from "../unit/unit.model";

@Injectable()
export class FocusedUnitsService {

  focusedUnitIds: Set<string> = new Set<string>();
  focusedUnits: Unit[] = [];

}
