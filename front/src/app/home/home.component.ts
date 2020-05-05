import {Component, OnInit} from '@angular/core';
import {LoginService} from '../auth/login.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  constructor(public loginService: LoginService, private router: Router) {}

  ngOnInit() {
    if (!this.loginService.isAdmin) {
      this.router.navigate(['/courses']);
    }
  }

}
