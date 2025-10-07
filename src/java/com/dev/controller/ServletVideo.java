package com.dev.controller;

import com.dev.dao.VideoDao;
import com.google.gson.Gson;
import com.dev.documentBson.SequenceGenerator;
import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBson2;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import com.dev.persistence.ConexaoMongoDB;
import com.dev.service.MogodbVideoService;
import com.dev.service.MogodbVideoServiceImpl;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2025
 * @version 1
 */
@WebServlet(name = "ServletVideo", urlPatterns = {"/ServletVideo"})
public class ServletVideo extends HttpServlet {

    private MogodbVideoService mogodbVideoService;

    public ServletVideo() {
        mogodbVideoService = new MogodbVideoServiceImpl();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = null;
        Connection conecxaoMySQL = null;
        com.mongodb.client.MongoDatabase conexaoMongoDB = null;
        Map saida = new HashMap();
        boolean isBinaryResponse = true;
        saida.put("status", "success");
        String json;
        try {
            conecxaoMySQL = ConecxaoMySQL.conect();
            conexaoMongoDB = ConexaoMongoDB.conect();
            try {
                String opcao = request.getParameter("opcao");
                switch (opcao) {
                    case "insert":
                        isBinaryResponse = false;
                         this.insert(conexaoMongoDB, request, response);
                        break;
                    case "find":
                        isBinaryResponse = false;
                        saida = this.findAll(conexaoMongoDB, saida);
                        break;
//                    case "findById":
//                        this.findById(conecxaoMySQL, request, response, saida, isBinaryResponse);
//                        break;
                    case "streamVideo":
                        this.streamVideo(conexaoMongoDB, request, response, saida, isBinaryResponse);
                        break;
                    case "delete":
                        isBinaryResponse = false;
                        this.delete(conexaoMongoDB, request, response);
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
            if (conexaoMongoDB != null) {
                ConexaoMongoDB.close();
            }
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
    
    private boolean streamVideo(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response, Map saida, boolean isBinaryResponse) throws IOException, SQLException {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            isBinaryResponse = false;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        response.setContentType("video/mp4");
        long idVideo = Long.parseLong(request.getParameter("id"));

        try {
            VideoBson videoBson = mogodbVideoService.streamVideo(database, idVideo);
            response.setContentLength(videoBson.getContent().length);

            try (OutputStream out = response.getOutputStream()) {
                out.write(videoBson.getContent());
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
    
    private Map findAll(com.mongodb.client.MongoDatabase database, Map saida) throws Exception {
        ArrayList<VideoBson2> lista = mogodbVideoService.findAll(database);
//        String response = Api.requestGET();
//        ArrayList<SelectVideo> lista = JsonToListGson.convert(response);

        saida.put("record", lista);
        saida.put("total", lista.size());
        return saida;
    }
    
    private void delete(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }        
//        Api.requestDELETE(Integer.parseInt(request.getParameter("id")));        
        mogodbVideoService.delete(database, Long.parseLong(request.getParameter("id")));
    }
    
    private void insert(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
        Part filePart = request.getPart("video");
        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Nenhum vídeo enviado.");
        }
        InputStream conteudo = request.getPart("video").getInputStream();
        byte[] videoBytes = conteudo.readAllBytes();

        SequenceGenerator seqGen = new SequenceGenerator(database.getCollection("counters"));
        VideoBson2 videoBson2 = new VideoBson2(
                seqGen,
                "XXX",
                videoBytes.length,
                videoBytes,
                ""
        );
        mogodbVideoService.insert(database, videoBson2);
    }
    
    
    
    

//    private boolean streamVideo(HttpServletRequest request, HttpServletResponse response, Connection conecxaoMySQL, Map saida, boolean isBinaryResponse) throws IOException {
//        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
//            isBinaryResponse = false;
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            throw new RuntimeException("id do video nao informado");
//        }
//        response.setContentType("video/mp4");
//        long idVideo = Long.parseLong(request.getParameter("id"));
//
//        try {
//            Video video = videoService.streamVideo(conecxaoMySQL, idVideo);
//            response.setContentLength(video.getContentBytes().length);
//            response.setContentLength(video.getContentBytes().length);
//
//            try (OutputStream out = response.getOutputStream()) {
//                out.write(video.getContentBytes());
//                out.flush();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                response.getWriter().write("Erro interno ao enviar o vídeo.");
//            } catch (IOException ignored) {
//            }
//        }
//        return isBinaryResponse;
//    }

//    private void delete(HttpServletRequest request, HttpServletResponse response, Connection conecxaoMySQL) throws SQLException {
//        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            throw new RuntimeException("id do video nao informado");
//        }
//        long idVideo = Long.parseLong(request.getParameter("id"));
//        videoService.delete(conecxaoMySQL, idVideo);
//    }
    
//    private void insert(Connection conecxaoMySQL, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
//        Part filePart = request.getPart("video");
//        if (filePart == null || filePart.getSize() == 0) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            throw new RuntimeException("Nenhum vídeo enviado.");
//        }
//        InputStream conteudo = request.getPart("video").getInputStream();
//        byte[] videoBytes = conteudo.readAllBytes();
//        
//        Video video = new Video(videoBytes);
//        IdVideo idVideo = new IdVideo();
//        idVideo.setName("---");
//        idVideo.setDateTime(new java.sql.Timestamp(System.currentTimeMillis()));
//        idVideo.setLength(videoBytes.length);
//
//        VideoDao.insert(conecxaoMySQL, video, idVideo);
//    }

//    private Map find(Connection conecxaoMySQL, Map saida) throws SQLException {
//        ArrayList<IdVideo> lista = videoService.find(conecxaoMySQL);
//
//        saida.put("record", lista);
//        saida.put("total", lista.size());
//        return saida;
//    }

//    private void findById(Connection conzzecxaoMySQL, HttpServletRequest request, HttpServletResponse response, Map saida, boolean isBinaryResponse) throws SQLException, IOException, InterruptedException {
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
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (Exception ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
