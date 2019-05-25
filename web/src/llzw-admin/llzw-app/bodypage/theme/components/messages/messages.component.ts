// import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
// import { MatMenuTrigger } from '@angular/material';
// import { MessagesService } from './messages.service';
//
// @Component({
//     selector: 'app-messages',
//     templateUrl: './messages.component.html',
//     styleUrls: ['./messages.component.scss'],
//     encapsulation: ViewEncapsulation.None,
//     providers: [MessagesService]
// })
// export class MessagesComponent implements OnInit {
//     @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger;
//     public selectedTab: number = 1;
//     public messages: Array<Object>;
//     public files: Array<Object>;
//     public meetings: Array<Object>;
//     constructor(private messagesService: MessagesService) {
//         this.messages = messagesService.getMessages();
//         this.files = messagesService.getFiles();
//         this.meetings = messagesService.getMeetings();
//     }
//
//     ngOnInit() {
//     }
//
//     openMessagesMenu() {
//         this.trigger.openMenu();
//         this.selectedTab = 0;
//     }
//
//     onMouseLeave() {
//         this.trigger.closeMenu();
//     }
//
//     stopClickPropagate(event: any) {
//         event.stopPropagation();
//         event.preventDefault();
//     }
//
// }
