import { Component, inject } from '@angular/core';
import { UIStore } from '../ui.store';

@Component({
  selector: 'app-ui-messages',
  standalone: true,
  imports: [],
  templateUrl: './ui-messages.html',
  styleUrl: './ui-messages.css',
})
export class UiMessages {
  ui = inject(UIStore);

}
