package com.dev.dao;

import com.dev.documentBson.VideoBson;
import com.dev.persistence.ConexaoMongoDB;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.Binary;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 07/09/2018
 * @version 1
 */
public final class VideoMogoDaoDB {

    public static void insert(MongoDatabase database, Document videoDoc) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().insertOne(videoDoc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Erro ao inserir documentos");
        }
    }

    public static void delete(MongoDatabase database, long idVideo) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().deleteOne(new Document("id_video", idVideo));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Erro ao inserir documentos");
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
        return new VideoBson(content, idVideo);
    }
}
