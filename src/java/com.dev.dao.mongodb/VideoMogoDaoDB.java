package com.dev.dao;

import com.dev.documentBson.IdVideoBson;
import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBson2;
import com.dev.persistence.ConexaoMongoDB;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.Binary;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 07/09/2018
 * @version 1
 */
public final class VideoMogoDaoDB {

    public static void insert(MongoDatabase database, VideoBson2 videoBson2) throws Exception {
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            ConexaoMongoDB.getCollection().insertOne(videoBson2.getDocumento());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Payload document size is larger than maximum")) {
                throw new Exception("Arquivo muito grande");
            }
            throw new Exception("Erro ao inserir documentos");
        }        
    }
    
//    /**
//     * 
//     * @deprecated
//     * @see Api.requestGET();
//     */
    public static List<VideoBson2> findAll(MongoDatabase database) throws Exception {
        List<VideoBson2> list = null;
        try {
            ConexaoMongoDB.setCollection(database.getCollection("videos"));
            FindIterable<Document> resultado = ConexaoMongoDB.getCollection().find();
            list = new ArrayList<>();
            for (Document documento : resultado) {
                VideoBson2 videoBson2 = new VideoBson2();
//                videoBson2.setId(documento.getObjectId("_id"));
                videoBson2.setIdVideo(documento.getLong("id_video"));
                videoBson2.setName(documento.getString("name"));
                videoBson2.setDateTime(documento.getDate("date_time"));
                videoBson2.setLength(documento.getLong("length"));
                videoBson2.setContent(documento.get("content", Binary.class).getData());
                
                list.add(videoBson2);
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
