package com.dev.service;

import com.dev.dao.IdVideoDaoMogoDB;
import com.dev.dao.VideoMogoDaoDB;
import com.dev.documentBson.IdVideoBson;
import com.dev.documentBson.VideoBson;
import com.dev.service.impl.VideoServiceImpl;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public class MogodbVideoServiceImpl implements MogodbVideoService{

    @Override
    public ArrayList<IdVideoBson> find(MongoDatabase database) throws Exception {
        return (ArrayList<IdVideoBson>) IdVideoDaoMogoDB.find(database);
    }
    
    @Override
    public void insert(com.mongodb.client.MongoDatabase database, IdVideoBson idVideoBson, byte[] videoBytes) throws Exception {
        long id = IdVideoDaoMogoDB.insert(database, idVideoBson.getDocumento());
        VideoBson videoBson = new VideoBson(
                videoBytes,
                id
        );
        VideoMogoDaoDB.insert(database, videoBson.getDocumento());
    }
    
    @Override
    public void delete(MongoDatabase database, long idVideo) {
        try {
            VideoMogoDaoDB.delete(database, idVideo);
            IdVideoDaoMogoDB.delete(database, idVideo); 
        } catch (Exception ex) {
            System.getLogger(VideoServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @Override
    public VideoBson streamVideo(MongoDatabase database, long idVideo) {
        VideoBson VideoBson = null;
        try {
            VideoBson = VideoMogoDaoDB.streamVideo(database, idVideo);
        } catch (Exception ex) {
            System.getLogger(MogodbVideoServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return VideoBson;
    }
    
}
