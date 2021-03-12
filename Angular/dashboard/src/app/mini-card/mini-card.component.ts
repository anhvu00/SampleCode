import { Component, OnInit } from '@angular/core';

export const mc = {
  icon: 'icon',
  title: 'title',
  value: 200,
  color: 'red',
  isIncrease: false,
  isCurrency: true,
  duration: '20',
  percentValue: 50
}

@Component({
  selector: 'app-mini-card',
  templateUrl: './mini-card.component.html',
  styleUrls: ['./mini-card.component.css']
})
export class MiniCardComponent implements OnInit {
  icon: string = 'icon';
  title: string = 'title';
  value: number = 200;
  color: string = 'red';
  isIncrease: boolean = false;
  isCurrency: boolean = true;
  duration: string = '20';
  percentValue: number = 50;
  
  constructor() { }

  ngOnInit(): void {} 

}
