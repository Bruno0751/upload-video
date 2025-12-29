package com.dev.documents;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.bson.Document;
import org.bson.types.Binary;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 28/09/2018
 * @version 2 beta
 */
public class VideoBson implements Serializable {

    private Document documento;
    private Long idVideo;
    private String name;
    private Date dateTime;
    private long length;
    private byte[] content;
    private String email;

    public VideoBson() {
    }

    public VideoBson(SequenceGenerator seqGen, String name, long length, byte[] content, String email) {
        this.idVideo = seqGen.getNextSequence("id_videos");
        this.name = name;
        this.length = length;
        this.content = content;
        this.email = email;
    }

    public VideoBson(byte[] content) {
        this.content = content;
    }

    public Document getDocumento() {
        return documento;
    }

    public void setDocumento(Document documento) {
        this.documento = documento;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "VideoBson{" + "documento=" + documento + ", idVideo=" + idVideo + ", name=" + name + ", dateTime=" + dateTime + ", length=" + length + ", content=" + content + ", email=" + email + '}';
    }

}
