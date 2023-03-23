package model

type Player struct {
	Id       int     `bson:"_id" json:"id"`
	Age      int     `json:"age"`
	Name     string  `json:"name"`
	Position string  `json:"position"`
	Country  string  `json:"country"`
	Price    int     `json:"price"`
	Days     int     `json:"days"`
	Strength float64 `json:"strength"`
	Aaw      map[AawCategory][]int
}

type AawCategory string

const (
	Training AawCategory = "Training"
	Fitness  AawCategory = "Fitness"
	Einsatz  AawCategory = "Einsatz"
	Alter    AawCategory = "Alter"
	Tor      AawCategory = "Tor"
)
