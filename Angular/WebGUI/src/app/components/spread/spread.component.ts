import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-spread',
  templateUrl: './spread.component.html'
})
export class SpreadComponent implements OnInit {
  @Input() spreadList;
  // define output (to pass to parent module)
  @Output() editSpreadEvt = new EventEmitter();

  // save all changes
  handleNewSpread() {
    // local var to manipulate form fields, like a json.
    this.editSpreadEvt.emit(this.spreadList);
    console.log(this.spreadList);
  }

  // update each value until submit (handleNewSpread)
  updateList(id: number, property: string, event: any) {
    const editField = event.target.textContent;
    this.spreadList[id][property] = editField;

    console.log(editField);
  }

  constructor() {
  }

  ngOnInit() {

   }
}
