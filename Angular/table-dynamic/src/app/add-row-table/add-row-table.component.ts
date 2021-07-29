import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-add-row-table',
  templateUrl: './add-row-table.component.html',
  styleUrls: ['./add-row-table.component.scss']
})
export class AddRowTableComponent implements OnInit {

  //READ https://blog.angularindepth.com/unleash-the-power-of-forms-with-angulars-reactive-forms-d6be5918f408

  //https://medium.com/codingthesmartway-com-blog/angular-material-part-4-data-table-23874582f23a
  data = [];
  dataSource = new BehaviorSubject<AbstractControl[]>([]);
  displayColumns = ['course', 'score'];
  rows: FormArray = this.fb.array([]);
  form: FormGroup = this.fb.group({
    name: '',
    marks: this.rows
  });

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.data.forEach(() => this.addRow());
    this.updateView();
  }

  emptyTable() {
    while (this.rows.length !== 0) {
      this.rows.removeAt(0);
    }
  }

  addRow() {
    const row = this.fb.group({
      course: '',
      score: 80
    });
    this.rows.push(row);
    this.updateView();
  }

  updateView() {
    this.dataSource.next(this.rows.controls);
  }

}
