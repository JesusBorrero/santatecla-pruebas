import {Component, Input} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from '../auth/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  @Input()
  url: string;

  username = '';
  password = '';

  constructor(private router: Router, private activatedRoute: ActivatedRoute, public loginService: LoginService,) {}

  login(event: any, user: string, pass: string) {
    event.preventDefault();
    this.loginService.login(user, pass).subscribe(
      (u) => {
        if (this.url === '/') {
          if (u.roles.includes('ROLE_ADMIN')) {
            this.router.navigate(['/']);
          } else {
            this.router.navigate(['/courses']);
          }
        } else {
          this.router.navigate([this.url]);
        }
      },
      (error) => alert('Invalid user or password'),
    );
  }

}
