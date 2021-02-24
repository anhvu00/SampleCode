import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MyboardComponent } from './myboard/myboard.component';

const routes: Routes = [{ path: 'dashboard', component: MyboardComponent }];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }