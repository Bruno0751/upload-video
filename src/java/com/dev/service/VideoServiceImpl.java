package com.dev.service;

import com.dev.dao.VideoDao;
import com.dev.documents.VideoBson;
import com.mongodb.client.MongoDatabase;
import java.util.List;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/09/2022
 * @version 1
 */
public class VideoServiceImpl implements VideoService {
    
    @Override
    public List<VideoBson> buscarVideos(MongoDatabase database) throws Exception {
        return VideoDao.buscarVideos(database);
    }

    @Override
    public void deletarVideo(MongoDatabase database, String idVideo) throws Exception {
        VideoDao.delete(database, idVideo);
    }

    @Override
    public VideoBson streamVideo(MongoDatabase database, long idVideo) throws Exception {
        return VideoDao.streamVideo(database, idVideo);
    }

    @Override
    public void inserir(MongoDatabase database, VideoBson videoBson) throws Exception {
        VideoDao.inserir(database, videoBson);
    }

}
