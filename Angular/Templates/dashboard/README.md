## 2/24/21
Source = https://www.smashingmagazine.com/2020/07/responsive-dashboard-angular-material-ng2-charts-schematics/

## Goals
- Angular Material grid layout
- How to add component and data to each card
- Find a nice css color scheme
- Use this a a template for new projects

## Done
- A basic dashboard layout of cards

## TODO
- Add chart, table, styling/color

## COMPILE

ng add @angular/material
npm install ng2-charts --save
npm install chart.js --save
npm install --save-dev ng2-charts-schematics

# charts
ng generate ng2-charts-schematics:radar charts/product-sales-chart
ng generate ng2-charts-schematics:pie charts/sales-traffic-chart
ng generate ng2-charts-schematics:line charts/annual-sales-chart 
ng generate ng2-charts-schematics:bar charts/store-sessions-chart

# table
ng generate @angular/material:table orders-table

## Menu - left column
ng generate @angular/material:navigation nav

## Dashboard
ng generate @angular/material:dashboard myboard

## Card - reusable component (input = content)
ng g c card -m app --style css