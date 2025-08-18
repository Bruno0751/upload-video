package com.dev.controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.dev.dao.IdVideoDao;
import com.dev.dao.VideoDao;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dev.persistence.ConecxaoMySQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import com.dev.model.IdVideo;
import com.dev.model.Video;
import org.bson.Document;
import com.dev.persistence.ConexaoMongoDB;
import com.dev.service.IdVideoService;
import com.dev.service.VideoService;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
@WebServlet(name = "ServletVideo", urlPatterns = {"/ServletVideo"})
@MultipartConfig(maxFileSize = 4L * 1024L * 1024L)
public class ServletVideo extends HttpServlet {

    private IdVideoService idVideoService;
    private VideoService videoService;

    public ServletVideo() {
        idVideoService = new IdVideoService();
        videoService = new VideoService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = null;
        Connection conecxaoMySQL = null;
        MongoCollection<org.bson.Document> conexaoMongoDB = null;
        Map saida = new HashMap();
        boolean isBinaryResponse = true;
        saida.put("status", "success");
        String json;
        try {
            conecxaoMySQL = ConecxaoMySQL.conect();
            //conexaoMongoDB = ConexaoMongoDB.conect();
            try {
                String opcao = request.getParameter("opcao");
                switch (opcao) {
                    case "insert":
                        isBinaryResponse = false;
                        this.insert(conecxaoMySQL, conexaoMongoDB, request, response, saida);
                        break;
                    case "find":
                        isBinaryResponse = false;
                        saida = this.find(response, conecxaoMySQL, conexaoMongoDB, saida);
                        break;
//                    case "findById":
//                        this.findById(conecxaoMySQL, request, response, saida, isBinaryResponse);
//                        break;
                    case "streamVideo":
                        this.streamVideo(request, response, conecxaoMySQL, saida, isBinaryResponse);
                        break;
                    case "delete":
                        isBinaryResponse = false;
                        this.delete(request, response, conecxaoMySQL, saida, isBinaryResponse);
                    default:
                        break;

                }
                if (conecxaoMySQL != null) {
                    conecxaoMySQL.commit();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                saida.put("msg", ex.getMessage());
                saida.put("status", "error");
                isBinaryResponse = false;
                if (conecxaoMySQL != null) {
                    conecxaoMySQL.rollback();
                }
                throw ex;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            saida.put("msg", ex.getMessage());
            saida.put("status", "error");
            isBinaryResponse = false;
            if (conecxaoMySQL != null) {
                conecxaoMySQL.rollback();
            }
            throw ex;
        } finally {
            if (conecxaoMySQL != null) {
                conecxaoMySQL.close();
            }
            if (!isBinaryResponse) {
                response.setContentType("application/json;charset=UTF-8");
                out = response.getWriter();
                json = new Gson().toJson(saida);
                response.getWriter().write(json);
                out.close();
            }
        }
    }

    private boolean streamVideo(HttpServletRequest request, HttpServletResponse response, Connection conecxaoMySQL, Map saida, boolean isBinaryResponse) throws IOException, SQLException {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            isBinaryResponse = false;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        response.setContentType("video/mp4");
        long idVideo = Long.parseLong(request.getParameter("id"));

        try {
            Video video = videoService.streamVideo(conecxaoMySQL, idVideo);
            response.setContentLength(video.getContentBytes().length);
            
            try (OutputStream out = response.getOutputStream()) {
                out.write(video.getContentBytes());
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Erro interno ao enviar o vídeo.");
            } catch (IOException ignored) {
            }
        }
        return isBinaryResponse;
    }

    private void delete(HttpServletRequest request, HttpServletResponse response, Connection conecxaoMySQL, Map saida, boolean binaryResponse) throws SQLException {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        long idVideo = Long.parseLong(request.getParameter("id"));
        videoService.delete(conecxaoMySQL, idVideo);
    }

    private void insert(Connection conecxaoMySQL, MongoCollection<org.bson.Document> conexaoMongoDB, HttpServletRequest request, HttpServletResponse response, Map saida) throws IOException, ServletException, SQLException, Exception {
        Part filePart = request.getPart("video");
        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Nenhum vídeo enviado.");
        }

        InputStream conteudo = request.getPart("video").getInputStream();
        byte[] videoBytes = conteudo.readAllBytes();
        Video video = new Video(videoBytes);

        IdVideo idVideo = new IdVideo();
        idVideo.setName("---");
        idVideo.setDateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        idVideo.setLength(videoBytes.length);

        VideoDao.insert(conecxaoMySQL, video, idVideo);
    }

    private Map find(HttpServletResponse response, Connection conecxaoMySQL, MongoCollection<Document> conexaoMongoDB, Map saida) throws SQLException {
        ArrayList<IdVideo> lista = idVideoService.find(conecxaoMySQL, conexaoMongoDB);
        saida.put("record", lista);
        saida.put("total", lista.size());
        return saida;
    }

//    private void findById(Connection conecxaoMySQL, HttpServletRequest request, HttpServletResponse response, Map saida, boolean isBinaryResponse) throws SQLException, IOException, InterruptedException {
    //    if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
    //        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    //        System.out.println("step de  erro id");
    //        throw new RuntimeException("id vazia");
    //    };
    //    int idVideo = Integer.parseInt(request.getParameter("id"));
    //    Video video = VideoDao.findById(conecxaoMySQL, idVideo);
    //    File tempFile = VideoDao.salvarVideoTemporario(video);
    //    if (Desktop.isDesktopSupported()) {
    //        Desktop.getDesktop().open(tempFile);
    //    } else {
    //        System.out.println("Desktop não suportado neste sistema.");
    //    };
//        Thread.sleep(5000); // Espera 5 segundos (ajustável)
//        tempFile.delete();;
//    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
