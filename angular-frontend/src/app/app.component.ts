import {Component, HostListener} from '@angular/core';
import {ViewportScroller} from '@angular/common';

@Component({
               selector   : 'app-root',
               templateUrl: './app.component.html',
               styleUrls  : ['./app.component.css'],
           })
export class AppComponent {
    pageYOffset = 0;

    @HostListener('window:scroll', ['$event']) onScroll(event) {
        this.pageYOffset = window.pageYOffset;
    }

    constructor(private scroll: ViewportScroller) {
    }

    scrollToTop() {
        this.scroll.scrollToPosition([0, 0]);
    }
}
