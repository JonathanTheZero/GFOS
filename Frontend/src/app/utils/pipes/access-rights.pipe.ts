import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'accessRights'
})

export class AccessRightsPipe implements PipeTransform {

  transform(value: string): string {
    if(value === "root") return "Root";
    if(value === "admin") return "Administrator";
    if(value === "personnelDepartment") return "Personalabteilung";
    if(value === "headOfDepartment") return "Gruppenleiter";
    if(value === "user") return "Benutzer";
  }

}
