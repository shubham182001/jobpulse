import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgxSpinnerModule } from 'ngx-spinner';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgxSpinnerModule],
  template: `
    <router-outlet></router-outlet>
    <ngx-spinner bdColor="rgba(10,14,39,0.8)" size="medium" color="#8b5cf6" type="ball-clip-rotate">
      <p style="color: #a78bfa;">Loading...</p>
    </ngx-spinner>
  `,
  styles: []
})
export class App {}