import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {emailValidator, matchingPasswords} from '../../../../../appsettings/utils/app-validators';

// import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'kt-sheet2',
    templateUrl: './sheet2.component.html',
    styleUrls: ['./sheet2.component.scss']
})
export class Sheet2Component implements OnInit {

    public form: FormGroup;

    constructor(public fb: FormBuilder, public router: Router) {

        this.form = this.fb.group({
            'username': [null, Validators.compose([Validators.required, Validators.pattern(/^[a-zA-Z][a-zA-Z0-9]{3,15}$/)])],
            'email': [null, Validators.compose([Validators.required, emailValidator])],
            'password': ['', Validators.required],
            'confirmPassword': ['', Validators.required],

            'phoneNumber': [null, Validators.compose([Validators.required, Validators.pattern(/^\d{5,20}$/)])],

            'role': ['User'],
        }, {validator: matchingPasswords('password', 'confirmPassword')});
    }


    ngOnInit() {
    }

    public onSubmit(values: Object): void {
        if (this.form.valid) {
            this.router.navigate(['/login']);
        }
        console.log(JSON.stringify(values));
    }

    onSubmit111({value, valid}) {
        if (!valid) return;
        console.log(JSON.stringify(value));
    }
}