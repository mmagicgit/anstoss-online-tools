package store

import (
	"anstoss-transfer-market-go/model"
	"context"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

type PlayerStore struct {
	playerCollection *mongo.Collection
}

func NewPlayerStore(connectString string) (*PlayerStore, error) {
	clientOptions := options.Client().ApplyURI(connectString)
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		return nil, err
	}
	err = client.Ping(context.TODO(), nil)
	if err != nil {
		return nil, err
	}
	return &PlayerStore{client.Database("anstoss").Collection("player")}, nil
}

func (store *PlayerStore) FindById(id int) (*model.Player, error) {
	var player model.Player
	err := store.playerCollection.FindOne(context.TODO(), bson.D{{"_id", id}}).Decode(&player)
	if err != nil {
		return nil, err
	}
	return &player, nil
}

func (store *PlayerStore) FindAll() (*[]model.Player, error) {
	cursor, err := store.playerCollection.Find(context.TODO(), bson.D{})
	if err != nil {
		return nil, err
	}
	var players []model.Player
	err = cursor.All(context.TODO(), &players)
	if err != nil {
		return nil, err
	}
	return &players, nil
}

func (store *PlayerStore) DeleteAll() error {
	_, err := store.playerCollection.DeleteMany(context.TODO(), bson.D{})
	if err != nil {
		return err
	}
	return nil
}

func (store *PlayerStore) Save(players *[]model.Player) error {
	var typeForInsertion []interface{}
	for _, player := range *players {
		typeForInsertion = append(typeForInsertion, player)
	}
	_, err := store.playerCollection.InsertMany(context.TODO(), typeForInsertion)
	if err != nil {
		return err
	}
	return nil
}

func (store *PlayerStore) Search(positions []string, strengthFrom int, strengthTo int, ageFrom int, ageTo int, maxPercent int, categories []string) (*[]model.Player, error) {
	step1 := bson.M{"$match": bson.M{"$and": []bson.M{{"age": bson.M{"$gte": ageFrom}}, {"age": bson.M{"$lte": ageTo}}, {"strength": bson.M{"$gte": strengthFrom}}, {"strength": bson.M{"$lte": strengthTo}}, {"position": bson.M{"$in": positions}}}}}
	step2 := bson.M{"$addFields": bson.M{
		"maxTRAININGPercent": bson.M{"$max": "$aaw.Training"},
		"maxFITNESSPercent":  bson.M{"$max": "$aaw.Fitness"},
		"maxEINSATZPercent":  bson.M{"$max": "$aaw.Einsatz"},
		"maxALTERPercent":    bson.M{"$max": "$aaw.Alter"},
	}}
	match := make([]bson.M, 0)
	for _, cat := range categories {
		match = append(match, bson.M{"max" + cat + "Percent": bson.M{"$gte": maxPercent}})
	}
	step3 := bson.M{"$match": bson.M{"$or": match}}

	pipeline := []bson.M{step1, step2, step3}
	cursor, err := store.playerCollection.Aggregate(context.TODO(), pipeline)
	if err != nil {
		return nil, err
	}
	players := make([]model.Player, 0)
	err = cursor.All(context.TODO(), &players)
	if err != nil {
		return nil, err
	}
	return &players, nil
}
