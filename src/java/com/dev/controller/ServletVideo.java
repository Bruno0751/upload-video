package com.dev.controller;

import com.dev.api.mongodb.Api;
import com.dev.def.PropertiesReader;
import com.google.gson.Gson;
import com.dev.documentBson.SequenceGenerator;
import com.dev.documentBson.VideoBson;
import com.dev.documentBson.VideoBsonBig;
import com.dev.dto.SelectVideo;
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
import javax.servlet.http.Part;
import com.dev.persistence.ConexaoMongoDB;
import com.dev.service.VideoServiceImpl;
import com.dev.util.JsonToListGson;
import javax.servlet.annotation.MultipartConfig;
import com.dev.service.VideoService;
import java.util.Properties;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2025
 * @version 1
 */
@WebServlet(name = "ServletVideo", urlPatterns = {"/ServletVideo"})
@MultipartConfig
//@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB (tamanho em memória)
//        maxFileSize = 1024 * 1024 * 100, // 100MB por arquivo
//        maxRequestSize = 1024 * 1024 * 200 // 200MB total
//)
public class ServletVideo extends HttpServlet {

    private VideoService videoService;

    public ServletVideo() {
        videoService = new VideoServiceImpl();
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
            Properties properties = PropertiesReader.getProperties();
            conecxaoMySQL = ConecxaoMySQL.conect(properties);
            conexaoMongoDB = ConexaoMongoDB.conect(properties);
            try {
                String opcao = request.getParameter("opcao");
                switch (opcao) {
                    case "insert":
                        isBinaryResponse = false;
//                        this.inserir(conexaoMongoDB, request, response, properties);
                        this.save(conexaoMongoDB, request, response, properties);
                        break;
                    case "find":
                        isBinaryResponse = false;
                        saida = this.findAll(saida, properties);
//                        saida = this.buscarVideos(conexaoMongoDB, saida);
                        break;
                    case "streamVideo":
                        this.streamVideo(conexaoMongoDB, request, response, saida, isBinaryResponse);
                        break;
                    case "delete":
                        isBinaryResponse = false;
                        this.delete(request, response, properties);
//                        this.deletarVideo(conexaoMongoDB, request, response);
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
            VideoBson videoBson = videoService.streamVideo(database, idVideo);
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
    
    private Map findAll(Map saida, Properties properties) throws Exception {
        Api api = new Api(properties);
        String response = api.requestGET();
        ArrayList<SelectVideo> lista = JsonToListGson.convert(response);
        saida.put("record", lista);
        saida.put("total", lista.size());
        return saida;
    }
    
    private void delete(HttpServletRequest request, HttpServletResponse response, Properties properties) throws Exception {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        Api api = new Api(properties);
        api.requestDELETE(Long.parseLong(request.getParameter("id")));
    }

    private void save(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response, Properties properties) throws IOException, ServletException, Exception {
        Part filePart = request.getPart("video");
        if (request.getPart("video") == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Nenhum vídeo enviado.");
        }
        InputStream conteudo = request.getPart("video").getInputStream();
        byte[] videoBytes = conteudo.readAllBytes();
        SequenceGenerator seqGen = new SequenceGenerator(database.getCollection("counters"));
        VideoBson videoBson = new VideoBson(
                seqGen,
                "XXX",
                videoBytes.length,
                videoBytes,
                "teste@teste.com.br"
        );
        Api api = new Api(properties);
        api.saveFile(videoBson);
    }
    
    /**
     * 
     * @deprecated 
     * @see use methos findAll(Map map) 
     */
    private Map buscarVideos(com.mongodb.client.MongoDatabase database, Map saida) throws Exception {
        ArrayList<VideoBson> lista = videoService.buscarVideos(database);
        saida.put("record", lista);
        saida.put("total", lista.size());
        return saida;
    }

    /**
     * 
     * @deprecated 
     * @see use methos delete(Map map, Properties properties) 
     */
    private void deletarVideo(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        videoService.deletarVideo(database, Long.parseLong(request.getParameter("id")));
    }

    /**
     * 
     * @deprecated 
     * @see use methos insert(Map map, Properties properties) 
     */
    private void inserir(com.mongodb.client.MongoDatabase database, HttpServletRequest request, HttpServletResponse response, Properties properties) throws IOException, ServletException, Exception {
        Part filePart = request.getPart("video");
        if (request.getPart("video") == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Nenhum vídeo enviado.");
        }
        InputStream conteudo = request.getPart("video").getInputStream();
        byte[] videoBytes = conteudo.readAllBytes();

        SequenceGenerator seqGen = new SequenceGenerator(database.getCollection("counters"));
        VideoBson videoBson = new VideoBson(
                seqGen,
                "XXX",
                videoBytes.length,
                videoBytes,
                "teste@teste.com.br"
        );
        videoService.inserir(database, videoBson);
//        VideoBsonBig videoBsonBig = new VideoBsonBig(
//                conteudo
//        );
//        videoService.inserirBig(videoBsonBig);
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
