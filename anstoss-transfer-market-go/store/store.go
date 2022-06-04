package store

import (
	"anstoss-transfer-market-go/model"
	"context"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
)

type PlayerStore struct {
	playerCollection *mongo.Collection
}

func NewPlayerStore() *PlayerStore {
	clientOptions := options.Client().ApplyURI("mongodb://localhost:27017")
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}
	err = client.Ping(context.TODO(), nil)
	if err != nil {
		log.Fatal(err)
	}
	return &PlayerStore{client.Database("anstoss").Collection("player")}
}

func (store PlayerStore) FindById(id int) *model.Player {
	var player model.Player
	err := store.playerCollection.FindOne(context.TODO(), bson.D{{"_id", id}}).Decode(&player)
	if err != nil {
		log.Fatal(err)
	}
	return &player
}

func (store PlayerStore) FindAll() *[]model.Player {
	cursor, err := store.playerCollection.Find(context.TODO(), bson.D{})
	if err != nil {
		log.Fatal(err)
	}
	var players []model.Player
	err = cursor.All(context.TODO(), &players)
	if err != nil {
		log.Fatal(err)
	}
	return &players
}

func (store PlayerStore) DeleteAll() {
	_, err := store.playerCollection.DeleteMany(context.TODO(), bson.D{})
	if err != nil {
		log.Fatal(err)
	}
}

func (store PlayerStore) Save(players *[]model.Player) {
	var typeForInsertion []interface{}
	for _, player := range *players {
		typeForInsertion = append(typeForInsertion, player)
	}
	_, err := store.playerCollection.InsertMany(context.TODO(), typeForInsertion)
	if err != nil {
		log.Fatal(err)
	}
}

func (store PlayerStore) Search(positions []string, strengthFrom int, strengthTo int, ageFrom int, ageTo int, maxPercent int, maxAgePercent int) *[]model.Player {
	step1 := bson.M{"$match": bson.M{"$and": []bson.M{{"age": bson.M{"$gte": ageFrom}}, {"age": bson.M{"$lte": ageTo}}, {"strength": bson.M{"$gte": strengthFrom}}, {"strength": bson.M{"$lte": strengthTo}}, {"position": bson.M{"$in": positions}}}}}
	step2 := bson.M{"$addFields": bson.M{"maxPercent": bson.M{"$max": []bson.M{{"$max": "$aaw.Training"}, {"$max": "$aaw.Einsatz"}, {"$max": "$aaw.Tor"}, {"$max": "$aaw.Alter"}, {"$max": "$aaw.Fitness"}}}, "maxAgePercent": bson.M{"$max": "$aaw.Alter"}}}
	step3 := bson.M{"$match": bson.M{"$and": []bson.M{{"maxPercent": bson.M{"$gte": maxPercent}}, {"maxAgePercent": bson.M{"$gte": maxAgePercent}}}}}
	pipeline := []bson.M{step1, step2, step3}
	cursor, err := store.playerCollection.Aggregate(context.TODO(), pipeline)
	if err != nil {
		log.Fatal(err)
	}
	var players []model.Player
	err = cursor.All(context.TODO(), &players)
	if err != nil {
		log.Fatal(err)
	}
	return &players
}
