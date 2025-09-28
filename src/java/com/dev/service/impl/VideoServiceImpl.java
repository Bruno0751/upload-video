package com.dev.service.impl;

import com.dev.dao.IdVideoDao;
import com.dev.dao.VideoDao;
import com.dev.model.IdVideo;
import com.dev.model.Video;
import com.dev.service.VideoService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/08/2018
 * @version 1
 */
public class VideoServiceImpl implements VideoService {

    @Override
    public Video streamVideo(Connection conecxaoMySQL, long idVideo) throws SQLException {
        Video video = VideoDao.findById(conecxaoMySQL, idVideo);
        return video;
    }

    @Override
    public ArrayList<IdVideo> find(Connection conecxaoMySQL) throws SQLException {
        ArrayList<IdVideo> lista = IdVideoDao.find(conecxaoMySQL);
        return lista;
    }

    @Override
    public void delete(Connection conecxaoMySQL, long idVideo) throws SQLException {
        VideoDao.delete(conecxaoMySQL, idVideo);
        IdVideoDao.delete(conecxaoMySQL, idVideo);
    }

    

    

}
