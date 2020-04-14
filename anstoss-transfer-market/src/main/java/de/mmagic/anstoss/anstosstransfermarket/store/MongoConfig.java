package de.mmagic.anstoss.anstosstransfermarket.store;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


@Configuration
public class MongoConfig {

    @Bean
    public ConnectionString connectionString(@Value("${spring.data.mongodb.uri}") String uri) {
        return new ConnectionString(uri);
    }

    @Bean
    public MongoClient mongoClient(ConnectionString connectionString) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public MongoDbFactory mongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDbFactory(mongoClient, PlayerStore.DATABASE_NAME);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, mongoConverter);
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mongoTemplate;
    }
}
