package com.dev.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.dev.model.IdVideo;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public final class IdVideoDao {

    private final static String SELECT = "SELECT * FROM db_upload_video.id_video";
    private final static String INSERT = "INSERT INTO db_upload_video.id_video";
    private final static String DELETE = "DELETE FROM db_upload_video.id_video";

    public static long insert(Connection conexaoMysql, IdVideo idVideo) throws SQLException {
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            pst = conexaoMysql.prepareStatement(IdVideoDao.INSERT + " VALUES (NULL, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, idVideo.getName());
            pst.setTimestamp(2, idVideo.getDateTime());
            pst.setLong(3, idVideo.getLength());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                idVideo.setIdVideo(rs.getLong(1));
            };
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error insert IdVideo");
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception ignored) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignored) {
                }
            }
        }
        return idVideo.getIdVideo();
    }

    public static ArrayList<IdVideo> find(Connection conexaoMysql) throws SQLException {
        ResultSet rs = null;
        ArrayList lista;
        try (PreparedStatement pst = conexaoMysql.prepareStatement(IdVideoDao.SELECT + ";")) {
            rs = pst.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                IdVideo IdVideo = new IdVideo();
                IdVideo.setIdVideo(rs.getInt("id_video"));
                IdVideo.setName(rs.getString("name"));
                IdVideo.setDateTime(rs.getTimestamp("data_time"));
                IdVideo.setLength(rs.getLong("length"));
                lista.add(IdVideo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error find IdVideo");
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public static void delete(Connection conecxaoMySQL, long idVideo) throws SQLException {
        try (PreparedStatement pst = conecxaoMySQL.prepareStatement(IdVideoDao.DELETE + " WHERE id_video = ?;")) {
            pst.setLong(1, idVideo);
            pst.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException("Erro delete IdVideo");
        }
    }

    public static ArrayList<IdVideo> findBy(Connection conecxaoMySQL, String query) throws SQLException {
        ResultSet rs = null;
        try (Statement st = conecxaoMySQL.createStatement()) {
            rs = st.executeQuery(IdVideoDao.SELECT + " " + query + ";");
            ArrayList<IdVideo> lista = new ArrayList<>();
            while (rs.next()) {
                IdVideo idVideo = new IdVideo();

//                cliente.setIdCliente(rs.getLong("id_cliente"));
//                cliente.setNome(rs.getString("nome"));
//                cliente.setCpf(rs.getString("cpf"));
//                cliente.setIdade(rs.getInt("idade"));
//                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error findBy IdVideo");
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }
}
