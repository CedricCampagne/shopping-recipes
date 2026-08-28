import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingListSavedCard } from './shopping-list-saved-card';

describe('ShoppingListSavedCard', () => {
  let component: ShoppingListSavedCard;
  let fixture: ComponentFixture<ShoppingListSavedCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShoppingListSavedCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ShoppingListSavedCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
