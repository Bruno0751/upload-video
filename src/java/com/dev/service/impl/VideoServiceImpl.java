package com.dev.service.impl;

import com.dev.dao.VideoDao;
import com.dev.model.IdVideo;
import com.dev.model.Video;
import com.dev.service.VideoService;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/08/2018
 * @version 1
 */
public class VideoServiceImpl implements VideoService{

    @Override
    public Video streamVideo(Connection conecxaoMySQL, long idVideo) throws SQLException {
        Video video = VideoDao.findById(conecxaoMySQL, idVideo);
        return video;
    }
    
}
