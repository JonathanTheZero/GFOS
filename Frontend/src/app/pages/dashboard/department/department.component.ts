import { Component, OnInit, Input } from "@angular/core";
import { ApiService } from "src/app/utils/services/api.service";
import { Mitarbeiter, apiAnswer } from "src/app/utils/interfaces/default.model";
import { ClrDatagridSortOrder } from "@clr/angular";
import { DataService } from "src/app/utils/services/data.service";
import Swal from 'sweetalert2';

@Component({
  selector: "dashboard-department",
  templateUrl: "./department.component.html",
  styleUrls: ["./department.component.scss"]
})

export class DepartmentComponent implements OnInit {

  @Input() employees: Mitarbeiter[];
  ready = true;
  descSort = ClrDatagridSortOrder.DESC;

  constructor() { }


  ngOnInit(): void {
  }
}
