'use client'
import {useEffect, useState} from "react";

export default function Home() {
    const toggleDark = () => {
        document.documentElement.classList.toggle('dark')
    }

    const [players, setPlayers] = useState([])
    useEffect(() => {
        fetch("https://mmagic.uber.space//player/search?position=MD&position=RV&position=LV&position=LIB&position=LM&position=RM&position=ZM&position=ST&category=TRAINING&category=FITNESS&category=EINSATZ&category=ALTER&=&=&=&strengthFrom=4&strengthTo=10&ageFrom=18&ageTo=26&maxPercent=15")
            .then(response => response.json())
            .then(data => setPlayers(data))
    }, []);
    return (
        <div>
            <button onClick={toggleDark}>Toggle</button>
            <ul>
                {players.map(player => (
                    <li key={player["id"]}>{player["name"]}</li>
                ))}
            </ul>
        </div>
    );
}
