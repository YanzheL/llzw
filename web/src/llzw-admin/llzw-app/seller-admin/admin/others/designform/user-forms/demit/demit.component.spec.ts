import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DemitComponent } from './demit.component';

describe('DemitComponent', () => {
  let component: DemitComponent;
  let fixture: ComponentFixture<DemitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DemitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DemitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
