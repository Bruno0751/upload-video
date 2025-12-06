package com.dev.documentBson;

import java.io.InputStream;
import org.bson.Document;

/**
 *
 * @author Bruno
 */
public class VideoBsonBig {

    private Document documento;
    private InputStream contentBig;

    public VideoBsonBig() {
    }

    public VideoBsonBig(InputStream contentBig) {
        this.contentBig = contentBig;
        this.documento = new Document()
                .append("contentBig", contentBig);
    }

    public Document getDocumento() {
        return documento;
    }

    public void setDocumento(Document documento) {
        this.documento = documento;
    }

    public InputStream getContentBig() {
        return contentBig;
    }

    public void setContentBig(InputStream contentBig) {
        this.contentBig = contentBig;
    }

    @Override
    public String toString() {
        return "VideoBsonBig{" + "documento=" + documento + ", contentBig=" + contentBig + '}';
    }

}
