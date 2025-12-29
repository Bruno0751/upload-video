package com.dev.persistence;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 21/07/2025
 * @version 1
 */
public final class ConexaoMongoDB {
    
    private static com.mongodb.client.MongoCollection<org.bson.Document> collection;
    private static com.mongodb.MongoClient mongoClient = null;
    
    public static MongoDatabase conect(Properties properties) throws Exception {
        MongoDatabase database = null;
        try {
            MongoClientURI mongoClientURI = new MongoClientURI(properties.getProperty("MONGODB_URL"));
            mongoClient = new com.mongodb.MongoClient(mongoClientURI);
            database = mongoClient.getDatabase(properties.getProperty("MONGODB_DB"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro de conexao", "Erro", JOptionPane.ERROR_MESSAGE);
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
