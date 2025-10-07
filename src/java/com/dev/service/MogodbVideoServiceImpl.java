package com.dev.service;

import com.dev.dao.VideoMogoDaoDB;
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
public class MogodbVideoServiceImpl implements MogodbVideoService {

    @Override
    public void delete(MongoDatabase database, long idVideo) throws Exception {
        VideoMogoDaoDB.delete(database, idVideo);
    }

    @Override
    public VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception {
        return VideoMogoDaoDB.streamVideo(database, idVideo);
    }

    @Override
    public void insert(MongoDatabase database, VideoBson2 videoBson2) throws Exception {
        VideoMogoDaoDB.insert(database, videoBson2);
    }

    @Override
    public ArrayList<VideoBson2> findAll(MongoDatabase database) throws Exception {
        return (ArrayList<VideoBson2>) VideoMogoDaoDB.findAll(database);
    }

}
