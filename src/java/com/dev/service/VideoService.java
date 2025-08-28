package com.dev.service;

import com.dev.model.Video;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public interface VideoService {

    @Override
    public String toString();

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();
    
    Video streamVideo(Connection conecxaoMySQL, long idVideo) throws SQLException;
    
}
