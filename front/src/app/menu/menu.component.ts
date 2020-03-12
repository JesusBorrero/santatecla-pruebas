import { Router, ActivatedRoute } from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {LoginService} from '../auth/login.service';
import {Unit} from '../unit/unit.model';
import {TabService} from '../tab/tab.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent {

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              public loginService: LoginService,
              private tabService: TabService) {}

  logout() {
    this.loginService.logout().subscribe(
      (response) => {
        this.router.navigate(['/']);
      },
      (error) => console.log('Error when trying to logout: ' + error),
    );
  }

}
