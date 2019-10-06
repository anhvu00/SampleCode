import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-pick',
  templateUrl: './pick.component.html'
})
export class PickComponent {
  // TODO: change to new data.json
  @Input() spreadList;

  pickVisitor(item: any) {
    console.log("visitor pick " + item);
  }

  pickHome(item: any) {
    console.log("home pick " + item);
  }

}
