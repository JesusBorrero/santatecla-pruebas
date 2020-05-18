import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {FormsModule} from '@angular/forms';
import {MatDialogModule, MatFormFieldModule, MatIconModule, MatTooltipModule} from '@angular/material';
import {HttpClientModule} from '@angular/common/http';
import {LoginService} from '../auth/login.service';
import {LoginComponent} from './login.component';
import {MenuComponent} from '../menu/menu.component';
import {AppComponent} from '../app.component';
import {By} from '@angular/platform-browser';

describe('Login component', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        FormsModule,
        MatIconModule,
        MatFormFieldModule,
        MatTooltipModule,
        MatDialogModule,
        HttpClientModule
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
    let teacherInput = fixture.debugElement.query(By.css('#username')).nativeElement;
    teacherInput.value = 'profesor';

    let passwordInput = fixture.debugElement.query(By.css('#password')).nativeElement;
    passwordInput.value = 'profesor';

    let submitButton = fixture.debugElement.query(By.css('#login-button')).nativeElement;
    submitButton.click();

    fixture.whenStable().then(() => {
      expect(component.login).toHaveBeenCalled();
    });
  });

});
