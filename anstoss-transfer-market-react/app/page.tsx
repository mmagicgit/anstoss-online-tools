'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import PlayerSearchForm from "@/components/PlayerSearchForm";
import PlayerTable from "@/components/PlayerTable";
import {initialPlayerFormSelection, PlayerFormSelection} from "@/model/PlayerFormSelection";
import {PlayerTableLoading} from "@/components/PlayerTableLoading";

export default function Home() {
    const [players, setPlayers] = useState<Player[]>([])
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        fetchPlayers(initialPlayerFormSelection);
    }, []);

    function fetchPlayers(selection: PlayerFormSelection) {
        try {
            setIsLoading(true)
            const positionString = selection.position.map(position => "position=" + position).join("&");
            const categoryString = selection.category.map(category => "&category=" + category).join("&");
            const strength = "&strengthFrom=" + selection.strengthFrom + "&strengthTo=" + selection.strengthTo;
            const age = "&ageFrom=" + selection.ageFrom + "&ageTo=" + selection.ageTo;
            const percent = selection.percent > 0 ? "&maxPercent=" + selection.percent : "";
            const url = "https://mmagic.uber.space/" + "/player/search?" + positionString + categoryString + strength + age + percent;
            fetch(url)
                .then(response => response.json())
                .then(data => setPlayers(data))
        } finally {
            const delay =
                (ms: number) => new Promise(res => setTimeout(res, ms));
            async function go() {
                await delay(3000)
                setIsLoading(false)
            }
            go()
        }
    }

    return (
        <div className="flex flex-col items-center">
            <h1 className="text-xl font-bold">Spielersuche</h1>
            <PlayerSearchForm onSearch={fetchPlayers}/>
            <div className="divider"></div>
            {isLoading ?
                <PlayerTableLoading/>
                :
                <PlayerTable players={players}/>
            }
        </div>
    );
}