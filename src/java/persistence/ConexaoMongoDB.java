package persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.JOptionPane;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 21/07/2018
 * @version 1
 */
public final class ConexaoMongoDB {
    
    public static final String URL_MONGODB = "mongodb://127.0.0.1:27017";
    public static final String DB = "db_upload_video";
    private static com.mongodb.client.MongoCollection<org.bson.Document> c;
    
    public static MongoCollection<org.bson.Document> conect() throws Exception {
        try {
            MongoClientURI uri = new MongoClientURI(ConexaoMongoDB.URL_MONGODB);
            MongoClient mongoClient = new MongoClient(uri);
            MongoDatabase database = mongoClient.getDatabase(ConexaoMongoDB.DB);
            mongoClient.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "", "Erro de conexao", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Erro de conexao");
        }
        return ConexaoMongoDB.c;
    }

    public static MongoCollection<Document> getC() {
        return c;
    }

    public static void setC(MongoCollection<Document> c) {
        ConexaoMongoDB.c = c;
    }
    
}
