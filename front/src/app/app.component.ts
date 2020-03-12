import { Router, ActivatedRoute } from '@angular/router';
import { LoginService } from './auth/login.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  constructor(private router: Router, private activatedRoute: ActivatedRoute, public loginService: LoginService) {}

}
