package com.dev.service;

import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBson2;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public interface MogodbVideoService {
    
    ArrayList<VideoBson2> findAll(MongoDatabase database) throws Exception;
    
    void delete(MongoDatabase database, long idVideo) throws Exception;

    VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception;

    void insert(MongoDatabase database, VideoBson2 videoBson2) throws Exception;
    
}
