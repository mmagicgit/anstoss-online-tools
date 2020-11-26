package de.mmagic.anstoss.store;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import de.mmagic.anstoss.model.Player;
import org.bson.conversions.Bson;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PlayerStore {

    public static final String DATABASE_NAME = "anstoss";
    public static final String COLLECTION_NAME = "player";

    @Inject
    MongoClient mongoClient;

    public List<Player> findAll() {
        return toList(getCollection().find());
    }

    public List<Player> search(List<String> positions, Integer strengthFrom, Integer strengthTo, Integer ageFrom, Integer ageTo, Integer maxPercent, Integer maxAgePercent) {
        List<Bson> pipeline = List.of(
                new BasicDBObject("$match", new BasicDBObject("$and", List.of(
                        new BasicDBObject("age", new BasicDBObject("$gte", ageFrom)),
                        new BasicDBObject("age", new BasicDBObject("$lte", ageTo)),
                        new BasicDBObject("strength", new BasicDBObject("$gte", strengthFrom)),
                        new BasicDBObject("strength", new BasicDBObject("$lte", strengthTo)),
                        new BasicDBObject("position", new BasicDBObject("$in", positions))))
                ),
                new BasicDBObject("$addFields", new BasicDBObject(Map.of(
                        "maxPercent", new BasicDBObject("$max", List.of(
                                new BasicDBObject("$max", "$aaw.Training"),
                                new BasicDBObject("$max", "$aaw.Einsatz"),
                                new BasicDBObject("$max", "$aaw.Tor"),
                                new BasicDBObject("$max", "$aaw.Alter"),
                                new BasicDBObject("$max", "$aaw.Fitness"))),
                        "maxAgePercent", new BasicDBObject("$max", "$aaw.Alter")))
                ),
                new BasicDBObject("$match", new BasicDBObject("$and", List.of(
                        new BasicDBObject("maxPercent", new BasicDBObject("$gte", maxPercent)),
                        new BasicDBObject("maxAgePercent", new BasicDBObject("$gte", maxAgePercent))))
                )
        );
        return toList(getCollection().aggregate(pipeline));
    }

    public void save(List<Player> players) {
        getCollection().insertMany(players);
    }

    public void deleteAll() {
        getCollection().drop();
    }

    private MongoCollection<Player> getCollection() {
        return mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME, Player.class);
    }

    private List<Player> toList(MongoIterable<Player> players) {
        List<Player> result = new ArrayList<>();
        try (MongoCursor<Player> cursor = players.iterator()) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
        }
        return result;
    }
}
