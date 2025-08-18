package com.dev.model;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public class Video {
    
    private long idTable;
    private byte[] contentBytes;
    private long idVideo;

    public Video() {
    }

    public Video(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public long getIdTable() {
        return idTable;
    }

    public void setIdTable(long idTable) {
        this.idTable = idTable;
    }


    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(long idVideo) {
        this.idVideo = idVideo;
    }

    @Override
    public String toString() {
        return "Video{" + "idTable=" + idTable + ", contentBytes=" + contentBytes + ", idVideo=" + idVideo + '}';
    }    
}
