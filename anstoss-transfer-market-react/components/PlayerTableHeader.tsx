import {Sort} from "@/model/Sort";

interface TableHeaderProps {
    sort: Sort,
    name: string,
    columnKey: string,
    onSort: (columnKey: string) => void
}

export default function PlayerTableHeader({sort, name, columnKey, onSort}: TableHeaderProps) {
    return (
        <th>
            <button className="btn-ghost hover:bg-base-100" onClick={() => onSort(columnKey)}>
                {name} <span className={sort.mode === "NO" || sort.field !== columnKey ? "opacity-0" : "opacity-100"}>
                                {sort.mode === "DESC" ? "↓" : "↑"}
                            </span>
            </button>
        </th>
    )
}