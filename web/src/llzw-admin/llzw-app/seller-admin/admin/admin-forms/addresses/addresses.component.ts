import {Component, OnInit} from '@angular/core';

import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'kt-addresses',
    templateUrl: './addresses.component.html',
    styleUrls: ['./addresses.component.scss']
})
export class AddressesComponent implements OnInit {

    public form: FormGroup;

    constructor(public fb: FormBuilder, public router: Router) {

        this.form = this.fb.group({
            'owner_id': [null, Validators.compose([Validators.required])],
            'province': [null, Validators.compose([Validators.required])],
            'city': [null, Validators.compose([Validators.required])],
            'district': [null, Validators.compose([Validators.required])],
            'address': [null, Validators.compose([Validators.required])],
            'zip': ['000000', Validators.compose([Validators.nullValidator, Validators.minLength(6), Validators.maxLength(6)])], // 验证6数字字符

        });
    }

    ngOnInit() {
    }

    public onSubmit(values: Object): void {
        if (this.form.valid) {
            this.router.navigate(['/login']);
        }
        console.log(JSON.stringify(values));
    }
}
