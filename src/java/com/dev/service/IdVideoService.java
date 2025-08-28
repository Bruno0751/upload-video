package com.dev.service;

import com.dev.model.IdVideo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/08/2018
 * @version 1
 */
public interface IdVideoService {

    @Override
    public String toString();

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    ArrayList<IdVideo> find(Connection conecxaoMySQL) throws SQLException;
    
    void delete(Connection conecxaoMySQL, long idVideo) throws SQLException;

}
