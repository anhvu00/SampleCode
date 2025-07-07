import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';


export const SPREAD_DATA = [
  {"gameId": 1, "gameDate": "2021-09-12", "visitor": "ARI", "home": "SEA", "gamePeriod": "FH", "visitorSpread": -1, "homeSpread": -1, "overUnder": 21},
  {"gameId": 2, "gameDate": "2021-09-12", "visitor": "ARI", "home": "SEA", "gamePeriod": "WG", "visitorSpread": 3.5, "homeSpread": -3.5, "overUnder": 42},
];

export const SPREAD_SCHEMA = {
  "gameId": "number",
  "gameDate": "date",
  "visitor": "text",
  "home": "text",
  "gamePeriod": "text",
  "visitorSpread": "number",
  "homeSpread": "number",
  "overUnder": "number",
  "edit": "edit"
}

@Component({
  selector: 'app-table-spread',
  templateUrl: './table-spread.component.html',
  styleUrls: ['./table-spread.component.scss']
})
export class TableSpreadComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  displayedColumns2: string[] = ['gameDate', 'visitor', 'home', 'gamePeriod', 'visitorSpread', 'homeSpread', 'overUnder', 'edit'];
  dataSource2 = SPREAD_DATA;
  dataSchema2 = SPREAD_SCHEMA;

  editSpread(element) {
    if (element.isEdit) {
      element.isEdit = false;
      // save data
      // flip between visitor and home spread
      console.log('saving ' + element.homeSpread + ', visitorspread=' + -element.homeSpread + ', gameId=' + element.gameId);
    } else {
      element.isEdit = true;
    }
  }
}




