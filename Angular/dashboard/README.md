BUILD:
npm i
ng build
ng serve -o

## 2/22/21
## https://www.smashingmagazine.com/2020/07/responsive-dashboard-angular-material-ng2-charts-schematics/
## Try to find a page layout

ng add @angular/material
npm install ng2-charts --save
npm install chart.js --save
npm install --save-dev ng2-charts-schematics

## Menu - left column
ng generate @angular/material:navigation nav

## Dashboard
ng generate @angular/material:dashboard myboard

## Card - reusable component (input = content)
ng g c card -m app --style css

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

# charts
ng generate ng2-charts-schematics:radar charts/product-sales-chart
ng generate ng2-charts-schematics:pie charts/sales-traffic-chart
ng generate ng2-charts-schematics:line charts/annual-sales-chart 
ng generate ng2-charts-schematics:bar charts/store-sessions-chart

# table
ng generate @angular/material:table orders-table

## 3/11/21
- Added Toolbar with navigation/routing
- Add a Dashboard template