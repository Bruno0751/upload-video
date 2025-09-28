package com.dev.service;

import com.dev.documentBson.IdVideoBson;
import com.dev.model.IdVideo;
import com.dev.model.Video;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public interface VideoService {

    @Override
    public String toString();

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();
    
    Video streamVideo(Connection conecxaoMySQL, long idVideo) throws SQLException;
    
    ArrayList<IdVideo> find(Connection conecxaoMySQL) throws SQLException;
    
    void delete(Connection conecxaoMySQL, long idVideo) throws SQLException;

    
}
