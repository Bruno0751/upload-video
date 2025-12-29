package com.dev.documents;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 13/09/2025
 * @version 1
 */
public class SequenceGenerator {

    private final MongoCollection<Document> counters;

    public SequenceGenerator(MongoCollection<Document> counters) {
        this.counters = counters;
    }

    public long getNextSequence(String name) {
        Document updated = counters.findOneAndUpdate(
                new Document("_id", name),
                new Document("$inc", new Document("seq", 1)),
                new com.mongodb.client.model.FindOneAndUpdateOptions()
                        .returnDocument(com.mongodb.client.model.ReturnDocument.AFTER)
                        .upsert(true)
        );

        Number seq = updated.get("seq", Number.class);
        return seq.longValue();
    }
}
