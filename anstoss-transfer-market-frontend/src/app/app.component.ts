import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Player } from "./Player";
import { Sort } from "@angular/material";
import { environment } from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  positionList: string[] = ["MD", "RV", "LV", "LIB", "LM", "RM", "ZM", "ST"];
  selectedPositions: string[] = this.positionList;

  aawCategoryList: string[] = ["TRAINING", "FITNESS", "EINSATZ", "ALTER"];
  selectedAawCategories: string[] = this.aawCategoryList;

  strengthList: number[] = [3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
  selectedStrengthFrom: number = 0;
  selectedStrengthTo: number = 0;

  ageList: number[] = [18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32];
  selectedAgeFrom: number = 0;
  selectedAgeTo: number = 0;

  percentList: number[] = [5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25];
  selectedPercent: number = 0;

  errorMessage: string = null;
  players: Player[] = [];
  sortedData: Player[] = [];

  constructor(private http: HttpClient) {
    this.reset();
    this.search();
  }

  search() {
    const positionString = this.selectedPositions.map(position => "position=" + position).join("&");
    const categoryString = this.selectedAawCategories.map(category => "&category=" + category).join("&");
    const strength = "&strengthFrom=" + this.selectedStrengthFrom + "&strengthTo=" + this.selectedStrengthTo;
    const age = "&ageFrom=" + this.selectedAgeFrom + "&ageTo=" + this.selectedAgeTo;
    let percent = "";
    if (this.selectedPercent > 0) {
      percent += "&maxPercent=" + this.selectedPercent;
    }
    this.http.get<Player[]>(environment.baseUrl + "/player/search?" + positionString + categoryString + strength + age + percent).subscribe(
      players => {
        this.players = players;
        this.sortedData = this.players.slice();
      },
      () => this.errorMessage = "Server error or backend not reachable"
    );
  }

  reset() {
    this.selectedStrengthFrom = 4;
    this.selectedStrengthTo = 10;
    this.selectedAgeFrom = this.ageList[0];
    this.selectedAgeTo = 26;
    this.selectedPercent = 15;
    this.selectedPositions = this.positionList;
    this.selectedAawCategories = this.aawCategoryList;
    this.search();
  }

  togglePosition(position: string) {
    if (this.selectedPositions.indexOf(position) === -1) {
      this.selectedPositions.push(position);
    } else {
      this.selectedPositions = this.selectedPositions.filter(pos => pos !== position);
    }
  }

  toggleCategory(category: string) {
    if (this.selectedAawCategories.indexOf(category) === -1) {
      this.selectedAawCategories.push(category);
    } else {
      this.selectedAawCategories = this.selectedAawCategories.filter(cat => cat !== category);
    }
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

