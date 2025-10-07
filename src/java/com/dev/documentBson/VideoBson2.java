package com.dev.documentBson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 28/09/2018
 * @version 2 beta
 */
public class VideoBson2 {

    private ObjectId id;
    private Document documento;
    private Long idVideo;
    private String name;
    private Date dateTime;
    private long length;
    private byte[] content;
    private String email;

    public VideoBson2() {
    }

    public VideoBson2(SequenceGenerator seqGen, String name, long length, byte[] content, String email) {
        this.idVideo = seqGen.getNextSequence("id_videos");
        this.name = name;
        this.length = length;
        this.content = content;
        this.email = email;

        this.documento = new Document()
                .append("id_video", idVideo)
                .append("name", name)
                .append("date_time", Date.from(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).atZone(ZoneId.of("America/Sao_Paulo")).toInstant()))
                .append("length", length)
                .append("content", content)
        .append("email", email);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
        return "VideoBson2{" + "id=" + id + ", documento=" + documento + ", idVideo=" + idVideo + ", name=" + name + ", dateTime=" + dateTime + ", length=" + length + ", content=" + content + ", email=" + email + '}';
    }
    
}
