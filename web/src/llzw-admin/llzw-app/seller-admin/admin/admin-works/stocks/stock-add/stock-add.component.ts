import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

import {StockService} from '../../_services/stock.service';
import {Stock} from '../../_models/stock.model';
import {DatePipe} from '@angular/common';

/*

//const months = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
const months = ['JAN', 'FEB', 'MAR', 'APR', '五月', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
export class AppDateAdapter extends NativeDateAdapter {

  format(date: Date, displayFormat: Object): string {

    if (displayFormat === 'input') {
      const day = date.getDate();
      const month = date.getMonth();
      const year = date.getFullYear();
      //return `${months[month]} ${year}`;
      //let dateStr: string = this.datePipe.transform(val, 'yyyy-MM-dd')
      return `${year}-${month}-${day}`;
    }

    return date.toDateString();
  }
}
export const APP_DATE_FORMATS =
{
  parse: {
    dateInput: { month: 'short', year: 'numeric', day: 'numeric' },
  },
  display: {
    dateInput: 'input',
    monthYearLabel: { year: 'numeric', month: 'numeric' },
    dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric', month: 'long' },
  }
};


*/

@Component({
    selector: 'kt-stock-add',
    templateUrl: './stock-add.component.html',
    styleUrls: ['./stock-add.component.scss'],
    providers: [DatePipe,
        // {
        //   provide: DateAdapter, useClass: AppDateAdapter
        // },
        // {
        //   provide: MAT_DATE_FORMATS, useValue: APP_DATE_FORMATS
        // }
    ]
})
export class StockAddComponent implements OnInit {

    statusCode: number; // http 返回状态码
    stock: Stock;
    stockForm: FormGroup;
    productId: number = 0;
    editStockId: number = 0;
    addStock: number = 0;
    isAddFromProductsList = false; // 是否从ProductsList新建 用于html中表单项productId锁定

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private stockService: StockService,
        private datePipe: DatePipe,
    ) {
    }

    get fullDate() {
        const dateValue = this.stockForm.controls['producedAt'].value;
        if (!dateValue) {
            return '';
        }
        //return moment(dateValue).format('MM/DD/YYYY');
        let dateStr: string = this.datePipe.transform(dateValue, 'yyyy-MM-dd')
        return dateStr;


    }

    ngOnInit() {

        // 读取 product_list 传递过来的 操作方法
        this.addStock = +window.localStorage.getItem('addStock'); // 通过+转换为number
        // 删除临时数据，防止页面刷新时不正确跳转
        window.localStorage.removeItem('addStock'); //删除 临时数据文件

        // 读取 product_list 传递过来的 操作方法
        this.productId = +window.localStorage.getItem('targetProductId'); // 通过+转换为number
        // 删除临时数据，防止页面刷新时不正确跳转
        window.localStorage.removeItem('targetProductId'); //删除 临时数据文件

        // 读取 product_list 传递过来的 操作方法
        this.editStockId = +window.localStorage.getItem('editStockId'); // 通过+转换为number
        // 删除临时数据，防止页面刷新时不正确跳转
        window.localStorage.removeItem('editStockId'); //删除 临时数据文件


        //如果 既不是新建 也不是 编辑，则跳转到父页面
        if (!this.productId && !this.editStockId && !this.addStock)
            this.router.navigate(['..'], {relativeTo: this.activatedRoute});


        let myDate = new Date();
        this.stockForm = this.formBuilder.group({
            id: [null,],
            product: ['', Validators.required],
            productId: ['',],
            //producedAt: [myDate.toLocaleDateString(),], //获取当前日期, Validators.required],
            producedAt: [myDate,], //获取当前日期, Validators.required],
            shelfLife: ['365', Validators.required],
            totalQuantity: ['1', Validators.required],

            trackingId: ['SF-0322A-W982R-EAS-A9822145',],
            carrierName: ['SF-Express',],

        });

        // Mysql可以使用AUTO_INCREMENT来设定主键的值为自增长的，其默认值是1, 可以据此判断 非0，就是有效的id
        if (this.productId) {
            this.stockForm.controls['product'].setValue(this.productId);
            this.isAddFromProductsList = true; // html中表单项productId锁定
        }

        // 如果是编辑状态
        if (!this.productId && this.editStockId) {

            // 根据 id 取值，并赋值给 表单
            this.stockService.getById(this.editStockId)
            // this.productService.getById(2)
                .subscribe(data => {
                    // 元气少女 因为此处需要编辑的控制器 少于 从 API get 到的 fields, 所以需要单独赋值
                    // 不这么做 this.productForm.setValue(data.result);
                    // 不这么做 this.productForm.setValue(data);
                    // 不取id数据
                    //如果是编辑状态，需要取得id, 不然没法 update; 新增 不赋值id, 由服务器自动生成 id
                    this.stockForm.controls['id'].setValue(data['id']);

                    this.stockForm.controls['product'].setValue(data['product']);
                    this.stockForm.controls['producedAt'].setValue(data['producedAt']);
                    this.stockForm.controls['shelfLife'].setValue(data['shelfLife']);
                    this.stockForm.controls['totalQuantity'].setValue(data['totalQuantity']);
                    this.stockForm.controls['trackingId'].setValue(data['trackingId']);
                    this.stockForm.controls['carrierName'].setValue(data['carrierName']);
                })

        }
    }

    onDataChange(val) {
        console.log('onDataChange(val)  val=', val)
        //let dateStr: string = this.datePipe.transform(val, 'yyyy-MM-dd HH:mm:ss')
        let dateStr: string = this.datePipe.transform(val, 'yyyy-MM-dd')
        console.log('dateStr=', dateStr)
        //this.stockForm.controls['producedAt'].setValue(dateStr);

    }

    onSubmit() {

        //验证不通过 不上传
        if (this.stockForm.invalid) {
            return;
        }

        this.stockForm.controls['productId'].setValue(this.stockForm.controls['product'].value);
        console.log("this.datePipe.transform(this.stockForm.controls['producedAt'].value=", this.stockForm.controls['producedAt'].value);
        //this.stockForm.controls['producedAt'].setValue( this.datePipe.transform(this.stockForm.controls['producedAt'].value, 'yyyy-MM-dd'));

        this.stockService.create(this.stockForm.value)
            .subscribe(data => {
                this.router.navigate(['..'], {relativeTo: this.activatedRoute});
            });
    }

}
