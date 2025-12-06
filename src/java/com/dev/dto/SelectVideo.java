package com.dev.dto;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 29/09/2018
 * @version 1
 */
public class SelectVideo {
    
    private String id;
    private long idVideo;
    private String name;
    private String dateTime;
    private long length;
    private String email;

    @Override
    public String toString() {
        return "SelectVideo{" + "id=" + id + ", idVideo=" + idVideo + ", name=" + name + ", dateTime=" + dateTime + ", length=" + length + ", email=" + email + '}';
    }
    
}
