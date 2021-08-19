import { Component, Input, OnInit } from '@angular/core';
import { Country } from '../model/country';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-country-detail',
  templateUrl: './country-detail.component.html',
  styleUrls: ['./country-detail.component.scss']
})
export class CountryDetailComponent implements OnInit {

  ngOnInit(): void {
  }

  // interface Country is defined in table-country.component.ts
  @Input() country: Country;

  constructor(public activeModal: NgbActiveModal) { }

}
