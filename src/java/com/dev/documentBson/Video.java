package com.dev.documentBson;

import org.bson.Document;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 21/07/2018
 * @version 1
 */
public class Video {

    private Document documento;
    private String idTable;
    // CHAVE/S
    private String keyContent;
    // VALOR/S
    private String content;

    public Video(String content) {
        this.keyContent = "content";
        this.content = content;
        this.documento = new Document(this.keyContent, this.content);
    }

    public Document getDocumento() {
        return documento;
    }

    public void setDocumento(Document documento) {
        this.documento = documento;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Video{" + "documento=" + documento + ", idTable=" + idTable + ", keyContent=" + keyContent + ", content=" + content + '}';
    }
    
    

}
