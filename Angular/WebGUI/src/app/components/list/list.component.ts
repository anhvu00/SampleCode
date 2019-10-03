// Input = how bring data into this component from another component
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html'
})
export class ListComponent {
  // receive the data via directive (injection?)
  // Note it's a Function.
  @Input() aptList;
  @Output() deleteEvt = new EventEmitter();

  // event handler
  handleDelete(theApt: object) {
    // pass theApt to parent component
    this.deleteEvt.emit(theApt);
  }
}
