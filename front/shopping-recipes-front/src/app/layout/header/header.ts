import { Component, signal } from '@angular/core';
import { AuthStateService } from '../../auth/services/auth-state.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  isFetching = signal(false);

  constructor(
    private authState: AuthStateService,
    private router: Router 
  ){}

  logout() {
    this.isFetching.set(true);
    setTimeout(()=>{
      this.isFetching.set(false);
      this.authState.logout();
      this.router.navigate(['/login']);
    }, 2500)
  }
}
