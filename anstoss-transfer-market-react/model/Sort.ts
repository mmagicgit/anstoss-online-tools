export type SortMode = "NO" | "ASC" | "DESC"

export interface Sort {
    mode: SortMode;
    field: string;
}