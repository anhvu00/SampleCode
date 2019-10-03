import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-winloss',
  templateUrl: './winloss.component.html'
})
export class WinlossComponent {
  @Input() winlossList;

}
