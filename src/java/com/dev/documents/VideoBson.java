package com.dev.documents;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 28/09/2018
 * @version 2 beta
 */
public class VideoBson implements Serializable {

    private Document documento;
    private String id;
    private Long idVideo;
    private String name;
    private Date date;
    private long length;
    private byte[] content;
    private String base64;
    private String email;

    public VideoBson() {
    }
    
    public VideoBson(byte[] content) {
    }

    public VideoBson(SequenceGenerator seqGen, String name, long length, byte[] content, String base64, String email) {
        this.idVideo = seqGen.getNextSequence("id_videos");
        this.name = name;
        this.date = new Date();
        this.length = length;
        this.content = content;
        this.base64 = base64;
        this.email = email;
        this.documento = new Document("id_video", this.idVideo)
                .append("name", this.name)
                .append("date", this.date)
                .append("length", this.length)
                .append("content", this.content)
                .append("base64", this.base64)
                .append("email", this.email)
                ;
    }

    public Document getDocumento() {
        return documento;
    }

    public void setDocumento(Document documento) {
        this.documento = documento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VideoBson{" + "documento=" + documento + ", id=" + id + ", idVideo=" + idVideo + ", name=" + name + ", date=" + date + ", length=" + length + ", content=" + content + ", base64=" + base64 + ", email=" + email + '}';
    }

}
