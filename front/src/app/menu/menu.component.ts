import { Router, ActivatedRoute } from '@angular/router';
import {Component} from '@angular/core';
import {LoginService} from '../auth/login.service';
import {TabService} from '../tab/tab.service';
import {Tab} from '../tab/tab.model';
import {UnitService} from "../unit/unit.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent {

  constructor(private router: Router, private activatedRoute: ActivatedRoute, public loginService: LoginService, private tabService: TabService, public unitService: UnitService) {}

  logout() {
    this.loginService.logout().subscribe(
      (response) => {
        this.router.navigate(['/']);
      },
      (error) => console.log('Error when trying to logout: ' + error),
    );
    this.tabService.emptyTabs();
  }

  closeTab(tab: Tab) {
    if (tab.isActive) {
      this.router.navigate([tab.closeLink]);
    }
    this.tabService.removeTab(tab);
  }

}
