import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Sheet1Component} from './sheet1.component';

describe('Sheet1Component', () => {
    let component: Sheet1Component;
    let fixture: ComponentFixture<Sheet1Component>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [Sheet1Component]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(Sheet1Component);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
