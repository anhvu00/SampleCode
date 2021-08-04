import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { FormField } from './form-field';
import { FormfieldControlService } from './formfield-control.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'form-dynamic';
  // fetch data
  formFields: Observable<FormField<any>[]>;
  constructor(service: FormfieldControlService) {
    this.formFields = service.getFormFields();
    console.log(this.formFields);
  }
}
