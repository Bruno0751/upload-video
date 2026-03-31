package com.dev.dao;

import com.dev.documents.VideoBson;
import com.dev.persistence.ConexaoMongoDB;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.Binary;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 16/11/2025
 * @version 1
 */
public class VideoDao {
    
     public static void inserir(MongoDatabase database, VideoBson videoBson) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().insertOne(videoBson.getDocumento());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Payload document size is larger than maximum")) {
                throw new Exception("Arquivo muito grande");
            }
            throw new Exception("Erro ao inserir documentos");
        }
    }

    public static List<VideoBson> buscarVideos(MongoDatabase database) throws Exception {
        List<VideoBson> list = null;
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            FindIterable<Document> resultado = ConexaoMongoDB.getCollection().find();
            list = new ArrayList<>();
            for (Document documento : resultado) {
                VideoBson videoBson = new VideoBson();
                videoBson.setId(documento.getObjectId("_id").toHexString());
                videoBson.setIdVideo(documento.getLong("id_video"));
                videoBson.setName(documento.getString("name"));
                videoBson.setLength(documento.getLong("length"));
                videoBson.setEmail(documento.getString("email"));

                list.add(videoBson);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Error buscarVideos");
        }
        return list;
    }

    public static void delete(MongoDatabase database, String id) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().deleteOne(new Document("_id", new ObjectId(id)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Erro ao delete documentos");
        }
    }    
}
