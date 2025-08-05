package service;

import com.mongodb.client.MongoCollection;
import dao.IdVideoDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import model.IdVideo;
import org.bson.Document;

public class IdVideoService {

    public ArrayList<IdVideo> findAll(Connection conecxaoMySQL, MongoCollection<Document> conexaoMongoDB) throws SQLException {
        ArrayList<IdVideo> lista = IdVideoDao.findAll(conecxaoMySQL);
        return lista;
    }
    
}
