import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import {
  Arbeitsgruppe,
  Mitarbeiter,
  apiAnswer
} from "src/app/utils/interfaces/default.model";
import { ApiService } from "src/app/utils/services/api.service";
import Swal from "sweetalert2";
import { DataService } from "src/app/utils/services/data.service";
import { ClrDatagridSortOrder } from "@clr/angular";

@Component({
  selector: 'dashboard-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.scss']
})

export class GroupsComponent implements OnInit {

  //input which groups should be displayed
  @Input() groups: Array<Arbeitsgruppe>;
  //send refresh to parents component if the dashboard should refresh
  @Output() refresh = new EventEmitter<boolean>();
  //data for template binding, names are self explaining
  public groupMembers: Array<Array<Mitarbeiter>> = [];
  public leaders: Array<Mitarbeiter> = [];
  public user: Mitarbeiter;
  public allowedToDelete: boolean;
  public password: string;
  public openModal: boolean[] = [];
  public descSort = ClrDatagridSortOrder.DESC;
  public loading: boolean = true;

  constructor(private api: ApiService,
    public dataService: DataService) { }

  /**
   * initialize data and fill the view
   */
  ngOnInit(): void {
    this.openModal.fill(false, 0, this.groups.length)

    this.user = this.dataService.getUser();
    this.allowedToDelete = ['root', 'admin'].includes(this.user.rechteklasse);

    for (let g of this.groups) {
      this.api.getAllUsersFromGroup(g.arbeitsgruppenID).then(answer => {
        if ((answer as apiAnswer)?.fehler)
          return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + (answer as apiAnswer).fehler, "error");
        this.groupMembers.push(answer[1] as Mitarbeiter[]);
        this.leaders.push(answer[0] as Mitarbeiter);
        this.loading = false;
      });
    }

  }

  //open window with specific index (= group index)
  public deleteGroup(index: number) {
    this.openModal[index] = true;
  }

}
