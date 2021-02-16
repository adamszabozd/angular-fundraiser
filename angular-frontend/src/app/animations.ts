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

export const slideRight = trigger('slideIn', [
    state('*', style({
                         transform: 'translateX(-600%)',
                     })),
    state('in', style({
                          transform: 'translateX(0%)',
                      })),
    state('out',   style({
                             transform: 'translateX(100%)',
                         })),
    transition('* => in', animate('800ms ease-in-out')),
    transition('in => out', animate('800ms ease-in-out'))
]);

export const slideInFromDown = trigger('slideUp', [
    state('*', style({
                         transform: 'translateY(100%)',
                     })),
    state('up', style({
                          transform: 'translateY(0)',
                      })),
    state('down',   style({
                             transform: 'translateY(-100%)',
                         })),
    transition('* => up', animate('800ms ease-in-out')),
    transition('in => down', animate('800ms ease-in-out'))
]);
