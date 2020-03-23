import { Pipe, PipeTransform } from "@angular/core";
import { Mitarbeiter } from "../interfaces/default.model";
import { EmployeeNamePipe } from "./employee-name.pipe";

@Pipe({
  name: "listEmployees"
})

export class ListEmployeesPipe implements PipeTransform {
  constructor(private namePipe: EmployeeNamePipe) {}

  transform(value: Mitarbeiter[], length = 2): string {
    let str: string = "";

    for (var i = 0; i < value.length && i < length; i++) {
      str +=
        i === 0 && i !== value.length - 1
          ? `${this.namePipe.transform(value[i])}, `
          : `${this.namePipe.transform(value[i])}`;
    }

    return i === length ? str : str + "...";
  }
}
