import {FormControl, FormGroup} from '@angular/forms';

// 验证Hash(SH1)
export function sha1_64_Validator(control: FormControl): { [key: string]: any } {

    let patrn = /\b([a-f0-9]{64})\b/;
    let patrnOK: boolean = control.value && patrn.test(control.value);

    // 输入正确  或 输入为空
    if (patrnOK || !control.value) {
        return null;
    } else {
        // return { 'invalidInfo': '密码需满足: At least 1 upper case, 1 lower case, 1 digit and 1 symbol, min length = 4' };
        return {invalidInfo: 'SHA1(64) 格式不对'};
    }
}


export function emailValidator(control: FormControl): { [key: string]: any } {
    let emailRegexp = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/;
    if (control.value && !emailRegexp.test(control.value)) {
        return {invalidEmail: true};
    }
}

// password: At least 1 upper case, 1 lower case, 1 digit and 1 symbol, min length = 8
export function passwordlValidator(control: FormControl): { [key: string]: any } {
    // 最少包含1个大写字母、1个小写字母、1个数字、一个指定的特殊字符、长度4到256

//密码强度正则，最少6位，包括至少1个大写字母，1个小写字母，1个数字，1个特殊字符
    let patrn = /^.*(?=.{6,256})(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[_!@#$%^&*? ]).*$/;

    //let patrn = /^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()])[\da-zA-Z!@#$%^&_*()]{6,256}$/;
    // let minLength = 6;
    // let maxLength = 256;
    // let lengthOK: boolean = control.value && control.value.length >= minLength;
    let patrnOK: boolean = control.value && patrn.test(control.value);

    // 输入正确  或  输入为空
    if (patrnOK || !control.value) {
        return null;
    } else {
        // return { 'invalidInfo': '密码需满足: At least 1 upper case, 1 lower case, 1 digit and 1 symbol, min length = 4' };
        return {'invalidInfo': 'password 需足够复杂'};
    }
}


export function matchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
        let password = group.controls[passwordKey];
        let passwordConfirmation = group.controls[passwordConfirmationKey];
        if (password.value !== passwordConfirmation.value) {
            return passwordConfirmation.setErrors({mismatchedPasswords: true});
        }
    };
}

// 用户名 严格认真
export function usernamelValidator(control: FormControl): { [key: string]: any } {
    // 用户名正则，4到16位（字母，数字，下划线，减号）
    let patrn1 = /^[a-zA-Z0-9_-]+$/;
    let patrn2 = /^[a-zA-Z0-9_-]{4,64}$/;
    let patrn3 = /\w+[\w\d]+/;
    let patrn4 = /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im;

    let patrn = patrn1;
    let minLength = 4;
    let maxLength = 64;
    let lengthOK: boolean = control.value && control.value.length >= minLength && control.value.length <= maxLength;
    let patrnOK: boolean = control.value && patrn.test(control.value);

    //  输入正确 或 输入为空
    if ((patrnOK && lengthOK) || !control.value) {
        return null;
    }
    if (!patrnOK) {
        return {'invalidInfo': 'username 不允许包含特殊字符'};
    }

    if (!lengthOK) {
        return {'invalidInfo': 'username 需满足: ' + minLength + ' ≤ 长度 ≤ ' + maxLength};
    }
}

