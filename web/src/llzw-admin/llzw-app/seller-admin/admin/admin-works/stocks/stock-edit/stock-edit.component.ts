// import { Component, OnInit } from '@angular/core';
// import { ActivatedRoute, Router } from '@angular/router';
// import { SelectionModel } from '@angular/cdk/collections';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { first } from 'rxjs/operators';
//
// import { StockService } from '../../_services/stock.service';
// import { Stock } from '../../_models/stock.model';
//
//
// @Component({
//   selector: 'kt-stock-edit',
//   templateUrl: './stock-edit.component.html',
//   styleUrls: ['./stock-edit.component.scss']
// })
// export class StockEditComponent implements OnInit {
//
//   statusCode: number; // http 返回状态码
//   stock: Stock;
//   stockForm: FormGroup;
//
//   constructor(
//     private formBuilder: FormBuilder,
//     private router: Router,
//     private activatedRoute: ActivatedRoute,
//     private stockService: StockService
//   ) { }
//
//
//
//   ngOnInit() {
//     let stockId = window.localStorage.getItem('editStockId');
//     if (!stockId) {
//       alert('Invalid action.');
//       this.router.navigate(['..'], { relativeTo: this.activatedRoute });
//       return;
//     }
//
//     /*
//     orderId	    Integer	    True    Parent Order ID
//     subject	    String	    True    ubject of this stock
//     totalAmount	Float	    True    Total Amount
//     description	String	    False	Description
//     */
//
//     this.stockForm = this.formBuilder.group({
//       id: [null, Validators.required],
//       orderId: ['', Validators.required],
//       subject: ['', Validators.required],
//       totalAmount: ['', Validators.required],
//       description: [''],
//     });
//     this.stockService.getById(+stockId)
//       // this.stockService.getById(2)
//       .subscribe(data => {
//         // 元气少女 因为此处需要编辑的控制器 少于 从 API get 到的 fields, 所以需要单独赋值
//         // this.stockForm.setValue(data.result);
//         // this.stockForm.setValue(data);
//         this.stockForm.controls['id'].setValue(data['id']);
//         this.stockForm.controls['orderId'].setValue(data['orderId']);
//         this.stockForm.controls['subject'].setValue(data['subject']);
//         this.stockForm.controls['totalAmount'].setValue(data['totalAmount']);
//         this.stockForm.controls['description'].setValue(data['description']);
//       });
//   }
//
//   /*
//
//             .subscribe(
//             successCode => {
//        this.statusCode = successCode;
//        this.getAllArticles();
//        this.backToCreateArticle();
//   },
//   errorCode => this.statusCode = errorCode);
//   */
//
//   onSubmit() {
//
//     //验证不通过 不上传
//     if (this.stockForm.invalid) {
//       return;
//     }
//
//     this.stockService.update(this.stockForm.value)
//       .pipe(first())
//       .subscribe(
//         success => {
//           this.statusCode = success.status;
//           alert('Stock updated successfully.');
//           console.log('successCode:', this.statusCode);
//           this.router.navigate(['..'], { relativeTo: this.activatedRoute });
//
//         },
//         error => {
//           this.statusCode = error.status;
//           alert('errorCode' + this.statusCode);
//         });
//   }
//
// }
