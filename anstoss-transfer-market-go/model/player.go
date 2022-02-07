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
}