//一般名字校验(此处不校验非空)
export function namelValidator(control: FormControl): { [key: string]: any } {

    //let patrn = /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im;


    //   /^[".chr(0xa1)."-".chr(0xff)."A-Za-z0-9_]+$/  ;//GB2312 汉字字母数字下划线正则表达式

    //   /^[\x{4e00}-\x{9fa5}A-Za-z0-9_]+$/u';    // UTF-8 汉字字母数字下划线正则表达式
    //   /^[a-zA-Z0-9_\\-\u4e00-\u9fa5]+$/  // UTF-8 满足中文，英文，数字，中划线，下划线的正则表达式


    //   /^(?!\d+$)[_a-zA-Z0-9]{4,16}$/    //可以由a-z A-Z的大小写英文字母、0-9的数字、下划线组成，长度在4-16个字符，不能单独使用数字

    let patrn1 = /^[a-zA-Z0-9_-]+$/;  //  UTF-8 英文，数字，中划线，下划线的正则表达式 长度在4-16个字符，不能单独使用数字
    let patrn2 = /^[a-zA-Z0-9_-\u4e00-\u9fa5]+$/;  //  UTF-8 满足中文，英文，数字，中划线，下划线的正则表达式 长度在4-16个字符，不能单独使用数字

    // /^[A-Za-z0-9\u4e00-\u9fa5]+$/
    // 其实[A-Za-z0-9]可以用\w来代替，只是\w还可以匹配一个下划线，如果必须要求只有数字和字母那还是用[A-Za-z0-9]这个好了

    let patrn4 = /^[\u4e00-\u9fa5\w-]+$/; //中文 + 字母 + 数字 + '_' + '-'

    //let patrn5 = /^[/u2E80-/u9FFF]+$/; //中日韩

    let patrn6 = /^[\u2E80-\u9FFF\w- ./()（）]+$/; //中文 + 字母 + 数字 + '_' + '-' + 空格 + 点.


    let patrn = patrn6;
    let minLength = 4;
    let maxLength = 64;
    let lengthOK: boolean = control.value && control.value.length >= minLength && control.value.length <= maxLength;
    let patrnOK: boolean = control.value && patrn.test(control.value);

    //  输入正确 或 输入为空
    if ((patrnOK && lengthOK) || !control.value) {
        return null;
    }
    if (!patrnOK) {
        return {'invalidInfo': '输入文字 不允许包含特殊字符'};
    }

    if (!lengthOK) {
        return {'invalidInfo': '输入文字 需满足: ' + minLength + ' ≤ 长度 ≤ ' + maxLength};
    }

}


export function phoneNumberlValidator(control: FormControl): { [key: string]: any } {
    // 电话号码正则，5到20位（数字）
    let patrn1 = /^\d+$/;
    // let patrn2 = /^\d{5,20}$/;
    // let patrn3 = /^\d{5,20}$/;

    let patrn = patrn1;
    let minLength = 5;
    let maxLength = 20;
    let lengthOK: boolean = control.value && control.value.length >= minLength && control.value.length <= maxLength;
    let patrnOK: boolean = control.value && patrn.test(control.value);

    // 输入正确  或 输入为空
    if ((patrnOK && lengthOK) || !control.value) {
        return null;
    }

    if (!patrnOK) {
        return {'invalidInfo': 'phoneNumber 只能是数字'};
    }

    if (!lengthOK) {
        return {'invalidInfo': 'phoneNumber 需满足: ' + minLength + '≤长度≤' + maxLength};
    }
}

//价格正则, 浮点数字, 最小 1.0 最大 9999999999999.99(万亿少1分)
export function pricelValidator(control: FormControl): { [key: string]: any } {

    //let patrn1 = /^[0-9]+(.[0-9]{2})?$/ ; // 验证有两位小数的正实数
    let patrn1 = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/  //正浮点数

    let patrn = patrn1;
    let minPrice = 1.0;
    let maxPrice = 9999999999999.99;

    let patrnOK: boolean = control.value && patrn.test(control.value);


    //js提供了parseInt()和parseFloat()两个转换函数 转换时会忽略字符串中非数字相关的字符
    let price = parseFloat(control.value); //价格 还未验证

    let priceOK: boolean = control.value && price >= minPrice && price <= maxPrice;


    //  输入正确  或 输入为空
    if ((patrnOK && priceOK) || !control.value) {
        return null;
    }

    if (!patrnOK) {
        return {'invalidInfo': '价格输入错误'};
    }

    if (!priceOK) {
        return {'invalidInfo': '价格需满足: ' + minPrice.toFixed(2) + ' ≤ 价格 ≤ ' + maxPrice.toFixed(2)};  //.toFixed(2) 转换为带2个小数的数字字符串
    }
}
