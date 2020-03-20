import { Pipe, PipeTransform } from '@angular/core';
import { Mitarbeiter } from '../interfaces/default.model';

@Pipe({
  name: 'employeeName'
})

export class EmployeeNamePipe implements PipeTransform {

  /**
   * This method takes an employee object and return the full name
   * @param value an employee object
   * @return the fullname
   */
  transform(value: Mitarbeiter): string {
    return value.vorname + " " + value.name;
  }

}
