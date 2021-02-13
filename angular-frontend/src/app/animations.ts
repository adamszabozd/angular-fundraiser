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

export const slideInFromRight = trigger('slideIn', [
    state('*', style({
                         transform: 'translateX(100%)',
                     })),
    state('in', style({
                          transform: 'translateX(0)',
                      })),
    state('out',   style({
                             transform: 'translateX(-100%)',
                         })),
    transition('* => in', animate('600ms ease-in')),
    transition('in => out', animate('600ms ease-in'))
]);
