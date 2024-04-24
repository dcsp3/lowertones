import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-custom-toast',
  templateUrl: './custom-toast.component.html',
  styleUrls: ['./custom-toast.component.scss'],
})
export class CustomToastComponent {
  @Input() message: string = '';
  @Input() success: boolean = false;
  @Input() error: boolean = false;
}
