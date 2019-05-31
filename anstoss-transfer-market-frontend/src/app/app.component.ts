import {AfterViewInit, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Player} from "./Player";
import {MatSort, MatTableDataSource, Sort} from "@angular/material";
import {environment} from "../environments/environment";
import {throwError} from "rxjs";
import {catchError} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {

  errorMessage: string = null;
  displayedColumns: string[] = ['position', 'name', 'strength', 'age', 'country', 'price'];
  dataSource = new MatTableDataSource<Player>();

  @ViewChild(MatSort)
  set sort(v: MatSort) {
    this.dataSource.sort = v;
  }

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
    this.http.get<Player[]>(environment.baseUrl + "/search").subscribe(
      players => this.dataSource.data = players,
      () => this.errorMessage = "Backend not reachable"
    );
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }
}

