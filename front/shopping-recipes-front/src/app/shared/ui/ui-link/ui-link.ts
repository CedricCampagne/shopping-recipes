import { Component, input } from '@angular/core';

@Component({
  selector: 'app-ui-link',
  imports: [],
  templateUrl: './ui-link.html',
  styleUrl: './ui-link.css',
})
export class UiLink {
  variant = input<'primary' | 'danger' | 'secondary'>('primary');
  size = input<'sm' | 'md' | 'lg'>('md');

  get linkClasses(): string {
    const sizes = {
      sm: 'px-2 py-1 text-sm',
      md: 'px-4 py-2',
      lg: 'px-6 py-3 text-lg',
    };

    return `
      cursor-pointer
      font-medium
      border-2
      rounded-lg
      text-primary
      hover:bg-primary/10
      hover:text-primary
      hover:border-transparent
      transition-colors
      duration-500
      ${sizes[this.size()]}
    `;
  }
}
