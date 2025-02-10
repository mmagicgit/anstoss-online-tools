'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import PlayerSearchForm from "@/components/PlayerSearchForm";
import PlayerTable from "@/components/PlayerTable";
import {PlayerFormSelection, initialPlayerFormSelection} from "@/model/PlayerFormSelection";

export default function Home() {
    const [players, setPlayers] = useState<Player[]>([])

    useEffect(() => {
        fetchPlayers(initialPlayerFormSelection);
    }, []);

    function fetchPlayers(selection: PlayerFormSelection) {
        const positionString = selection.position.map(position => "position=" + position).join("&");
        const categoryString = selection.category.map(category => "&category=" + category).join("&");
        const strength = "&strengthFrom=" + selection.strengthFrom + "&strengthTo=" + selection.strengthTo;
        const age = "&ageFrom=" + selection.ageFrom + "&ageTo=" + selection.ageTo;
        const percent = selection.percent > 0 ? "&maxPercent=" + selection.percent : "";
        const url = "https://mmagic.uber.space/" + "/player/search?" + positionString + categoryString + strength + age + percent;
        fetch(url)
            .then(response => response.json())
            .then(data => setPlayers(data))
    }

    return (
        <div className="flex flex-col items-center">
            <h1 className="text-xl font-bold">Spielersuche</h1>
            <PlayerSearchForm onSearch={fetchPlayers}/>
            <div className="divider"></div>
            <PlayerTable players={players}/>
        </div>
    );
}