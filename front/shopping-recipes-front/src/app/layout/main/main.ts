import { Component } from '@angular/core';
import { RouterModule } from "@angular/router";
import { Header } from "../header/header";

@Component({
  selector: 'app-main',
  imports: [RouterModule, Header],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {}
