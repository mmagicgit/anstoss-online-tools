import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Player} from "./Player";
import {Sort} from "@angular/material";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  errorMessage: string = null;
  players: Player[] = [];
  sortedData: Player[] = [];

  constructor(private http: HttpClient) {
    this.http.get<Player[]>(environment.baseUrl + "/search").subscribe(
      players => {
        this.players = players;
        this.sortedData = this.players.slice();
      },
      () => this.errorMessage = "Server error or backend not reachable"
    );
  }

  sortData(sort: Sort) {
    const data = this.players.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return compare(a.name, b.name, isAsc);
        case 'age':
          return compare(a.age, b.age, isAsc);
        case 'strength':
          return compare(a.strength, b.strength, isAsc);
        case 'position':
          return compare(a.position, b.position, isAsc);
        case 'country':
          return compare(a.country, b.country, isAsc);
        case 'price':
          return compare(a.price, b.price, isAsc);
        case 'days':
          return compare(a.days, b.days, isAsc);
        default:
          return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

