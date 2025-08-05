package model;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 06/07/2018
 * @version 1
 */
public class IdVideo {
    
    private long idVideo;
    private String name;
    private java.sql.Timestamp dateTime;
    private long length;

    public IdVideo() {
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

    public java.sql.Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(java.sql.Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "IdVideo{" + "idVideo=" + idVideo + ", name=" + name + ", dateTime=" + dateTime + ", length=" + length + '}';
    }

    

   
    
}
