import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DesignformComponent } from './designform.component';

describe('DesignformComponent', () => {
  let component: DesignformComponent;
  let fixture: ComponentFixture<DesignformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DesignformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DesignformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
