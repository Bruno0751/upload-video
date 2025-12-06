package com.dev.service;

import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBsonBig;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public interface VideoService {
    
    ArrayList<VideoBson> buscarVideos(MongoDatabase database) throws Exception;
    
    void deletarVideo(MongoDatabase database, long idVideo) throws Exception;

    VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception;

    void inserir(MongoDatabase database, VideoBson videoBson) throws Exception;
    
    void insertBig(VideoBsonBig videoBsonBig) throws Exception;
    
}
