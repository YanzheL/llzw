import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {EmbryoService} from '../../Services/Embryo.service';

@Component({
    selector: 'embryo-SignIn',
    templateUrl: './CommonSignIn.component.html',
    styleUrls: ['./CommonSignIn.component.scss']
})
export class CommonSignInComponent implements OnInit {
    temp = '';
    rootUser = {'username': '', 'password': ''};
    flag = false;
    username = new FormControl('test_user_seller_username_1', [
        Validators.required,
    ]);
    password = new FormControl('test_user_seller_PASSWORD_1', [
        Validators.required,
    ]);

    constructor(private route: ActivatedRoute,
                private router: Router,
                public embryoService: EmbryoService) {
    }

    ngOnInit() {
    }

    public logerr(value: any) {
        const message = value;
        this.embryoService.confirmationPopup(message).subscribe(res => {
            },
            err => console.log(err)
            // ()  => this.getPopupResponse(this.popupResponse, value, 'cart')
        );
    }

    OnSubmit() {
        this.rootUser.username = this.username.value;
        this.rootUser.password = this.password.value;
        if (this.username.valid && this.password.valid) {
            this.embryoService.login(this.rootUser).subscribe(res => {
                    localStorage.setItem('rootUser', JSON.stringify(this.rootUser));
                    this.router.navigate(['/']);
                },
                error1 => {
                    console.log(error1);
                    this.flag = error1.error.success;
                    if (!this.flag) {
                        this.logerr(error1.error.error.message);
                    }
                });
        }

    }
}
