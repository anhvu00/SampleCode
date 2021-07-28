import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StudentTableComponent } from './student-table/student-table.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
//import { ExpansionExpandCollapseAllExampleComponent } from './expansion-expand-collapse-all-example/expansion-expand-collapse-all-example.component';

import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material/core';
import { AcTableComponent } from './ac-table/ac-table.component';
import { HTableComponent } from './h-table/h-table.component';


@NgModule({
  declarations: [
    AppComponent,
    StudentTableComponent,
    AcTableComponent,
    HTableComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatListModule,
    MatButtonModule,

    FormsModule,
    HttpClientModule,
    MatNativeDateModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
