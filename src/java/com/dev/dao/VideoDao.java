package com.dev.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.dev.model.IdVideo;
import com.dev.model.Video;
import com.dev.persistence.ConexaoMongoDB;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public final class VideoDao {

    private final static String SELECT = "SELECT * FROM db_upload_video.video";
    private final static String INSERT = "INSERT INTO db_upload_video.video (content, id_video)";
    private final static String DELETE = "DELETE FROM db_upload_video.video";
//    private final static MongoClientURI URL_MONGODB = new MongoClientURI(ConexaoMongoDB.URL_MONGODB);

    public static ArrayList<Video> find(Connection conexaoMysql) throws SQLException {
        ResultSet rs = null;
        ArrayList lista;
        ByteArrayOutputStream buffer = null;
        InputStream conteudo = null;
        try (PreparedStatement pst = conexaoMysql.prepareStatement(VideoDao.SELECT + ";")) {
            rs = pst.executeQuery();
            lista = new ArrayList();
            if (rs.next()) {
                Video video = new Video();
                video.setIdTable(rs.getInt("idTable"));
                conteudo = rs.getBinaryStream("content");

                buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int bytesRead;
                while ((bytesRead = conteudo.read(temp)) != -1) {
                    buffer.write(temp, 0, bytesRead);
                }
                video.setContentBytes(buffer.toByteArray());
                lista.add(video);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error find Video");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (Exception ignored) {
                }
            }
            if (conteudo != null) {
                try {
                    conteudo.close();
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
        return lista;
    }

    public static Video findById(Connection conexaoMysql, long id) throws SQLException {
        ResultSet rs = null;
        Video video = null;
        InputStream conteudo = null;
        ByteArrayOutputStream buffer = null;

        try (PreparedStatement pst = conexaoMysql.prepareStatement(VideoDao.SELECT + " WHERE id_video = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pst.setLong(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                video = new Video();
                video.setIdTable(rs.getLong("id_table"));
                conteudo = rs.getBinaryStream("content");
                video.setIdVideo(rs.getLong("id_video"));

                buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int bytesRead;
                while ((bytesRead = conteudo.read(temp)) != -1) {
                    buffer.write(temp, 0, bytesRead);
                }
                video.setContentBytes(buffer.toByteArray());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error findById Video");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (Exception ignored) {
                }
            }
            if (conteudo != null) {
                try {
                    conteudo.close();
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
        return video;
    }

    public static long insert(Connection conexaoMysql, Video video, IdVideo idVideo) throws SQLException, Exception {
        long idVideoAux = IdVideoDao.insert(conexaoMysql, idVideo);
        VideoDao.saveMySQL(conexaoMysql, video, idVideoAux);
        return idVideoAux;
    }
    
    public static void delete(Connection conecxaoMySQL, long idVideo) throws SQLException {
        try (PreparedStatement pst = conecxaoMySQL.prepareStatement(VideoDao.DELETE + " WHERE id_video = ?;")) {
            pst.setLong(1, idVideo);
            pst.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error ao deletar video");
        }
    }

    public static File salvarVideoTemporario(Video video) throws IOException {
        File pastaDestino = new File("D:/file-temp");
        if (!pastaDestino.exists()) {
            pastaDestino.mkdirs(); // cria a pasta (e subpastas, se necessário)
        }

        File tempFile = new File(pastaDestino, "video_" + System.currentTimeMillis() + ".mp4");
        System.out.println("Arquivo criado em: " + tempFile.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(video.getContentBytes());
        }
        return tempFile;
    }

    private static void saveMySQL(Connection conexaoMysql, Video video, long idVideoAux) throws SQLException, IOException {
        ResultSet rs = null;
        byte[] conteudo = video.getContentBytes();
        if (conteudo == null || conteudo.length == 0) {
            throw new RuntimeException("Conteúdo do vídeo está vazio.");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(conteudo);

        try (PreparedStatement pst = conexaoMysql.prepareStatement(VideoDao.INSERT + " VALUES(?, ?);")) {

            pst.setBinaryStream(1, bais, conteudo.length);
            pst.setLong(2, idVideoAux);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Packet for query is too large")) {
                try (PreparedStatement pst = conexaoMysql.prepareStatement("SHOW VARIABLES LIKE 'max_allowed_packet';")) {
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        String large = rs.getString("Value");
                        throw new SQLException("Arquivo muito grande. Máximo permitido: 4GB");
                    }
                }
                if (rs != null) {
                    rs.close();
                }
            }
            throw new SQLException("Error save movie");
        } finally {
            if (bais != null) {
                bais.close();
            }
        }
    }

//    private static void saveMongoDB(MongoCollection<Document> conexaoMongoDB, Video video) throws Exception {
//        try (MongoClient mongoClient = new MongoClient(URL_MONGODB)) {
//
//            MongoDatabase database = mongoClient.getDatabase(ConexaoMongoDB.DB);
//            MongoCollection<Document> collection = database.getCollection("video");
//            com.dev.documentBson.Video videBson = new com.dev.documentBson.Video(video.getContentBytes().toString());
//
//            collection.insertOne(videBson.getDocumento());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new Exception("Erro ao inserir documentos");
//        }
//    }    
}
