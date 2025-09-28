package com.dev.dao;

import com.dev.documentBson.IdVideoBson;
import com.dev.persistence.ConexaoMongoDB;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Filters.eq;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 07/09/2025
 * @version 1
 */
public final class IdVideoDaoMogoDB {

    public static void delete(MongoDatabase database, long idVideo) {
        ConexaoMongoDB.setCollection(database.getCollection("id_videos"));
        DeleteResult result = ConexaoMongoDB.getCollection().deleteOne(eq("id_video", idVideo));
        if (result.getDeletedCount() > 0) {
            System.out.println("Vídeo removido com sucesso!");
        } else {
            System.out.println("Nenhum vídeo encontrado com este id.");
        }

    }

    public static long insert(MongoDatabase database, Document idVideoDoc) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("id_videos"));
            ConexaoMongoDB.getCollection().insertOne(idVideoDoc);
            Document maxDoc = ConexaoMongoDB.getCollection().find()
                    .sort(Sorts.descending("id_video"))
                    .first();

            if (maxDoc != null) {
                return maxDoc.get("id_video", Number.class).longValue();
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Error insert IdVideo");
        }
    }

    public static List<IdVideoBson> find(MongoDatabase database) throws Exception {
        List<IdVideoBson> list = null;
        try {
            ConexaoMongoDB.setCollection(database.getCollection("id_videos"));
            FindIterable<Document> resultado = ConexaoMongoDB.getCollection().find();
            list = new ArrayList<>();
            for (Document documento : resultado) {
                IdVideoBson idVideoBson = new IdVideoBson();
                idVideoBson.setId(documento.getObjectId("_id"));
                idVideoBson.setIdVideo(documento.getLong("id_video"));
                idVideoBson.setName(documento.getString("name"));
                idVideoBson.setDateTime(documento.getString("data_time"));
                idVideoBson.setLength(documento.getLong("length"));
                list.add(idVideoBson);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Error find");
        }
        return list;
    }

}
