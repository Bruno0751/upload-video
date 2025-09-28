package com.dev.persistence;

import com.dev.def.Constants;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.JOptionPane;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 21/07/2025
 * @version 1
 */
public final class ConexaoMongoDB {
    
    private static final String DB = "db_upload_video";
    private static com.mongodb.client.MongoCollection<org.bson.Document> collection;
    private static com.mongodb.MongoClient mongoClient = null;
    
    
    public static MongoDatabase conect() throws Exception {
        MongoDatabase database = null;
        try {
            MongoClientURI mongoClientURI = new MongoClientURI(Constants.URL_MONGODB);
            mongoClient = new com.mongodb.MongoClient(mongoClientURI);
            database = mongoClient.getDatabase(ConexaoMongoDB.DB);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "", "Erro de conexao", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Erro de conexao");
        }
        return database;
    }

    public static MongoCollection<Document> getCollection() {
        return collection;
    }

    public static void setCollection(MongoCollection<Document> collection) {
        ConexaoMongoDB.collection = collection;
    }
    
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            collection = null;
        }
    }
    
}
