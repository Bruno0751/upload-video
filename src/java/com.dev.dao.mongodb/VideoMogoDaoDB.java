package com.dev.dao;

import com.dev.def.Constants;
import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBsonBig;
import com.dev.persistence.ConexaoMongoDB;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.Binary;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 16/11/2025
 * @version 1
 */
public final class VideoMogoDaoDB {

    public static void insertBig(VideoBsonBig videoBsonBig, Properties properties) throws Exception {
        com.mongodb.client.MongoClient mongoClient = null;
        try {
            ConnectionString connString = new ConnectionString(properties.getProperty("MONGODB_DB"));
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("meu_banco");

            GridFSBucket gridFSBucket = GridFSBuckets.create(database, "videos");
            gridFSBucket.uploadFromStream("XXX", videoBsonBig.getContentBig());

        } catch (Exception e) {
            throw new Exception("Erro ao inserir vídeo no MongoDB: " + e.getMessage(), e);
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

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

    public static List<VideoBson> findAll(MongoDatabase database) throws Exception {
        List<VideoBson> list = null;
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            FindIterable<Document> resultado = ConexaoMongoDB.getCollection().find();
            list = new ArrayList<>();
            for (Document documento : resultado) {
                VideoBson videoBson = new VideoBson();
                videoBson.setIdVideo(documento.getLong("id_video"));
                videoBson.setName(documento.getString("name"));
                videoBson.setContent(documento.get("content", Binary.class).getData());
                videoBson.setDateTime(documento.getDate("date_time"));
                videoBson.setLength(documento.getLong("length"));
                videoBson.setEmail(documento.getString("email"));

                list.add(videoBson);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Error find");
        }
        return list;
    }

    public static void delete(MongoDatabase database, long idVideo) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().deleteOne(new Document("id_video", idVideo));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Erro ao inserir documentos");
        } finally {
        }
    }

    public static VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception {
        byte[] content = null;
        try {

            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            Document doc = ConexaoMongoDB.getCollection().find(Filters.eq("id_video", idVideo)).first();

            if (doc != null) {
                Binary bin = doc.get("content", Binary.class);
                content = bin.getData();

            } else {
                System.out.println("Nenhum vídeo encontrado com id " + idVideo);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Erro ao inserir documentos");
        }
        return new VideoBson(content);
    }
}
