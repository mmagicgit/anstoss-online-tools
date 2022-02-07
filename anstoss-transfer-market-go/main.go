package main

import (
	"anstoss-transfer-market-go/model"
	"context"
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"net/http"
	"strconv"
)

func connectToDatabase() *mongo.Collection {
	clientOptions := options.Client().ApplyURI("mongodb://localhost:27017")

	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}

	err = client.Ping(context.TODO(), nil)
	if err != nil {
		log.Fatal(err)
	}
	return client.Database("anstoss").Collection("player")
}

func hello(res http.ResponseWriter, req *http.Request) {
	_, err := fmt.Fprint(res, "Hello Team!")
	if err != nil {
		log.Fatal(err)
	}
}

func getPlayer(res http.ResponseWriter, req *http.Request) {
	res.Header().Set("Content-Type", "application/json")
	var params = mux.Vars(req)
	fmt.Println(params["id"])
	i, _ := strconv.Atoi(params["id"])
	collection := connectToDatabase()

	var player model.Player
	err := collection.FindOne(context.TODO(), bson.D{{"_id", i}}).Decode(&player)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("Found a single document: %+v\n", player)
	err = json.NewEncoder(res).Encode(player)
	if err != nil {
		log.Fatal(err)
	}
}

func main() {
	r := mux.NewRouter()
	r.HandleFunc("/", hello).Methods("GET")
	r.HandleFunc("/player/{id}", getPlayer).Methods("GET")
	err := http.ListenAndServe(":9000", r)
	if err != nil {
		log.Fatal(err)
	}
}
