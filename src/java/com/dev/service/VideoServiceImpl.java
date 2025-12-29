package com.dev.service;

import com.dev.dao.VideoMogoDaoDB;
import com.dev.documents.VideoBson;
import com.dev.documents.VideoBsonBig;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public class VideoServiceImpl implements VideoService {
    
    @Override
    public ArrayList<VideoBson> buscarVideos(MongoDatabase database) throws Exception {
        return (ArrayList<VideoBson>) VideoMogoDaoDB.findAll(database);
    }

    @Override
    public void deletarVideo(MongoDatabase database, long idVideo) throws Exception {
        VideoMogoDaoDB.delete(database, idVideo);
    }

    @Override
    public VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception {
        return VideoMogoDaoDB.streamVideo(database, idVideo);
    }

    @Override
    public void inserir(MongoDatabase database, VideoBson videoBson) throws Exception {
        VideoMogoDaoDB.inserir(database, videoBson);
    }

    @Override
    public void insertBig(VideoBsonBig videoBsonBig) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
