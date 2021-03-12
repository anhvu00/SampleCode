import { Component, OnInit } from '@angular/core';

/*
2/24/21 - 
- Static/hard coded board = learn basic grid-list
- Test out cards, css, input data, action
- 
*/
@Component({
  selector: 'app-myboardstatic',
  templateUrl: './myboardstatic.component.html',
  styleUrls: ['./myboardstatic.component.css']
})
export class MyboardstaticComponent implements OnInit {
  title = 'My static grid';
  constructor() { }

  ngOnInit(): void {
  }

}
