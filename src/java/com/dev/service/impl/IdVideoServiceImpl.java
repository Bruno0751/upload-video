package com.dev.service.impl;

import com.dev.dao.IdVideoDao;
import com.dev.dao.VideoDao;
import com.dev.model.IdVideo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.dev.service.IdVideoService;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 27/08/2018
 * @version 1
 */
public class IdVideoServiceImpl implements IdVideoService{

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
