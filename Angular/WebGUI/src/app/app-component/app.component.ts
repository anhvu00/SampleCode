import { Component, OnInit } from '@angular/core';
// import HttpClient (not HttpClientModule)
import { HttpClient } from '@angular/common/http';
// use fontAwesome
import { library } from '@fortawesome/fontawesome-svg-core';
// the "x" and "+" icon
import { faTimes, faPlus } from '@fortawesome/free-solid-svg-icons';
// lodash util function - array slicer
import { without } from 'lodash';

// (global definition) Add font to the library (local object)
library.add( faTimes, faPlus );

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'Wisdom Pet Medicine';
  // local var to keep the read data, note the small "o"
  theList: object[];
  // NFL spread list
  spreadList: object[];
  // winloss list
  winlossList: object[];

  // handle the add event from child component
  addApt( anApt: object ) {
    // use javascript function unshift to push to the array
    this.theList.unshift(anApt);
    console.log(this.theList);
  }

  // handle the delete bound in html = remove from theList
  deleteApt( anApt: object) {
    this.theList = without(this.theList, anApt);
  }

  // handle edited spread
  editSpreadEvt( spread: object[] ) {
    // TODO: update the visitor spread = homespread * (-1)
    this.spreadList = spread.splice(0);
    console.log("new spread is");
    console.log(this.spreadList);
  }

  // declare type (best practice)
  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    // read (or subscribe) to the data.json
    // param 'data' is a function
    // need to cast Object[] so we can assign to local var
    this.http.get<Object[]>('../assets/data.json').subscribe(data => {
      this.theList = data;
    });
    // load spread json
    this.http.get<Object[]>('../assets/data-spread.json').subscribe(data => {
      this.spreadList = data;
      console.log(this.spreadList);
    });
    // load win/loss. TODO: find a better way to update this info
    this.http.get<Object[]>('../assets/data-winloss.json').subscribe(data => {
      this.winlossList = data;
      console.log(this.winlossList);
    });
  }
}
