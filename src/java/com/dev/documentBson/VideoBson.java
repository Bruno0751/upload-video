package com.dev.documentBson;

import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 21/07/2018
 * @version 1
 */
public class VideoBson {

    private org.bson.types.ObjectId id;
    private Document documento;
    private long idVideo;
    private byte[] content;

    public VideoBson() {
    }
    
    public VideoBson(byte[] content, long idVideo) {
        this.content = content;
        this.idVideo = idVideo;

        this.documento = new Document()
                .append("id_video", this.idVideo)
                .append("content", new Binary(this.content));
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

    public long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(long idVideo) {
        this.idVideo = idVideo;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "VideoBson{" + "id=" + id + ", documento=" + documento + ", idVideo=" + idVideo + ", content=" + content + '}';
    }

}
