package com.dev.service;

import com.dev.documents.VideoBson;
import com.mongodb.client.MongoDatabase;
import java.util.List;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public interface VideoService {
    
    List<VideoBson> buscarVideos(MongoDatabase database) throws Exception;
    
    void deletarVideo(MongoDatabase database, String idVideo) throws Exception;
    
}
