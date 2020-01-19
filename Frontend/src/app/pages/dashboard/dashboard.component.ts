import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent implements OnInit {

  public readonly samples: todo[] = [
    {
      title: "Test"
    },
    {
      title: "Test2"
    },
    {
      title: "Test3",
      subs: [
        {
          title: "Test4"
        },
        {
          title: "Test5"
        }
      ]
    }
  ]

  constructor() { }

  ngOnInit() {
  }

  public check(event: any): void{ 

    const flatTodos = (arr: todo[]): todo[] => {
      var newArr = [];
      for(let i = 0;i< arr.length;i++){
        newArr.push(arr[i]);
        if(arr[i].subs) newArr.push(...flatTodos(arr[i].subs));
      }
      return newArr;
    } //self written method, in ES2019/ESNEXT use Array.prototype.flat

    var bar : any = document.getElementById("bar");
    bar.value += 100 / flatTodos(this.samples).length;
    console.log(event);
    if(event.path.length >= 19){ //if user clicks on the icon, disable the button nevertheless
      event.path[1].disabled = true;
    }
    else {
      event.target.disabled = true;
    }
  }

}

interface todo {
  title: string;
  subs?: todo[]
}