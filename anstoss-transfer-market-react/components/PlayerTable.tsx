import {Sort} from "@/model/Sort";
import TableHeader from "@/components/TableHeader";
import {Player} from "@/model/Player";

interface PlayerTableProps {
    players: Player[],
    sort: Sort,
    onSort: (columnKey: string) => void
}

export default function PlayerTable({players, sort, onSort}: PlayerTableProps) {
    return (
        <div className="overflow-x-auto relative max-w-2xl">
            <table className="table table-xs">
                <thead>
                <tr>
                    <TableHeader sort={sort} name="Position" columnKey="position" onSort={onSort}/>
                    <TableHeader sort={sort} name="Name" columnKey="name" onSort={onSort}/>
                    <TableHeader sort={sort} name="Stärke" columnKey="strength" onSort={onSort}/>
                    <TableHeader sort={sort} name="Alter" columnKey="age" onSort={onSort}/>
                    <TableHeader sort={sort} name="Land" columnKey="country" onSort={onSort}/>
                    <TableHeader sort={sort} name="Preis" columnKey="price" onSort={onSort}/>
                    <TableHeader sort={sort} name="Tage" columnKey="days" onSort={onSort}/>
                </tr>
                </thead>
                <tbody>
                {players.map(player => (
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