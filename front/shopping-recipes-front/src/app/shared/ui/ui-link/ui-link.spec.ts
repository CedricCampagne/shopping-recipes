import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiLink } from './ui-link';

describe('UiLink', () => {
  let component: UiLink;
  let fixture: ComponentFixture<UiLink>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UiLink],
    }).compileComponents();

    fixture = TestBed.createComponent(UiLink);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
