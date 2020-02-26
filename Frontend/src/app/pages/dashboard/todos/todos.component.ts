import { Component, OnInit } from '@angular/core';
import { Todo } from 'src/app/utils/interfaces/dashboard.model';
import { ApiService } from 'src/app/utils/api.service';

@Component({
  selector: 'dashboard-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.scss']
})
export class TodosComponent implements OnInit {

  samples: Todo[];

  constructor(private api: ApiService) { }

  ngOnInit(): void {
    this.api.getTodoSamples().subscribe(samples => this.samples = samples);
  }

  public check(event: any): void{ 

    const flatTodos = (arr: Todo[]): Todo[] => {
      var newArr = [];
      for(let i = 0;i< arr.length;i++){
        newArr.push(arr[i]);
        if(arr[i].subs) newArr.push(...flatTodos(arr[i].subs));
      }
      return newArr;
    } //self written method, in ES2019/ESNEXT use Array.prototype.flat

    var bar : any = document.getElementById("bar");
    bar.value += 100 / flatTodos(this.samples).length;

    if(event.path.length >= 19){ //if user clicks on the icon, disable the button nevertheless
      event.path[1].disabled = true;
    }
    else {
      event.target.disabled = true;
    }
  }

}