import {Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, OnChanges {

  @Input() dataSource = [];
  @Input() columnsToDisplay = [];
  @Input() columnsToIterate = [];
  @Output() Click = new EventEmitter<number>();
  @Output() DoubleClick = new EventEmitter<number>();
  @Output() Delete = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)){
        let vary = this.get(propName);
        vary = changes[propName].currentValue;
      }
    }
  }

  get(element: string): string[] {
    switch (element) {
      case 'dataSource':
        return this.dataSource;
      case 'columnsToDisplay':
        return this.columnsToDisplay;
      default:
        return this.columnsToIterate;
    }
  }


  clicked(id): void {
    this.Click.emit(id);
  }

  doubleClicked(id): void {
    this.DoubleClick.emit(id);
  }

  delete(id) {
    this.Delete.emit(id);
  }
}
