
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'tab-dynamic';
  tabs = ['Tab 1', 'Tab 2', 'Tab 3'];
  selected = new FormControl(0);
  tabtitle: string = '';

  addTab(selectAfterAdding: boolean) {

    if (this.tabtitle != '') {
      this.tabs.push(this.tabtitle);
    } else {
      this.tabs.push('New');
    }

    this.tabtitle = '';

    if (selectAfterAdding) {
      this.selected.setValue(this.tabs.length - 1);
    }
  }

  removeTab(index: number) {
    this.tabs.splice(index, 1);
  }

}