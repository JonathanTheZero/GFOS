import { Component, OnInit } from "@angular/core";
import { loginForm } from "src/app/utils/interfaces/login.model";
import { Title } from "@angular/platform-browser";
import { errorObj } from "src/app/utils/interfaces/register.model";
import { ApiService } from "src/app/utils/services/api.service";
import { apiAnswer } from "src/app/utils/interfaces/default.model";
import { Router, ActivatedRoute } from "@angular/router";
import Swal from "sweetalert2";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})

export class LoginComponent implements OnInit {

  //template binding and error handling
  form: loginForm = {
    username: "",
    password: ""
  };

  public err: errorObj = {
    reason: ""
  };

  //save possible redirect url
  url?: string;

  constructor(
    private titleService: Title,
    public api: ApiService,
    private router: Router,
    private route: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    this.titleService.setTitle("Login");
    this.route.queryParams.subscribe(params => this.url = params["returnUrl"]);
  }

  public submit() {
    if(this.form.password.length == 0 || this.form.username.length == 0) {
      this.err.reason = 'Bitte füllen Sie die Felder aus.';
      return;
    }
    this.api.login(
      this.form.username,
      this.form.password
    ).then((answer: apiAnswer) => {
      if (answer.erfolg) {
        // if the user successfully logged in redirect him after either 5 seconds or when he closes the pop-up
        // whatever comes first
        Swal.fire("", "Sie sind nun eingeloggt", "success").then(() =>
          this.router.navigate(["/dashboard"])
        );
        setTimeout(() => this.router.navigate(["/dashboard"]), 5000);
      } else {
        this.err.reason = answer.fehler;
      }
    });
  }
}
