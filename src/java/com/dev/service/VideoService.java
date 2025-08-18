package com.dev.service;

import com.dev.dao.VideoDao;
import com.dev.model.Video;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public class VideoService {

    public void delete(Connection conecxaoMySQL, long idVideo) throws SQLException {
        VideoDao.delete(conecxaoMySQL, idVideo);
        IdVideoService.delete(conecxaoMySQL, idVideo);
    }

    public Video streamVideo(Connection conecxaoMySQL, long idVideo) throws SQLException {
        Video video = VideoDao.findById(conecxaoMySQL, idVideo);
        return video;
    }
    
}
