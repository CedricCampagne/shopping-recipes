import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UiLink } from "../shared/ui/ui-link/ui-link";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, UiLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
