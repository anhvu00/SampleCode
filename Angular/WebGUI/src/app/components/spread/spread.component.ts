import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-spread',
  templateUrl: './spread.component.html'
})
export class SpreadComponent {
  @Input() spreadList;

}
