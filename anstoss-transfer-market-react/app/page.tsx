'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {Sort, SortMode} from "@/model/Sort";
import TableHeader from "@/components/TableHeader";

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
        <div className="overflow-x-auto relative max-w-2xl">
            <table className="table table-xs">
                <thead>
                <tr>
                    <TableHeader sort={sort} name="Position" columnKey="position" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Name" columnKey="name" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Stärke" columnKey="strength" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Alter" columnKey="age" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Land" columnKey="country" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Preis" columnKey="price" onSort={setSortMode}/>
                    <TableHeader sort={sort} name="Tage" columnKey="days" onSort={setSortMode}/>
                </tr>
                </thead>
                <tbody>
                {getPlayers().map(player => (
                    <tr key={player.id} className="hover">
                        <td>{player.position}</td>
                        <td><a className="link link-primary"
                               href={"https://www.anstoss-online.de/?do=spieler&spieler_id=" + player.id + "#"}>{player.name}</a>
                        </td>
                        <td className="text-right">{player.strength.toLocaleString("en-EN", {minimumFractionDigits: 1, maximumFractionDigits: 1})}</td>
                        <td className="text-right">{player.age}</td>
                        <td>{player.country}</td>
                        <td className="text-right">{player.price.toLocaleString()}</td>
                        <td className="text-right">{player.days}</td>
                    </tr>
                ))}
                </tbody>
            </table>
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