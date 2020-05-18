import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule} from '@angular/forms';
import {MatDialogModule, MatFormFieldModule, MatIconModule, MatInputModule, MatTooltipModule} from '@angular/material';
import {HttpClientModule} from '@angular/common/http';
import {LoginService} from '../auth/login.service';
import {LoginComponent} from './login.component';
import {MenuComponent} from '../menu/menu.component';
import {AppComponent} from '../app.component';
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {Observable} from 'rxjs';

describe('Login component', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    class MockLoginService extends LoginService {
      error = false;

      login(user: string, pass: string) {
        if (user === 'profesor' && pass === 'password') {
          this.error = true;
        }

        return Observable.create(observer => {
          if (this.error) {
            observer.error(new Error());
          } else {
            observer.next();
          }
          observer.complete();
        });
      }
    }

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        FormsModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatTooltipModule,
        MatDialogModule,
        HttpClientModule,
        BrowserAnimationsModule
      ],
      providers: [LoginService],
      declarations: [
        LoginComponent,
        MenuComponent,
        AppComponent
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should login successfully', () => {
    spyOn(component, 'login');

    fixture.debugElement.query(By.css('#username')).nativeElement.value = 'profesor';
    fixture.debugElement.query(By.css('#password')).nativeElement.value = 'profesor';
    fixture.debugElement.query(By.css('#send-login-button')).triggerEventHandler('click', null);

    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.login).toHaveBeenCalled();
    });
  });

  it('should not login', () => {
    spyOn(window, 'alert');

    fixture.debugElement.query(By.css('#username')).nativeElement.value = 'profesor';
    fixture.debugElement.query(By.css('#password')).nativeElement.value = 'password';
    fixture.debugElement.query(By.css('#send-login-button')).triggerEventHandler('click', null);

    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.login).toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Invalid user or password');
    });
  });

});
