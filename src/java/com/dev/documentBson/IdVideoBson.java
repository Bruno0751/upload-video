package com.dev.documentBson;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 07/09/2025
 * @version 1
 */
public class IdVideoBson {

    private org.bson.types.ObjectId id;
    private Document documento;
    private long idVideo;
    private String name;
    private String dateTime;
    private long length;

    public IdVideoBson() {
    }
    
    public IdVideoBson(SequenceGenerator seqGen, String name, String dateTime, long length) {
        this.idVideo = seqGen.getNextSequence("id_videos");
        this.name = name;
        this.dateTime = dateTime;
        this.length = length;
        
        this.documento = new Document()
                .append("id_video", this.idVideo)
                .append("name", this.name)
                .append("data_time", this.dateTime)
                .append("length", this.length);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(long idVideo) {
        this.idVideo = idVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public Document getDocumento() {
        return documento;
    }

    @Override
    public String toString() {
        return "IdVideoBson{" + "id=" + id + ", documento=" + documento + ", idVideo=" + idVideo + ", name=" + name + ", dateTime=" + dateTime + ", length=" + length + '}';
    }
    
}
