export default function SearchForm() {
    return (
        <div className="grid grid-cols-2 gap-2 max-w-xs">
            <label className="form-control col-span-2">
                <div className="label">
                    <span className="label-text text-xs">Position</span>
                </div>
                <div className="join gap-1">
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">MD</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">RV</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">LV</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">LIB</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">LM</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">RM</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">ZM</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">ST</button>
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Stärke</span>
                </div>
                <div className="flex items-center gap-2">
                    <select className="flex-1 select select-bordered select-xs">
                        <option>3</option>
                        <option>4</option>
                    </select>
                    <span className="text-xs">bis</span>
                    <select className="flex-1 select select-bordered select-xs">
                        <option>12</option>
                        <option>13</option>
                    </select>
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Alter</span>
                </div>
                <div className="flex items-center gap-2">
                    <select className="flex-1 select select-bordered select-xs">
                        <option>18</option>
                        <option>19</option>
                    </select>
                    <span className="text-xs">bis</span>
                    <select className="flex-1 select select-bordered select-xs">
                        <option>33</option>
                        <option>34</option>
                        <option>35</option>
                    </select>
                </div>
            </label>
            <label className="form-control col-span-2">
                <div className="label">
                    <span className="label-text text-xs">AAW Kategorie</span>
                </div>
                <div className="join gap-1">
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">TRAINING</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">FITNESS</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item">EINSATZ</button>
                    <button className="no-animation flex-1 btn btn-xs btn-active join-item btn-info">ALTER</button>
                </div>
            </label>
            <label className="form-control">
                <div className="label">
                    <span className="label-text text-xs">Max. AAW Prozentwert ≥</span>
                </div>
                <select className="select select-bordered select-xs">
                    <option>20</option>
                    <option>21</option>
                </select>
            </label>
            <div></div>
            <div className="flex justify-center col-span-2 gap-1 mt-6">
                <button className="btn btn-xs btn-active">Suchen</button>
                <button className="btn btn-xs btn-active">Zurücksetzen</button>
            </div>
        </div>
    )
}