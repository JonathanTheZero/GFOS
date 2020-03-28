import { Component, OnInit } from '@angular/core';
import { errorObj } from 'src/app/utils/interfaces/register.model';
import { ApiService } from 'src/app/utils/services/api.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})

export class DeleteComponent implements OnInit {

  //binding for forms
  public form: any = {
    username: "",
    password: ""
  }

  //error object which is passed to error-component
  public err: errorObj = {
    reason: ""
  };

  constructor(public api: ApiService) { }

  ngOnInit(): void {

  }

  //validate user input and change the error object if needed, otherwise send data to api
  public send(): void {
    if(!this.form.username){
      this.err.reason = "Bitte geben Sie die Personalnummer an!";
      return;
    }
    
    if(!this.form.password){
      this.err.reason = "Bitte geben Sie Ihr Passwort ein!";
      return;
    }

    this.api.deleteUser(this.form.password, this.form.username).then(answer =>  {
      if(answer?.fehler)
        return Swal.fire("Fehler", "Es ist folgender Fehler aufgetreten: " + answer.fehler, "error");

      return Swal.fire("", "Der Benutzer wurde erfolgreich gelöscht", "success");
    }); 
  }

}
