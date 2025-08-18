package com.dev.service;

import com.mongodb.client.MongoCollection;
import com.dev.dao.IdVideoDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.dev.model.IdVideo;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public class IdVideoService {

    static void delete(Connection conecxaoMySQL, long idVideo) throws SQLException {
        IdVideoDao.delete(conecxaoMySQL, idVideo);
    }

    public ArrayList<IdVideo> find(Connection conecxaoMySQL, MongoCollection<Document> conexaoMongoDB) throws SQLException {
        ArrayList<IdVideo> lista = IdVideoDao.find(conecxaoMySQL);
        return lista;
    }
    
}
