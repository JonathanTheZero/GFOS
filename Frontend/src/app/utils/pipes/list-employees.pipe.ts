import { Pipe, PipeTransform } from "@angular/core";
import { Mitarbeiter } from "../interfaces/default.model";
import { EmployeeNamePipe } from "./employee-name.pipe";

@Pipe({
  name: "listEmployees"
})

export class ListEmployeesPipe implements PipeTransform {
  constructor(private namePipe: EmployeeNamePipe) {}

  transform(value: Mitarbeiter[], length = 2): string {

    if(value.length > 2) return `${value[0].vorname} ${value[0].name}, ${value[1].vorname} ${value[1].name}...`;

    let str: string = "";
    for(var i = 0; i < value.length; i++) {
        str += `${value[i].vorname} ${value[i].name}, `
    }

    return str.substr(0, str.length - 2);
  }    
}
