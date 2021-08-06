import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-tab-group',
  templateUrl: './tab-group.component.html',
  styleUrls: ['./tab-group.component.scss']
})
export class TabGroupComponent implements OnInit {
  buttonTitle: string = "Hide Filter2";
  visible: boolean = true;
  constructor() { }

  ngOnInit(): void {
  }
  
  showhideFilter2(){
    this.visible = this.visible?false:true;
    this.buttonTitle = this.visible?"Hide Filter2":"Show Filter2";
  }
}
