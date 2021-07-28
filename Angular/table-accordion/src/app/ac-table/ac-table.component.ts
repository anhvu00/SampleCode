import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ac-table',
  templateUrl: './ac-table.component.html',
  styleUrls: ['./ac-table.component.scss']
})
export class AcTableComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  rows = [
    {id: 1, desc: "master/general description 1", showDetail: false},
    {id: 2, desc: "master description 2", showDetail: false},
  ]
}
