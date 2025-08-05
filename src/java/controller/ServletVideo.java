package controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.IdVideoDao;
import dao.VideoDao;
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
import persistence.ConecxaoMySQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import model.IdVideo;
import model.Video;
import org.bson.Document;
import persistence.ConexaoMongoDB;
import service.IdVideoService;

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
                        this.insert(conecxaoMySQL, conexaoMongoDB, request, response, saida);
                        break;
                    case "findAllId":
                        isBinaryResponse = false;
                        saida = this.findAllId(response, conecxaoMySQL, conexaoMongoDB, saida);
                        break;
                    case "findById":
                        this.findById(conecxaoMySQL, request, response, saida, isBinaryResponse);
                        break;
                    case "stream":
                        this.streamVideo(request, response, conecxaoMySQL, saida, isBinaryResponse);
                        break;
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
                System.out.println(saida);
                response.setContentType("application/json;charset=UTF-8");
                out = response.getWriter();
                json = new Gson().toJson(saida);
                response.getWriter().write(json);
                out.close();
            }
        }
    }

    private Map findAllId(HttpServletResponse response, Connection conecxaoMySQL, MongoCollection<Document> conexaoMongoDB, Map saida) throws SQLException {
        idVideoService = new IdVideoService();

        ArrayList<IdVideo> lista = idVideoService.findAll(conecxaoMySQL, conexaoMongoDB);
        saida.put("record", lista);
        saida.put("total", lista.size());
        return saida;
    }

    private void findById(Connection conecxaoMySQL, HttpServletRequest request, HttpServletResponse response, Map saida, boolean isBinaryResponse) throws SQLException, IOException, InterruptedException {
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
    }

    private boolean streamVideo(HttpServletRequest request, HttpServletResponse response, Connection conecxaoMySQL, Map saida, boolean isBinaryResponse) throws IOException, SQLException {
        if (request.getParameter("id") == null || request.getParameter("id").isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("id do video nao informado");
        }
        try {

            long idVideo = Long.parseLong(request.getParameter("id"));
            System.out.println(idVideo);
            Video video = VideoDao.findById(conecxaoMySQL, idVideo);

            if (video == null || video.getContentBytes() == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"status\":\"error\", \"msg\":\"Vídeo não encontrado\"}");
            }
            response.setContentType("video/mp4");
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

    private void insert(Connection conecxaoMySQL, MongoCollection<org.bson.Document> conexaoMongoDB, HttpServletRequest request, HttpServletResponse response, Map saida) throws IOException, ServletException, SQLException, Exception {
        Part filePart = request.getPart("video");
        if (filePart == null || filePart.getSize() == 0) {
            System.out.println("Nenhum vídeo enviado.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Nenhum vídeo enviado.");
        }
        if (!"video/mp4".equals(filePart.getContentType())) {
            System.out.println("Tipo de arquivo inválido. Apenas arquivos MP4 são permitidos.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException("Tipo de arquivo inválido. Apenas arquivos MP4 são permitidos.");
        }
//        long maxSize = 4L * 1024L * 1024L;
//        System.out.println(filePart.getSize());
//        System.out.println(maxSize);
//        System.out.println(filePart.getSize() > maxSize);
//        if (filePart.getSize() > maxSize) {
//            System.out.println("Arquivo muito grande. Máximo permitido: 4GB");
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            throw new RuntimeException("Arquivo muito grande. Máximo permitido: 4GB");
//        }

        InputStream conteudo = request.getPart("video").getInputStream();
        byte[] videoBytes = conteudo.readAllBytes();
        Video video = new Video(videoBytes);

        IdVideo idVideo = new IdVideo();
        idVideo.setName("XXX");
        idVideo.setDateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        idVideo.setLength(videoBytes.length);

        VideoDao.insert(conecxaoMySQL, video, idVideo);
        System.out.println("ok video web");
    }

    private void salvarApi(Video video, IdVideo idVideo) throws IOException {
        String boundary = "===" + System.currentTimeMillis() + "===";
        String lineSeparator = "\r\n";
        String charset = "UTF-8";

        // URL do microserviço
        URL url = null;
        try {
            url = new URL("http://localhost:9091/v1/video");
        } catch (MalformedURLException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        connection.setDoOutput(true);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            System.getLogger(ServletVideo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (DataOutputStream request = new DataOutputStream(connection.getOutputStream())) {

            // Parte 1: JSON dos metadados (campo "dados")
            String json = String.format("""
            {
                "name": "%s",
                "path": "%s"
            }
            """, idVideo.getName(), idVideo.getDateTime());

            request.writeBytes("--" + boundary + lineSeparator);
            request.writeBytes("Content-Disposition: form-data; name=\"dados\"" + lineSeparator);
            request.writeBytes("Content-Type: application/json; charset=" + charset + lineSeparator);
            request.writeBytes(lineSeparator);
            request.writeBytes(json + lineSeparator);

            // Parte 2: Arquivo (campo "file")
            request.writeBytes("--" + boundary + lineSeparator);
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"video.mp4\"" + lineSeparator);
            request.writeBytes("Content-Type: video/mp4" + lineSeparator);
            request.writeBytes(lineSeparator);
            request.write(video.getContentBytes());  // Enviando o byte[] diretamente
            request.writeBytes(lineSeparator);

            // Finaliza
            request.writeBytes("--" + boundary + "--" + lineSeparator);
            request.flush();
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Código de resposta: " + responseCode);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), charset))) {
            String line;
            System.out.println("Resposta do microserviço:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler resposta: " + e.getMessage());
        }
    }

}
