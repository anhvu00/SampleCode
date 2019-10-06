import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spread',
  templateUrl: './spread.component.html'
})
export class SpreadComponent {
  @Input() spreadList;

  // handle add
  handleNewSpread() {
    // local var to manipulate form fields, like a json.
    console.log(this.spreadList);
  }
  updateList(id: number, property: string, event: any) {
    const editField = event.target.textContent;
    this.spreadList[id][property] = editField;
    console.log(editField);
  }

}
