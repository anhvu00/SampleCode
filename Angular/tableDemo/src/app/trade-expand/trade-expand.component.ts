/* 2/9/21 - Trade expandable table
*  - Based on table-expand component
*  - Call TradeServer v1.0, get all trades 
* TODO:
*  - Find a working Angular nested table component/lib
*  - Add button to get gain/loss report, re-entry points
*/

import { Component, OnInit } from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { Observable } from 'rxjs';
import { TradeService } from '../service/trade.service';

@Component({
  selector: 'app-trade-expand',
  templateUrl: './trade-expand.component.html',
  styleUrls: ['./trade-expand.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})

export class TradeExpandComponent implements OnInit {
  // sample hard code data
  dataSource = TRADE_DATA;
  // real data from mysql/trade server
  dataSource_ws = new Observable<any>();
  columnsToDisplay: string[] = ['symbol', 'sellQuantity', 'buyPrice', 'target', 'buyTotal',
  'sellDate', 'sellPrice', 'sellTotal', 'gainLoss', 'gainLossPct'];
  expandedElement!: Trade | null;
  constructor(private svc: TradeService) { 
    this.dataSource_ws = svc.getAll();
  }

  ngOnInit(): void { }

}

export interface Trade {
  /*
   ['symbol', 'sellQuantity', 'buyPrice', 'target', 'buyTotal',
  'sellDate', 'sellPrice', 'sellTotal', 'gainLoss', 'gainLossPct'
  */
  symbol: string;
  sellQuantity: number;
  buyPrice: number;
  target: number;
  buyTotal: number;
  sellDate: string; // change to date later...
  sellPrice: number;
  sellTotal: number;
  gainLoss: number;
  gainLossPct: number;
  notes: string;
}

const TRADE_DATA: Trade[] = [
  {
    symbol: 'ROKU',
    sellQuantity: 100,
    buyPrice: 1.05,
    target: 4.50,
    buyTotal: 105,
    sellDate: '2021-02-09',
    sellPrice: 2.05,
    sellTotal: 205,
    gainLoss: 100,
    gainLossPct: 0.5,
    notes: `This is my trade...`
  }, 
];
