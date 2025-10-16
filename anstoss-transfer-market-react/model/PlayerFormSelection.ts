export interface PlayerFormSelection {
    ageFrom: number;
    ageTo: number;
    strengthFrom: number;
    strengthTo: number;
    position: string[];
    category: string[];
    percent: number;
    country: string;
}

export const initialPlayerFormSelection = {
    ageFrom: 18,
    ageTo: 26,
    strengthFrom: 5,
    strengthTo: 11,
    position: ["MD", "RV", "LV", "LIB", "LM", "RM", "ZM", "ST"],
    category: ["TRAINING", "FITNESS", "ALTER"],
    percent: 20,
    country: "",
};