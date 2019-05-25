import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SelectionModel } from '@angular/cdk/collections';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { first } from 'rxjs/operators';

import { ProductService } from '../../_services/product.service';
import { Product } from '../../_models/product.model';
import { UploadFile, UploadEvent, FileSystemFileEntry, FileSystemDirectoryEntry } from 'ngx-file-drop';
import { usernamelValidator, namelValidator, emailValidator, matchingPasswords, sha1_64_Validator, pricelValidator } from '../../../../../appsettings/utils/app-validators';

import { FileService } from '../../../../../services/services/file.service'; //专门写了个文件上传服务

import { apiUrl } from '../../../../../../llzw-environments/environment';


// 上传图片用的卡片。
class UpLoadImg {
  // 有远程优先用远程
  isEmptyNewImg = false;
  isFromRemote = true;
  localUrl; // 上传前用于本地预览
  url: string // 来自远程
  relativePath: string;
  hash: string;
  obj: Object;
  // 时间戳  同一张卡片有本地数据有远程数据，本地 数据 先于 远程数据存入卡片，中间有时间差
  // 时间戳 用于上传图片后再找到 用本地图片 的 卡片编号
  timestamp: number = 0;
}



//商品介绍
interface Details {
  brand: string;              // '品牌': '恒都';
  name: string;               // '商品名称': '恒都澳洲牛腱子1kg 整肉';
  number: string;             // '商品编号': '3043161';
  gross: string;              // '商品毛重': '1.03kg';
  saveStatus: string;         // '保存状态': '冷冻';
  netWeight: string;          // '重量': '501g-1kg';
  domesticImport: string;     //  '国产/进口': '进口';
  variety: string;            // '品种': '其他';
  feedingMethod: string;      // '饲养方式': '草饲';
  classification: string;     // '分类': '牛腱';
  package: string;            // '包装': '简装';
  torageConditions: string;   // '贮存条件': '深冷、冷冻 -18℃';
  shelfLife: string;          // '保质期': '365天';
  netContent: string;         // '净含量': '1000g';
  packingList: string;        // '包装清单': '澳洲牛腩块 冷冻肉 1kg×1';

  imgs: string[];              // 商品详情中的大图
}



//商品介绍
/*


interface Evaluation {
    customer: string;
    stars: number; // 1~5星
    evaluation: string;
    images: string[];
}

// 商品介绍类 存储时转为json文本
interface Introduction {
    ch: Characteristics;
    sp: SpecificationsAndPackaging;
    evs?: Evaluation[];
    imghashs?: string[];
}
*/

const BASE_URL = apiUrl + '/files';  // 中文

@Component({
  selector: 'kt-product-add',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.scss']
})
export class ProductAddComponent implements OnInit {


  files_url = BASE_URL; //必须是类属性， 才可以被 html 读取
  statusCode: number; // http 返回状态码
  product: Product;
  productForm: FormGroup; //主表单

  isFormInitFromGet = false; //是否正在初始化 用于临时使详情表单监听失效

  detailsForm: FormGroup; //详情表单
  updateImageCardNum: 0; // 用于上传的图潘编号
  //dragdockimgsrc = "assets/llzw/images/将图片拖入此处.png"
  //dragdockimgUrl;

  smallImgs: UpLoadImg[] = []; // TODO: 图像文件数组(url) 必须这样初始化，否则后面用不了

  bigImgs: UpLoadImg[] = []; // TODO: 图像文件数组(url) 必须这样初始化，否则后面用不了

