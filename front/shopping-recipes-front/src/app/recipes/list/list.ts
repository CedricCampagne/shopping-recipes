import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list',
  imports: [],
  templateUrl: './list.html',
  styleUrl: './list.css',
})
export class List {

  constructor(
    private router: Router 
  ){}

  data = [
    { id: 1, title: 'Recette 1', description: 'Description 1' },
    { id: 2, title: 'Recette 2', description: 'Description 2' },
    { id: 3, title: 'Recette 3', description: 'Description 3' }
  ];

  goToRecipe(id: number) {
    this.router.navigate([`/app/recipes/${id}`])
  }
}
