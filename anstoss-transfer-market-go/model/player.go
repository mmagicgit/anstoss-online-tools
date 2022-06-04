package model

type Player struct {
	Id       int `bson:"_id"`
	Age      int
	Name     string
	Position string
	Country  string
	Price    int
	Days     int
	Strength float64
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