  caImageFile = new UpLoadImg(); // ca 认证文件 上传显示用对象
  details: Details; //接口 商品详情，用于解析 introduct 字段


  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    public fileService: FileService,
  ) {
    // 必须在构造函数中初始化 this.details, 不然 html不识别 会报错
    this.details_init();
  }

  // 读取 product_list 传递过来的 操作方法
  // 三种可能    operateType 1: 编辑     2: 全新方式新增      3: 克隆方式新建
  // 新增 和 编辑的区别 还在 http提交方式不同，新增(POST) 修改(PUT)
  operateType: number;
  productId: number;

  ngOnInit() {
    // 读取 product_list 传递过来的 操作方法
    // 三种可能    operateType 1: 编辑     2: 全新方式新增      3: 克隆方式新建
    // 新增 和 编辑的区别 还在 http提交方式不同，新增(POST) 修改(PUT)
    this.operateType = +window.localStorage.getItem('operateType'); // 通过+转换为number
    this.productId = +window.localStorage.getItem('targetProductId'); // 通过+转换为number

    // 删除临时数据，防止页面刷新时不正确跳转
    window.localStorage.removeItem('operateType'); //删除 临时数据文件
    window.localStorage.removeItem('targetProductId'); //删除 临时数据文件

    console.log('operateType = ', this.operateType)
    console.log('productId = ', this.productId)

    //如果 既不是新建 也不是 编辑，则跳转到父页面
    if (!(this.operateType === 1 || this.operateType === 2 || this.operateType === 3))
      this.router.navigate(['..'], { relativeTo: this.activatedRoute });

    // 初始化 主表单
    this.productForm = this.formBuilder.group({
      id: [null],
      'name': [null, Validators.compose([Validators.required, Validators.minLength(4), namelValidator])],
      'valid': [true, Validators.compose([Validators.required])],
      'price': [999.99, Validators.compose([Validators.required, pricelValidator])],


      'introduction': [null, Validators.compose([Validators.required, Validators.minLength(1), Validators.maxLength(65535)])],


      'ca': [null, Validators.compose([Validators.required, namelValidator])],
      'caFile': [null, Validators.compose([Validators.required, sha1_64_Validator])],
      'caId': [null, Validators.compose([Validators.required, namelValidator])],

      'mainImageFiles': [[],],

      //新增表单项
      'category': [null,],
      'numberToSell': [null,],
      'numberSelled': [null,],
      'feature': [null,],

      'caIsOk': [false,],
      'caFileIsOk': [false,],
    });

    // 初始化 详情表单
    this.detailsForm = this.formBuilder.group({

      'brand': [null,],
      'name': [null,],
      'number': [null,],
      'gross': [null,],
      'saveStatus': [null,],
      'netWeight': [null,],
      'domesticImport': [null,],
      'variety': [null,],
      'feedingMethod': [null,],
      'classification': [null,],
      'package': [null,],
      'torageConditions': [null,],
      'shelfLife': [null,],
      'netContent': [null,],
      'packingList': [null,],

      'imgs': [[],],
    });




    // 主表单项监听 放在表单赋值前面 🔆🔆🔆🔆🔆🔆🔆🔆🔆🔆🔆
    this.productForm.get("caFile").valueChanges.subscribe(
      value => {
        this.productForm.controls['caFileIsOk'].setValue(value ? true : false);
        this.productForm.controls['caIsOk'].setValue(this.productForm.controls['caFile'].value && this.productForm.controls['ca'].value && this.productForm.controls['caId'].value);
      }
    )

    this.productForm.get("ca").valueChanges.subscribe(
      value => {
        this.productForm.controls['caIsOk'].setValue(this.productForm.controls['caFile'].value && this.productForm.controls['ca'].value && this.productForm.controls['caId'].value);
      }
    )

    this.productForm.get("caId").valueChanges.subscribe(
      value => {
        this.productForm.controls['caIsOk'].setValue(this.productForm.controls['caFile'].value && this.productForm.controls['ca'].value && this.productForm.controls['caId'].value);
      }
    )



    // 如果是 编辑 或 克隆方式新增  => 表单赋值
    if (this.operateType === 1 || this.operateType === 3) {
      this.productService.getById(this.productId)
        // this.productService.getById(2)
        .subscribe(data => {
          // 元气少女 因为此处需要编辑的控制器 少于 从 API get 到的 fields, 所以需要单独赋值
          // 不这么做 this.productForm.setValue(data.result);
          // 不这么做 this.productForm.setValue(data);
          // 不取id数据
          if (this.operateType === 1) {
            // 如果是编辑状态，需要取得id, 不然没法 update; 新增 不赋值id, 由服务器自动生成 id
            this.productForm.controls['id'].setValue(data['id']);
          }

          this.productForm.controls['valid'].setValue(data['valid']);
          this.productForm.controls['name'].setValue(data['name']);
          // this.productForm.controls['seller'].setValue(data['seller']);
          this.productForm.controls['introduction'].setValue(data['introduction']);
          this.productForm.controls['price'].setValue(data['price']);
          this.productForm.controls['ca'].setValue(data['ca']);
          this.productForm.controls['caFile'].setValue(data['caFile']);
          this.productForm.controls['caId'].setValue(data['caId']);

          this.productForm.controls['category'].setValue(data['category']);
          this.productForm.controls['feature'].setValue(data['feature']);

          // 取 ca 图像文件
          if (data['caFile']) {
            this.caImageFile.hash = data['caFile']; // TODO: 奇怪 竟然不需要.value
            this.caImageFile.url = BASE_URL + '/' + data['caFile']; // ca 图片url
          }


          // 取小图像文件， 小图像不用表单项控制器管理
          if (data['mainImageFiles']) {

            data['mainImageFiles'].forEach(
              (hash) => {
                let temp = new UpLoadImg();
                temp.hash = hash;
                temp.url = BASE_URL + '/' + hash; //图片url
                this.smallImgs.push(temp);  // 尾部插入
              }
            )
          }

          // 取商品详情数据 反序列化 😂😂😂😂😂😂😂
          try {
            // alert(data['introduction']);
            let obj_details: Details = JSON.parse(data['introduction']);
            // console.log("取商品详情数据 反序列化后 = ", obj_details);
            this.details = obj_details;


            // 将商品详情 填入表单
            this.isFormInitFromGet = true; // 监听功能失效 this.detailsForm.valueChanges.subscribe
            this.details_to_form();
            this.isFormInitFromGet = false; // 监听功能生效 this.detailsForm.valueChanges.subscribe
            // 商品详情表单填写结束



            // 取商品详情图像文件， 图像不用表单项控制器管理
            if (this.details.imgs) {

              this.details.imgs.forEach(
                (hash) => {
                  let temp = new UpLoadImg();
                  temp.hash = hash;
                  temp.url = BASE_URL + '/' + hash; //图片url
                  this.bigImgs.push(temp);  // 尾部插入
                }
              )
            }
          } catch (e) {
            // document.writeln("Caught: " + e.message); // 这行语句会生成个错误提示页面，不是咱想用的
            console.log(' Caught: ', e.message);
          }
          // console.log('this.details = ', this.details);
        });
    }



    // detailsForm 监听 好像这个放在哪儿都会生效 🔆🔆🔆🔆🔆🔆🔆🔆🔆🔆🔆
    this.detailsForm.valueChanges.subscribe(data => {
      // XXXXX this.details XXX= data; //这里不能这么写
      if (this.isFormInitFromGet === false) //采取办法让监听受控
        this.details_from_form(); //写入数组
    })


    let temp1 = new UpLoadImg();
    temp1.isEmptyNewImg = true;
    this.smallImgs.push(temp1); // 末尾插入 新的数组成员(用于新插入卡片占位)

    let temp2 = new UpLoadImg();
    temp2.isEmptyNewImg = true;
    this.bigImgs.push(temp2); // 末尾插入 新的数组成员(用于新插入卡片占位)

  }

  details_init() {
    let temp: Details = {
      brand: '',
      name: '',
      number: '',
      gross: '',
      saveStatus: '',
      netWeight: '',
      domesticImport: '',
      variety: '',
      feedingMethod: '',
      classification: '',
      package: '',
      torageConditions: '',
      shelfLife: '',
      netContent: '',
      packingList: '',

      imgs: [], //图像文件初始为空
    }
    this.details = temp;
  }

  detailsForm_demo() {
    // 自动填写 参考 数据
    this.detailsForm.controls['brand'].setValue('恒都');
    this.detailsForm.controls['name'].setValue('恒都澳洲牛腱子1kg 整肉');
    this.detailsForm.controls['number'].setValue('3043161');
    this.detailsForm.controls['gross'].setValue('1.03kg');
    this.detailsForm.controls['saveStatus'].setValue('冷冻');
    this.detailsForm.controls['netWeight'].setValue('501g-1kg');
    this.detailsForm.controls['domesticImport'].setValue('进口');
    this.detailsForm.controls['variety'].setValue('其他');
    this.detailsForm.controls['feedingMethod'].setValue('草饲');
    this.detailsForm.controls['classification'].setValue('牛腱');
    this.detailsForm.controls['package'].setValue('简装');
    this.detailsForm.controls['torageConditions'].setValue('深冷、冷冻 -18℃');
    this.detailsForm.controls['shelfLife'].setValue('365天');
    this.detailsForm.controls['netContent'].setValue('1000g');
    this.detailsForm.controls['packingList'].setValue('澳洲牛腩块 冷冻肉 1kg×1');
  }

  details_to_form() {
    // 将商品详情 填入表单

    // 将 json 中所有属性 复制到 details
    for (let key in this.details) {
      // this.details[key] = json[key];
      this.detailsForm.controls[key].setValue(this.details[key]);
    }

    // this.detailsForm.controls['brand'].setValue(this.details['brand']);
    // this.detailsForm.controls['name'].setValue(this.details['name']);
    // this.detailsForm.controls['number'].setValue(this.details['number']);
    // this.detailsForm.controls['gross'].setValue(this.details['gross']);
    // this.detailsForm.controls['saveStatus'].setValue(this.details['saveStatus']);
    // this.detailsForm.controls['netWeight'].setValue(this.details['netWeight']);
    // this.detailsForm.controls['domesticImport'].setValue(this.details['domesticImport']);
    // this.detailsForm.controls['variety'].setValue(this.details['variety']);
    // this.detailsForm.controls['feedingMethod'].setValue(this.details['feedingMethod']);
    // this.detailsForm.controls['classification'].setValue(this.details['classification']);
    // this.detailsForm.controls['package'].setValue(this.details['package']);
    // this.detailsForm.controls['torageConditions'].setValue(this.details['torageConditions']);
    // this.detailsForm.controls['shelfLife'].setValue(this.details['shelfLife']);
    // this.detailsForm.controls['netContent'].setValue(this.details['netContent']);
    // this.detailsForm.controls['packingList'].setValue(this.details['packingList']);
    // 商品详情表单填写结束
  }

  details_from_form() {
    // 将商品详情 从表单读取 写入对象details

    // 将 json 中所有属性 复制到 details
    for (let key in this.detailsForm.controls) {
      this.details[key] = this.detailsForm.controls[key].value;
    }
    //console.log('遍历 this.details detailsForm.controls = ', this.details);

    // this.details['brand'] = this.detailsForm.controls['brand'].value;
    // this.details['name'] = this.detailsForm.controls['name'].value;
    // this.details['number'] = this.detailsForm.controls['number'].value;
    // this.details['gross'] = this.detailsForm.controls['gross'].value;
    // this.details['saveStatus'] = this.detailsForm.controls['saveStatus'].value;
    // this.details['netWeight'] = this.detailsForm.controls['netWeight'].value;
    // this.details['domesticImport'] = this.detailsForm.controls['domesticImport'].value;
    // this.details['variety'] = this.detailsForm.controls['variety'].value;
    // this.details['feedingMethod'] = this.detailsForm.controls['feedingMethod'].value;
    // this.details['classification'] = this.detailsForm.controls['classification'].value;
    // this.details['package'] = this.detailsForm.controls['package'].value;
    // this.details['torageConditions'] = this.detailsForm.controls['torageConditions'].value;
    // this.details['shelfLife'] = this.detailsForm.controls['shelfLife'].value;
    // this.details['netContent'] = this.detailsForm.controls['netContent'].value;
    // this.details['packingList'] = this.detailsForm.controls['packingList'].value;


  }


  //组件及子组件每次检查视图时调用
  ngAfterViewChecked() {
    // this.drag_update();
  }

  onSubmit() {

    // 从 smallImgs 图像文件数组 生成 用于上传的 product.mainImageFiles (数组)
    let smallImgFileUrls: string[] = [];

    for (let i = 0; i < this.smallImgs.length; i++) {
      if (this.smallImgs[i].hash)
        smallImgFileUrls.push(this.smallImgs[i].hash);
    }

    // 如图像文件数组不为空，赋值给表单项
    if (smallImgFileUrls.length)
      this.productForm.controls['mainImageFiles'].setValue(smallImgFileUrls);

    // 从detailsForm 取值 赋值给 商品详情对象


    // 从 bigImgs 图像文件数组 生成 用于上传的 product.mainImageFiles (数组)
    let bigImgFileUrls: string[] = [];

    for (let i = 0; i < this.bigImgs.length; i++) {
      if (this.bigImgs[i].hash)
        bigImgFileUrls.push(this.bigImgs[i].hash);
    }

    // console.log('从 bigImgs 图像文件数组 生成 用于上传的 product.mainImageFiles (数组)', bigImgFileUrls);
    // 如图像文件数组不为空，赋值给 商品详情对象
    if (bigImgFileUrls.length)
      this.details.imgs = bigImgFileUrls;
    console.log('从 bigImgs 图像文件数组 生成  this.details.imgs = ', this.details.imgs);

    // 开了监听 就没必要下面这行了
    //this.details_from_form();
    // console.log('this.details = ', this.details);

    let str_updateIntroduction = JSON.stringify(this.details);    // 商品详情 JSON序列化为字符串
    // alert(str_updateIntroduction);

    this.productForm.controls['introduction'].setValue(str_updateIntroduction); // 将商品详情存入表单
    if (this.productForm.controls.introduction.errors) {
      alert('商品详情不能为空！');
      return; // 商品详情不能为空
    }

    // 验证不通过 不上传
    // if (this.productForm.invalid) {
    //     console.log('表单验证失败，请修改');
    //     return;
    // }
    // 初始化 表单

    console.log("this.productForm.controls['valid'].value = ", this.productForm.controls['valid'].value)
    // 上架商品
    if (this.operateType === 1) { // 编辑状态
      this.productService.update(this.productForm.value)
        .pipe(first())
        .subscribe(
          success => {
            this.statusCode = success.status;
            // alert('Product updated successfully.');
            console.log('successCode:', this.statusCode);
            this.router.navigate(['..'], { relativeTo: this.activatedRoute });

          },
          error => {
            this.statusCode = error.status;
            alert('errorCode' + this.statusCode);
          });

    } else { // 新增状态
      this.productService.create(this.productForm.value)
        .subscribe(data => {
          this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        });
    }
  }

  // 在当前空图片位置左侧插入真正的图片
  insertImage(newImage: UpLoadImg, imgs: UpLoadImg[]) {
    // emptyNum 数组中旧空图片位置(插入前)
    let emptyNum = imgs.findIndex(element => element.isEmptyNewImg === true);  //find()方法返回成员本身 findIdex()返回下标
    //插入真正图片 到 空图片 前面
    if (newImage)
      imgs.splice(emptyNum, 0, newImage); //这个函数用于删除和插入 第1参数:修改位置   第2参数:删除数量 第3参数,4..插入的元素
  }

  // 左侧插入图片(新空图片) 先删后插
  leftInsert(id, imgs: UpLoadImg[]) {

    console.log("左侧插入操作, 位置=", id);
    // emptyNum 数组中旧空图片位置(插入前)
    let emptyNum = imgs.findIndex(element => element.isEmptyNewImg === true);  //find()方法返回成员本身 findIdex()返回下标
    // 分三种情况 1:旧空图片在插入位置前 2:同位置 3:旧空图片在插入位置后
    // 同位置 不处理
    if (id === emptyNum) return;

    let insertId = 0
    // 旧空图片在插入位置前
    if (id > emptyNum) insertId = id - 1;
    // 旧空图片在插入位置后 不需要预处理
    if (id < emptyNum) insertId = id;

    //删除数组中的旧空图片
    imgs.splice(emptyNum, 1); //这个函数用于删除和插入 第1参数:修改位置   第2参数:删除数量

    //插入空图片到insertId
    let upLoadImg = new UpLoadImg();
    upLoadImg.isEmptyNewImg = true;
    imgs.splice(insertId, 0, upLoadImg); //这个函数用于删除和插入 第1参数:修改位置   第2参数:删除数量 第3参数,4..插入的元素

    console.log("左侧插入操作, 位置=", id);
  }

  // 左侧移动图片
  leftMove(id, imgs: UpLoadImg[]) {
    //已经是开头了，直接退出
    if (id === 0)
      return;
    //与前一个交换
    let temp = imgs[id - 1];
    imgs[id - 1] = imgs[id];
    imgs[id] = temp;
  }

  // 删除图片
  delete(id, imgs: UpLoadImg[]) {

    //如果当前要删除图片在尾部 且 为空图片, 不操作， 退出处理, 避免一次务必要的页面渲染
    if (id === imgs.length - 1 && imgs[id].isEmptyNewImg === true)
      return;

    if (imgs[id])
      //当前位置为不是空图片，删除
      imgs.splice(id, 1); //这个函数用于删除和插入 第1参数:修改位置   第2参数:删除数量
    else {
      imgs.splice(id, 1); //删除当前空图片

      let upLoadImg = new UpLoadImg();
      upLoadImg.isEmptyNewImg = true;
      imgs.push(upLoadImg); //尾部增加空图片
    }

  }


  // 右侧移动图片
  rightMove(id, imgs: UpLoadImg[]) {
    // 已经是末尾了，直接退出
    if (id === imgs.length - 1)
      return;
    // 与后一个交换
    let temp = imgs[id + 1];
    imgs[id + 1] = imgs[id];
    imgs[id] = temp;
  }

  // 右侧侧插入图片
  rightInsert(id, imgs: UpLoadImg[]) {
    this.leftInsert(id + 1, imgs);
  }


  public files: UploadFile[] = [];

  public imgs_dropped(event: UploadEvent, imgs: UpLoadImg[]) {
    this.files = event.files;

    let main_timestamp = new Date().getTime(); // 取当前时间戳 客户机

    // 清除 卡片所有时间戳 (用于防止用户在操作过程中，自己更改电脑时间，而时间戳引起冲突)
    for (let i = 0; i < imgs.length; i++) {
      imgs[i].timestamp = 0;
    }


    for (const droppedFile of event.files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile && this.isFileAllowed(droppedFile.fileEntry.name)) {

        main_timestamp++; // 总时间戳递增 每个增加的卡片时间戳不一样
        let current_timestamp = main_timestamp; // 将总时间戳 赋值 给当前卡片用时间戳

        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;

        const reader = new FileReader();
        fileEntry.file((file: File) => {

          // Here you can access the real file
          console.log(droppedFile.relativePath, file);
          console.log("用于上传新图片 = ", droppedFile.relativePath);

          let newImage = new UpLoadImg();

          reader.readAsDataURL(file);//发起异步请求
          reader.onload = () => {
            //读取完成后，将结果赋值给img的src
            newImage.localUrl = reader.result;
          }
          newImage.obj = file;
          newImage.relativePath = droppedFile.relativePath;

          // 在当前空位插入图片
          newImage.timestamp = current_timestamp; // 写入时间戳
          this.insertImage(newImage, imgs); //小图数组 插入

          // new 一个表单，用于文件上传
          const formData = new FormData()
          // 上传表单项 key=>file 值 file droppedFile.relativePath
          formData.append('file', file, droppedFile.relativePath);
          this.fileService.createFile(formData)
            .subscribe(data => {
              console.log(data['hash']); //http://localhost:8981/api/v1/files/f120379e3db1ab0af7be7b8597e16d4cf21f5e75de0b008120ea680f2129160e

              // 根据时间戳，找回上次新插入的卡片
              let findCardNum = imgs.findIndex(element => element.timestamp === current_timestamp);
              // 在卡片中写入 远程url
              imgs[findCardNum].url = BASE_URL + '/' + data['hash'];
              // 在卡片中写入 远程hash
              imgs[findCardNum].hash = data['hash'];
            });
        });
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  // 开发模式?
  isDevMode() {
    return true;
  }

  // 验证文件类型(这里只验证扩展名)
  isFileAllowed(fileName: string) {
    let isFileAllowed = false;
    // const allowedFiles = ['.doc', '.docx', '.ppt', '.pptx', '.pdf']; //常用文档格式
    const allowedFiles = ['.png', '.jpg', '.gif']; // 常用图片格式
    const regex = /(?:\.([^.]+))?$/;
    const extension = regex.exec(fileName);
    if (this.isDevMode()) {
      console.log('extension du fichier : ', extension);
    }
    if (undefined !== extension && null !== extension) {
      for (const ext of allowedFiles) {
        if (ext === extension[0]) {
          isFileAllowed = true;
        }
      }
    }
    return isFileAllowed;
  }




  public caFlieDropped(event: UploadEvent) {
    this.files = event.files;

    for (const droppedFile of event.files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile && this.isFileAllowed(droppedFile.fileEntry.name)) {

        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;

        const reader = new FileReader();
        fileEntry.file((file: File) => {

          // Here you can access the real file
          console.log(droppedFile.relativePath, file);
          console.log("用于上传ca新图片 = ", droppedFile.relativePath);

          reader.readAsDataURL(file);//发起异步请求
          reader.onload = () => {
            //读取完成后，将结果赋值给img的src
            this.caImageFile.localUrl = reader.result;
          }
          this.caImageFile.obj = file;
          this.caImageFile.relativePath = droppedFile.relativePath;

          // new 一个表单，用于文件上传
          const formData = new FormData()
          // 上传表单项 key=>file 值 file droppedFile.relativePath
          formData.append('file', file, droppedFile.relativePath);
          this.fileService.createFile(formData)
            .subscribe(data => {
              // console.log(data['hash']); //http://localhost:8981/api/v1/files/f120379e3db1ab0af7be7b8597e16d4cf21f5e75de0b008120ea680f2129160e

              // 在ca卡片中写入 远程url
              this.caImageFile.url = BASE_URL + '/' + data['hash'];
              // 在ca卡片中写入 远程hash
              this.caImageFile.hash = data['hash'];
              // 在 productForm 表单中 填入 hash
              this.productForm.controls['caFile'].setValue(data['hash']);
              console.log("this.productForm.controls['caFile']=", data['hash']);
            });
        });
        return; // 单张图片
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }


    }
  }

  //全自动提交预设好的文件夹(图片： 含小图片，大图片，ca图片;    json： ca信息，商品信息，商品详情)
  public autoDropped(event: UploadEvent) {
    this.files = event.files;

    let main_timestamp = new Date().getTime(); // 取当前时间戳 客户机

    //清除 卡片所有时间戳 (用于防止用户在操作过程中，自己更改电脑时间，而时间戳引起冲突)
    for (let i = 0; i < this.smallImgs.length; i++) {
      this.smallImgs[i].timestamp = 0;
    }
    for (let i = 0; i < this.bigImgs.length; i++) {
      this.bigImgs[i].timestamp = 0;
    }


    for (const droppedFile of event.files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile && this.isFileAllowedAuto(droppedFile.fileEntry.name)) {

        main_timestamp++; // 总时间戳递增 每个增加的卡片时间戳不一样
        let current_timestamp = main_timestamp; // 将总时间戳 赋值 给当前卡片用时间戳

        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;

        const reader = new FileReader();
        fileEntry.file((file: File) => {

          // console.log("product_info.json ？= ", droppedFile.fileEntry.name);

          //如果文件为 .json
          if (droppedFile.fileEntry.name.endsWith('.json')) {

            switch (droppedFile.fileEntry.name) {
              case 'product_info.json':

                reader.readAsText(file);
                reader.onload = () => {
                  // 异步函数
                  // 当读取完成之后会回调这个函数，然后此时文件的内容存储到了result中。直接操作即可。
                  // 读取完成后，将结果赋值给img的src
                  // 然后你分配字符串| ArrayBuffer类型(对于reader.result)的字符串类型,您刚刚分配.你不能.您只能将字符串分配给字符串.
                  // 因此,如果您100％确定reader.result包含字符串,您可以断言：
                  let txt: string = reader.result as string; // 必须加断言
                  let json = JSON.parse(txt);

                  this.productForm.controls['name'].setValue(json['name']);
                  this.productForm.controls['price'].setValue(json['price']);
                  this.productForm.controls['category'].setValue(json['variety']);
                  this.productForm.controls['numberToSell'].setValue(json['numberToSell']);
                  this.productForm.controls['numberSelled'].setValue(json['numberSelled']);
                  this.productForm.controls['feature'].setValue(json['chr']);
                  // 也可以用循环写
                };
                break;

              case 'product_ca.json':
                reader.readAsText(file);
                reader.onload = () => {
                  // 异步函数
                  let txt: string = reader.result as string; // 必须加断言
                  let json = JSON.parse(txt);

                  this.productForm.controls['ca'].setValue(json['ca']);
                  this.productForm.controls['caId'].setValue(json['caId']);
                };
                break;
              case 'product_detail.json':
                reader.readAsText(file);
                reader.onload = () => {
                  // 异步函数
                  let txt: string = reader.result as string; // 必须加断言
                  let json = JSON.parse(txt);

                  // 将 json 中所有属性 复制到 details
                  for (let key in json) {
                    // this.details[key] = json[key];
                    this.detailsForm.controls[key].setValue(json[key]);
                  }

                  // 这个函数用于 复制 目标中存在的属性，不存在的不复制
                  // function copy(source, dest) {
                  //     for (let key in dest) {
                  //         if (source.hasOwnProperty(key)) {
                  //             dest[key] = source[key];
                  //         }
                  //     }
                  // }

                };

              default:
                break;
            }
            return; // josn文件处理完成 当前是在 箭头函数内部，所以用 return
          }

          // 剩下的情况是 file 为 图片
          let isCaImg = false;
          let imgs: UpLoadImg[];
          if
            (droppedFile.fileEntry.name.startsWith('ca')) {
            isCaImg = true; //如果图片是 ca图片
          } else     //如果图片文件名 开头为a， 则图片为小图，否则为大图
            if (droppedFile.fileEntry.name.startsWith('a')) {
              imgs = this.smallImgs;
            }
            else {
              imgs = this.bigImgs;
            }

          // Here you can access the real file
          console.log(droppedFile.relativePath, file);
          console.log("用于上传新图片 = ", droppedFile.relativePath);

          let newImage = new UpLoadImg();

          reader.readAsDataURL(file);//发起异步请求
          reader.onload = () => {
            //读取完成后，将结果赋值给img的src
            newImage.localUrl = reader.result;
          }
          newImage.obj = file;
          newImage.relativePath = droppedFile.relativePath;

          if (!isCaImg) {
            // 在当前空位插入图片
            newImage.timestamp = current_timestamp; // 写入时间戳
            this.insertImage(newImage, imgs); //小图数组 插入
          }
          else {
            this.caImageFile = newImage;
          }

          // new 一个表单，用于文件上传
          const formData = new FormData()
          // 上传表单项 key=>file 值 file droppedFile.relativePath
          formData.append('file', file, droppedFile.relativePath);
          this.fileService.createFile(formData)
            .subscribe(data => {
              console.log(data['hash']); //http://localhost:8981/api/v1/files/f120379e3db1ab0af7be7b8597e16d4cf21f5e75de0b008120ea680f2129160e

              if (!isCaImg) {
                // 根据时间戳，找回上次新插入的卡片
                let findCardNum = imgs.findIndex(element => element.timestamp === current_timestamp);
                // 在卡片中写入 远程url
                imgs[findCardNum].url = BASE_URL + '/' + data['hash'];
                // 在卡片中写入 远程hash
                imgs[findCardNum].hash = data['hash'];
              } else {
                // 在ca卡片中写入 远程url
                this.caImageFile.url = BASE_URL + '/' + data['hash'];
                // 在ca卡片中写入 远程hash
                this.caImageFile.hash = data['hash'];
                // 在 productForm 表单中 填入 hash
                this.productForm.controls['caFile'].setValue(data['hash']);
                console.log("this.productForm.controls['caFile']=", data['hash']);
              }
            });
        });
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }



  //验证文件类型(这里只验证扩展名)
  isFileAllowedAuto(fileName: string) {
    let isFileAllowed = false;
    //const allowedFiles = ['.doc', '.docx', '.ppt', '.pptx', '.pdf']; //常用文档格式
    const allowedFiles = ['.png', '.jpg', '.gif', '.json']; // 常用图片格式
    const regex = /(?:\.([^.]+))?$/;
    const extension = regex.exec(fileName);
    if (this.isDevMode()) {
      console.log('extension du fichier : ', extension);
    }
    if (undefined !== extension && null !== extension) {
      for (const ext of allowedFiles) {
        if (ext === extension[0]) {
          isFileAllowed = true;
        }
      }
    }
    return isFileAllowed;
  }













  /*
  // 拖拽代码
  drag_update() {

      let box1, message;

      box1 = document.getElementById("box9834");

      message = document.getElementById("target");


      box1.ondragenter = function () {

          // 拖拽目标进入时变色0
          this.style.borderColor = 'red';

      }

      box1.ondragleave = function () {


          this.style.borderColor = '#666';

      }

      box1.ondragover = function (e) {
          e.preventDefault();
      }
      box1.ondrop = function (e) {
          // 放置图片时

          e.preventDefault();

          console.log( "上传的编号 = ",  this.updateImageNum);

          var file = e.dataTransfer.files[0];
          var nowfile = new FileReader();

          nowfile.readAsDataURL(file);
          console.log(e);
          nowfile.onload = function (e) {


              //转义字符
              box1.innerHTML = "<img src=\"" + nowfile.result + "\"/>";
          }

      }

  }
  */

}
