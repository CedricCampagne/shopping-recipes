import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientCard } from './ingredient-card';

describe('IngredientCard', () => {
  let component: IngredientCard;
  let fixture: ComponentFixture<IngredientCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngredientCard],
    }).compileComponents();

    fixture = TestBed.createComponent(IngredientCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
