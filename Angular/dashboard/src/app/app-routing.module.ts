import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MyboardComponent } from './myboard/myboard.component';
import { HomepageComponent } from './homepage/homepage.component';


const routes: Routes = [
  { path: '', component: HomepageComponent },
  { path: 'dashboard', component: MyboardComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }