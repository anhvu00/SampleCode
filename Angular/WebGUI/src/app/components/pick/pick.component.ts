import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-pick',
  templateUrl: './pick.component.html'
})
export class PickComponent {
  // TODO: change to new data.json
  @Input() spreadList;

}
