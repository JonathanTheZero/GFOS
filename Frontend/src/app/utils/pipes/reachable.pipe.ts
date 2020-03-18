import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'reachable'
})
export class ReachablePipe implements PipeTransform {

  transform(value: boolean): string {
    return (value) ? "Ja" : "Nein";
  }

}
