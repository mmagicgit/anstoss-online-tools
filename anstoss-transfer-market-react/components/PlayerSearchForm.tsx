import {useState} from "react";
import {PlayerFormSelection, initialPlayerFormSelection} from "@/model/PlayerFormSelection";

interface PlayerSearchFormProps {
    onSearch: (selection: PlayerFormSelection) => void
}

export default function PlayerSearchForm({onSearch}: PlayerSearchFormProps) {
    const [selection, setSelection] = useState<PlayerFormSelection>(initialPlayerFormSelection)

    function setPosition(position: string) {
        let positionToApply = [...selection.position]
        if (positionToApply.indexOf(position) === -1) {
            positionToApply.push(position);
        } else {
            positionToApply = positionToApply.filter(pos => pos !== position);
        }
        setSelection({...selection, position: positionToApply})
    }

    function setCategory(category: string) {
        let categoryToApply = [...selection.category]
        if (categoryToApply.indexOf(category) === -1) {
            categoryToApply.push(category);
        } else {
            categoryToApply = categoryToApply.filter(cat => cat !== category);
        }
        setSelection({...selection, category: categoryToApply})
    }

    return (
        <div className="grid grid-cols-2 gap-2 max-w-xs">
            <label className="form-control col-span-2">
                <div className="label">
                    <span className="label-text text-xs">Position</span>
                </div>
                <div className="join gap-1">
                    {["MD", "RV", "LV", "LIB", "LM", "RM", "ZM", "ST"].map(position => (
                        <button key={position} className={
                            "no-animation flex-1 btn btn-xs btn-active join-item " +
                            (selection.position.indexOf(position) > -1 ? "btn-info" : "")}
                                onClick={() => setPosition(position)}
                        >
                            {position}
                        </button>
                    ))}
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Stärke</span>
                </div>
                <div className="flex items-center gap-2">
                    <select className="flex-1 select select-bordered select-xs"
                            value={selection.strengthFrom}
                            onChange={e => setSelection({...selection, strengthFrom: parseInt(e.target.value)})}
                    >
                        {[...Array(14).keys()].slice(3).map(value => (
                            <option key={"strengthFrom" + value}>{value}</option>
                        ))}
                    </select>
                    <span className="text-xs">bis</span>
                    <select className="flex-1 select select-bordered select-xs"
                            value={selection.strengthTo}
                            onChange={e => setSelection({...selection, strengthTo: parseInt(e.target.value)})}>
                        {[...Array(14).keys()].slice(3).map(value => (
                            <option key={"strengthTo" + value}>{value}</option>
                        ))}
                    </select>
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Alter</span>
                </div>
                <div className="flex items-center gap-2">
                    <select className="flex-1 select select-bordered select-xs"
                            value={selection.ageFrom}
                            onChange={e => setSelection({...selection, ageFrom: parseInt(e.target.value)})}
                    >
                        {[...Array(36).keys()].slice(18).map(value => (
                            <option key={"ageFrom" + value}>{value}</option>
                        ))}
                    </select>
                    <span className="text-xs">bis</span>
                    <select className="flex-1 select select-bordered select-xs"
                            value={selection.ageTo}
                            onChange={e => setSelection({...selection, ageTo: parseInt(e.target.value)})}
                    >
                        {[...Array(36).keys()].slice(18).map(value => (
                            <option key={"ageTo" + value}>{value}</option>
                        ))}
                    </select>
                </div>
            </label>
            <label className="form-control col-span-2">
                <div className="label">
                    <span className="label-text text-xs">AAW Kategorie</span>
                </div>
                <div className="join gap-1">
                    {["TRAINING", "FITNESS", "EINSATZ", "ALTER"].map(category => (
                        <button key={category}
                                className={"no-animation flex-1 btn btn-xs btn-active join-item " +
                                    (selection.category.indexOf(category) > -1 ? "btn-info" : "")}
                                onClick={() => setCategory(category)}>
                            {category}
                        </button>
                    ))}
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Max. AAW Prozentwert ≥</span>
                </div>
                <select className="select select-bordered select-xs"
                        value={selection.percent}
                        onChange={e => setSelection({...selection, percent: parseInt(e.target.value)})}
                >
                    <option key="percent-inactive" value="-1"></option>
                    {[...Array(26).keys()].slice(5).map(value => (
                        <option key={"percent" + value}>{value}</option>
                    ))}
                </select>
            </label>
            <div></div>
            <div className="flex justify-center col-span-2 gap-1 mt-6">
                <button className="btn btn-xs btn-active"
                        onClick={() => onSearch(selection)}
                >
                    Suchen
                </button>
                <button className="btn btn-xs btn-active" type="submit"
                        onClick={() => setSelection(initialPlayerFormSelection)}
                >
                    Zurücksetzen
                </button>
            </div>
        </div>
    )
}