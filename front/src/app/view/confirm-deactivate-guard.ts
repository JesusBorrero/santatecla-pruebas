import { CanDeactivate } from '@angular/router';
import {ViewComponent} from "./view.component";

export class ConfirmDeactivateGuard implements CanDeactivate<ViewComponent> {

  canDeactivate(viewComponent: ViewComponent) {
    return viewComponent.canDeactivate();
  }

}
