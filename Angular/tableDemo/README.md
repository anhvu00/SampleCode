# 2/10/21 - Table Demo

PURPOSE:
- Demo material table, expandable rows. It is based on an example on the Angular website. 

DESCRIPTION:
- This application shows a history of stock trades. The end goal is to help me keep track of gain/loss and figure out if/when to buy the stock again.
- There are 2 datasources: one is a hard coded array of data. The other is a HTTP REST call to a Java/mysql server (see ...)

TODO:
- Find/build nested table or master-detail table. Most example online don't work (agtable, ngx-nested-table, etc.)
- Find better CSS/animation
- Add buttons, more features.

USAGE:
ng new <yourproject>. Select Y for routing.
cd to <yourproject>
npm i @angular-devkit/build-angular
ng g trade-expand (this creates and wires the trade-expand component for you)
copy my trade-expand/ folder to <yourproject>/src/app/ and change the necessary datasource and columns
ng g service service/trade
copy my service/ folder to <yourproject>/src/app/

ng add @angular/material, select Y for everything.
In app.module.ts:
- import {MatTableModule} from '@angular/material/table';
- imports MatTableModule
- import { HttpClientModule } from '@angular/common/http';
- imports HttpClientModule

In trade-expand.component.html:
Use "dataSource" for hard coded data array or "dataSource_ws" if you have the Java/mysql server running.

COMPILE/RUN:
ng build
ng serve -o 

