import {animate, state, style, transition, trigger} from '@angular/animations';

export const formAppearAnimation = trigger('formAppear', [
    state('invisible', style({
                                 opacity: '0',
                             })),
    state('visible', style({
                               opacity: '1',
                           })),
    transition('invisible => visible', animate('1100ms')),
]);
