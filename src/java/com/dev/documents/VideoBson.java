package com.dev.documents;

import java.io.Serializable;
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
    private long length;
    private String email;

    public VideoBson() {
    }
    
    public VideoBson(SequenceGenerator seqGen, String name, long length, String email) {
        this.idVideo = seqGen.getNextSequence("id_videos");
        this.name = name;
        this.length = length;
        this.email = email;
        this.documento = new Document("id_video", this.idVideo)
                .append("name", this.name)
                .append("length", this.length)
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

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VideoBson{" + "documento=" + documento + ", id=" + id + ", idVideo=" + idVideo + ", name=" + name + ", length=" + length + ", email=" + email + '}';
    }
}
