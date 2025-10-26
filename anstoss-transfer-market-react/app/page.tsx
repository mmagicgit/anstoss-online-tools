'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import PlayerSearchForm from "@/components/PlayerSearchForm";
import PlayerTable from "@/components/PlayerTable";
import {initialPlayerFormSelection, PlayerFormSelection} from "@/model/PlayerFormSelection";
import {PlayerTableLoading} from "@/components/PlayerTableLoading";
import {useRouter, useSearchParams} from "next/navigation";

export default function Home() {
    const searchParams = useSearchParams()
    const router = useRouter()
    const [selection, setSelection] = useState<PlayerFormSelection>(initialSelection())
    const [isLoading, setIsLoading] = useState(true)
    const [players, setPlayers] = useState<Player[]>([])

    useEffect(() => {
        fetchPlayers();
    }, []);

    function initialSelection() {
        let initialState = {...initialPlayerFormSelection}
        if (searchParams.has("ageFrom")) {
            initialState.ageFrom = Number(searchParams.get("ageFrom"))
        }
        if (searchParams.has("ageTo")) {
            initialState.ageTo = Number(searchParams.get("ageTo"))
        }
        if (searchParams.has("strengthFrom")) {
            initialState.strengthFrom = Number(searchParams.get("strengthFrom"))
        }
        if (searchParams.has("strengthTo")) {
            initialState.strengthTo = Number(searchParams.get("strengthTo"))
        }
        if (searchParams.has("percent")) {
            initialState.percent = Number(searchParams.get("percent"))
        }
        if (searchParams.has("country")) {
            initialState.country = searchParams.get("country") || ""
        }
        if (searchParams.has("position")) {
            initialState.position = searchParams.getAll("position")
        }
        if (searchParams.has("category")) {
            initialState.category = searchParams.getAll("category")
        }
        return initialState;
    }

    function fetchPlayers() {
        try {
            setIsLoading(true)
            const url = "https://mmagic.uber.space/" + "/player/search?" + toSearchParamsString();
            fetch(url)
                .then(response => response.json())
                .then(data => setPlayers(data))
        } finally {
            setIsLoading(false)
        }
    }

    function toSearchParamsString() {
        let params = new URLSearchParams();
        selection.position.forEach((position) => params.append("position", position));
        selection.category.forEach((category) => params.append("category", category));
        params.append("strengthFrom", selection.strengthFrom.toString())
        params.append("strengthTo", selection.strengthTo.toString())
        params.append("ageFrom", selection.ageFrom.toString())
        params.append("ageTo", selection.ageTo.toString())
        if (selection.percent > 0) {
            params.append("maxPercent", selection.percent.toString())
        }
        if (selection.country != "") {
            params.append("country", selection.country.toString())
        }
        return params.toString()
    }

    function directLink() {
        router.push("anstoss?" + toSearchParamsString());
    }

    function reset() {
        setSelection(initialPlayerFormSelection)
        router.push("anstoss");
    }

    return (
        <div className="flex flex-col items-center">
            <h1 className="text-xl font-bold">Spielersuche</h1>
            <PlayerSearchForm onSearch={fetchPlayers} setSelection={setSelection} selection={selection}
                              onLink={directLink} onReset={reset}/>
            <div className="divider"></div>
            {isLoading ?
                <PlayerTableLoading/>
                :
                <PlayerTable players={players}/>
            }
        </div>
    );
}