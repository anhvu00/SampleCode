import { Component, OnInit } from '@angular/core';
/*
2/24/21
- mat-card properties
*/
@Component({
  selector: 'app-cardstatic',
  templateUrl: './cardstatic.component.html',
  styleUrls: ['./cardstatic.component.css']
})
export class CardstaticComponent implements OnInit {
  imagesrc: string = "/assets/images/ARI.png";
  constructor() {  }

  ngOnInit(): void {
  }

}
