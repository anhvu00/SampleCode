import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
// import this to read/Observe data.json
import { HttpClientModule } from '@angular/common/http';
// import delete icon
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
// import this to help with Form. How?
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app-component/app.component';
import { AddComponent } from './components/add/add.component';
import { SearchComponent } from './components/search/search.component';
import { ListComponent } from './components/list/list.component';
import { SpreadComponent } from './components/spread/spread.component';

@NgModule({
  declarations: [
    AppComponent,
    AddComponent,
    SearchComponent,
    ListComponent,
    SpreadComponent
  ],
  imports: [BrowserModule, HttpClientModule, FontAwesomeModule, FormsModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
