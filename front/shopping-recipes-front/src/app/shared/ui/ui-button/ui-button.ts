import { Component, input } from '@angular/core';

@Component({
  selector: 'app-ui-button',
  standalone: true,
  imports: [],
  templateUrl: './ui-button.html',
  styleUrl: './ui-button.css',
})
export class UiButton {
  variant = input<'primary' | 'danger' | 'secondary'>('primary');
  size = input<'sm' | 'md' | 'lg'>('md');

  get buttonClasses(): string {
    const variants = {
      primary: 'bg-primary text-white hover:bg-primary/80',
      danger: 'bg-danger text-white hover:bg-danger/80',
      secondary: 'bg-surface text-text border border-primary',
    };

    const sizes = {
      sm: 'px-2 py-1 text-sm',
      md: 'px-4 py-2',
      lg: 'px-6 py-3 text-lg',
    };

    return `
      cursor-pointer
      border-2
      rounded-lg
      font-medium
      transition-colors
      duration-500
      ${variants[this.variant()]}
      ${sizes[this.size()]}
    `;
  }
}
