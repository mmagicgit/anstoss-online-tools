package de.mmagic.anstoss.anstosstransfermarket.store;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import de.mmagic.anstoss.anstosstransfermarket.model.Player;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PlayerStore {

    public static final String DATABASE_NAME = "anstoss";
    public static final String COLLECTION_NAME = "player";

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    private MongoTemplate mongoTemplate;

    public PlayerStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Player> findAll() {
        return toList(mongoTemplate.getCollection(COLLECTION_NAME).find(Player.class));
    }

    public List<Player> search(List<String> positions, Integer strengthFrom, Integer strengthTo, Integer ageFrom, Integer ageTo, Integer maxPercent, List<String> categories) {
        List<Bson> pipeline = List.of(
                new BasicDBObject("$match", new BasicDBObject("$and", List.of(
                        new BasicDBObject("age", new BasicDBObject("$gte", ageFrom)),
                        new BasicDBObject("age", new BasicDBObject("$lte", ageTo)),
                        new BasicDBObject("strength", new BasicDBObject("$gte", strengthFrom)),
                        new BasicDBObject("strength", new BasicDBObject("$lte", strengthTo)),
                        new BasicDBObject("position", new BasicDBObject("$in", positions))))
                ),
                new BasicDBObject("$addFields", new BasicDBObject(Map.of(
                        "maxTRAININGPercent", new BasicDBObject("$max", "$aaw.Training"),
                        "maxFITNESSPercent", new BasicDBObject("$max", "$aaw.Fitness"),
                        "maxEINSATZPercent", new BasicDBObject("$max", "$aaw.Einsatz"),
                        "maxALTERPercent", new BasicDBObject("$max", "$aaw.Alter")))
                ),
                new BasicDBObject("$match", new BasicDBObject("$or",
                        categories.stream().map(cat -> new BasicDBObject("max" + cat + "Percent", new BasicDBObject("$gte", maxPercent))).toList()
                ))
        );
        return toList(mongoTemplate.getCollection(COLLECTION_NAME).aggregate(pipeline, Player.class));
    }

    public void addAll(List<Player> players) {
        mongoTemplate.insertAll(players);
    }

    public void removeAll() {
        mongoTemplate.remove(new Query(), COLLECTION_NAME);
    }

    private List<Player> toList(MongoIterable<Player> players) {
        List<Player> result = new ArrayList<>();
        try (MongoCursor<Player> cursor = players.iterator()) {
            cursor.forEachRemaining(result::add);
        }
        return result;
    }
}
