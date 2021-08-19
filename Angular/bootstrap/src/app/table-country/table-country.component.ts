import { Component, OnInit } from '@angular/core';
import { Country } from '../model/country';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CountryDetailComponent } from '../country-detail/country-detail.component'


@Component({
  selector: 'app-table-country',
  templateUrl: './table-country.component.html',
  styleUrls: ['./table-country.component.scss']
})
export class TableCountryComponent implements OnInit {
  currentRate = 8;
  countries: Country[] = [
    {
      name: 'Russia',
      flag: 'f/f3/Flag_of_Russia.svg',
      area: 17075200,
      population: 146989754
    },
    {
      name: 'India',
      flag: "4/41/Flag_of_India.svg",
      area: 3287263,
      population: 1324171354
    },
    {
      name: 'Canada',
      flag: 'c/cf/Flag_of_Canada.svg',
      area: 9976140,
      population: 36624199
    },
    {
      name: 'United States',
      flag: 'a/a4/Flag_of_the_United_States.svg',
      area: 9629091,
      population: 324459463
    },
  ];

  constructor(private modalService: NgbModal) { }

  open(country: Country) {
    const modalRef = this.modalService.open(CountryDetailComponent);
    modalRef.componentInstance.country = country;
  }

  ngOnInit(): void {
  }


}
