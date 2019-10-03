import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html'
})

export class AddComponent implements OnInit {
  // local var to show/hide the form
  showForm: boolean;

  // define output (to pass to parent module)
  @Output() addEvt = new EventEmitter();

  // handle add
  handleAdd(formInfo: any) {
    // local var to manipulate form fields, like a json.
    const tempItem: object = {
      petName : formInfo.petName,
      ownerName : formInfo.ownerName,
      aptDate : formInfo.aptDate + ' ' + formInfo.aptTime,
      aptNotes : formInfo.aptNotes
    };
    // now we need to pass the above to parent module
    this.addEvt.emit(tempItem);
    // show/hide form
    this.toggleAptDisplay();
  }

  toggleAptDisplay() { 
    this.showForm = ! this.showForm;
  }
  constructor() {
    this.showForm = true;
  }

  ngOnInit() { }
}
