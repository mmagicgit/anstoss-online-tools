'use client'
import {useEffect, useState} from "react";
import {Player} from "@/model/Player";

export default function Home() {
    const [players, setPlayers] = useState<Player[]>([])
    useEffect(() => {
        fetch("https://mmagic.uber.space//player/search?position=MD&position=RV&position=LV&position=LIB&position=LM&position=RM&position=ZM&position=ST&category=TRAINING&category=FITNESS&category=EINSATZ&category=ALTER&=&=&=&strengthFrom=4&strengthTo=10&ageFrom=18&ageTo=26&maxPercent=15")
            .then(response => response.json())
            .then(data => setPlayers(data))
    }, []);
    return (
        <div className="overflow-x-auto">
            <table className="table table-xs">
                <thead>
                    <tr>
                        <th>Position</th>
                        <th>Name</th>
                        <th>Stärke</th>
                        <th>Alter</th>
                        <th>Land</th>
                        <th>Preis</th>
                        <th>Tage</th>
                    </tr>
                </thead>
                <tbody>
                    {players.map(player => (
                        <tr key={player.id} className="hover">
                            <td>{player.position}</td>
                            <td>{player.name}</td>
                            <td>{player.strength}</td>
                            <td>{player.age}</td>
                            <td>{player.country}</td>
                            <td>{player.price}</td>
                            <td>{player.days}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
