import { trigger, state, style, transition, animate } from '@angular/animations';

export const suckInAnimation = trigger('suckInAnimation', [
  state('default', style({ transform: 'translateX(0)' })),
  state('sucked', style({ transform: 'translateX(100%)' })),
  transition('default => sucked', animate('800ms ease-in-out')),
]);

export const fadeInOut = trigger('fadeInOut', [
  transition(':enter', [style({ opacity: 0 }), animate('300ms', style({ opacity: 1 }))]),
  transition(':leave', [animate('300ms', style({ opacity: 0 }))]),
]);
