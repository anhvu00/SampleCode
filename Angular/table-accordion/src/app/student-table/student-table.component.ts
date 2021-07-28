import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-student-table',
  templateUrl: './student-table.component.html',
  styleUrls: ['./student-table.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class StudentTableComponent implements OnInit {

  dataStudentsList = new MatTableDataSource();
  displayedStudentsColumnsList: string[] = ['id', 'name', 'age', 'address', 'actions'];
  isTableExpanded = false;

  constructor() { }

  ngOnInit(): void {
    this.dataStudentsList.data = this.STUDENTS_DATA;
  }

  toggleTableRows() {
   this.isTableExpanded = !this.isTableExpanded;

   this.dataStudentsList.data.forEach((row: any) => {
     row.isExpanded = this.isTableExpanded;
   })
 }

  STUDENTS_DATA = [
    {
       "id":1,
       "name":"Abby Jaskolski ",
       "age":21,
       "address":1.0079,
       "isExpanded":false,
       "subjects":[
          {
             "name":"Bio",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Chemistry",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Physics",
             "type":"Medical",
             "grade":"A"
          }
       ]
    },
    {
       "id":2,
       "name":"Jabari Fritsch",
       "age":20,
       "address":1.0079,
       "isExpanded":false,
       "subjects":[
          {
             "name":"Bio",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Chemistry",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Physics",
             "type":"Medical",
             "grade":"A"
          }
       ]
    },
    {
       "id":3,
       "name":"Maybell Simonis",
       "age":21,
       "address":1.0079,
       "isExpanded":false,
       "subjects":[
          {
             "name":"Bio",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Chemistry",
             "type":"Medical",
             "grade":"A"
          },
          {
             "name":"Physics",
             "type":"Medical",
             "grade":"A"
          }
       ]
    }
 ];

}
