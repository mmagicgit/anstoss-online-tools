'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {Sort, SortMode} from "@/model/Sort";
import SearchForm from "@/components/SearchForm";
import PlayerTable from "@/components/PlayerTable";

export default function Home() {
    const [players, setPlayers] = useState<Player[]>([])
    const [sort, setSort] = useState<Sort>({field: "", mode: "NO"})

    useEffect(() => {
        fetch("https://mmagic.uber.space//player/search?position=MD&position=RV&position=LV&position=LIB&position=LM&position=RM&position=ZM&position=ST&category=TRAINING&category=FITNESS&category=EINSATZ&category=ALTER&=&=&=&strengthFrom=4&strengthTo=10&ageFrom=18&ageTo=26&maxPercent=15")
            .then(response => response.json())
            .then(data => setPlayers(data))
    }, []);

    function setSortMode(field: string) {
        let nextMode: SortMode = "NO";
        if (field === sort.field) {
            switch (sort.mode) {
                case "NO":
                    nextMode = "ASC"
                    break;
                case "ASC":
                    nextMode = "DESC"
                    break;
                case "DESC":
                    nextMode = "NO"
                    break;
            }
        } else {
            nextMode = "ASC"
        }
        setSort({field: field, mode: nextMode})
    }

    function getPlayers(): Player[] {
        return sort.mode === "NO" ? players : sortPlayers(players, sort)
    }

    return (
        <div className="flex flex-col items-center">
            <h1 className="text-xl font-bold">Spielersuche</h1>
            <SearchForm/>
            <div className="divider"></div>
            <PlayerTable players={getPlayers()} sort={sort} onSort={setSortMode}/>
        </div>
    );
}

function sortPlayers(players: Player[], sort: Sort): Player[] {
    function sortFn(a: Player, b: Player): number {
        const isAsc = sort.mode === 'ASC';
        switch (sort.field) {
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
    }
    return players.slice().sort(sortFn)
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}