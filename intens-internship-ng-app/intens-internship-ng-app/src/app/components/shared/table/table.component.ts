import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent {

  @Input() dataSource = [];
  @Input() columnsToDisplay = [];
  @Input() columnsToIterate = [];
  @Output() Click = new EventEmitter<number>();
  @Output() DoubleClick = new EventEmitter<number>();
  @Output() Delete = new EventEmitter<number>();

  constructor() { }

  clicked(id): void {
    this.Click.emit(id);
  }

  doubleClicked(id): void {
    this.DoubleClick.emit(id);
  }

  delete(id): void {
    this.Delete.emit(id);
  }
}
