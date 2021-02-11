import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TradeExpandComponent } from '../trade-expand/trade-expand.component';

@Injectable({
  providedIn: 'root'
})

export class TradeService {
  private readonly URL = 'http://localhost:8080/trade/getAll';

  constructor(private http: HttpClient) { }

  getAll(): Observable<TradeExpandComponent[]> {
    console.log('Request is sent!');
    return this.http.get<TradeExpandComponent[]>(this.URL);
  }
}
