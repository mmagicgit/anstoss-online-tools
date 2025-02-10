import {Sort, SortMode} from "@/model/Sort";
import PlayerTableHeader from "@/components/PlayerTableHeader";
import {Player} from "@/model/Player";
import {useState} from "react";

interface PlayerTableProps {
    players: Player[],
}

export default function PlayerTable({players}: PlayerTableProps) {
    const [sort, setSort] = useState<Sort>({field: "", mode: "NO"})

    function toggleSortMode(field: string) {
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
                    <PlayerTableHeader sort={sort} name="Position" columnKey="position" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Name" columnKey="name" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Stärke" columnKey="strength" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Alter" columnKey="age" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Land" columnKey="country" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Preis" columnKey="price" onSort={toggleSortMode}/>
                    <PlayerTableHeader sort={sort} name="Tage" columnKey="days" onSort={toggleSortMode}/>
                </tr>
                </thead>
                <tbody>
                {getPlayers().map(player => (
                    <tr key={player.id} className="hover">
                        <td>{player.position}</td>
                        <td><a className="link"
                               href={"https://www.anstoss-online.de/?do=spieler&spieler_id=" + player.id + "#"}>{player.name}</a>
                        </td>
                        <td className="text-right">{player.strength.toLocaleString("en-EN", {
                            minimumFractionDigits: 1,
                            maximumFractionDigits: 1
                        })}</td>
                        <td className="text-right">{player.age}</td>
                        <td>{player.country}</td>
                        <td className="text-right">{player.price.toLocaleString()}</td>
                        <td className="text-right">{player.days}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
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