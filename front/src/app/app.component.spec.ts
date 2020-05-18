import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import {LoginComponent} from './login/login.component';
import {MenuComponent} from './menu/menu.component';
import {FormsModule} from '@angular/forms';
import { MatDialogModule, MatFormFieldModule, MatIconModule, MatTooltipModule} from '@angular/material';
import {LoginService} from './auth/login.service';
import {HttpClientModule} from '@angular/common/http';

describe('AppComponent', () => {
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

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });
});
