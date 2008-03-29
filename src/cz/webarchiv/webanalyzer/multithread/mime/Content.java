/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread.mime;

/**
 *
 * @author praso
 */
public class Content {

    private MimeTypes mimeTypes;
    private boolean mimeTypeMagic;
    private String contentType;

    public Content() {
        this.mimeTypeMagic = false;
        this.mimeTypes = MimeTypes.get("_anal/mime-types.xml");
    }

    public String getContentType(String typeName, String url, byte[] data) {
        MimeType type = null;
        try {
            typeName = MimeType.clean(typeName);
            type = typeName == null ? null : this.mimeTypes.forName(typeName);
        } catch (MimeTypeException mte) {
        // Seems to be a malformed mime type name...
        }

        if (typeName == null || type == null || !type.matches(url)) {
            // If no mime-type header, or cannot find a corresponding registered
            // mime-type, or the one found doesn't match the url pattern
            // it shouldbe, then guess a mime-type from the url pattern
            type = this.mimeTypes.getMimeType(url);
            typeName = type == null ? typeName : type.getName();
        }
        if (typeName == null || type == null || (this.mimeTypeMagic && type.hasMagic() && !type.matches(data))) {
            // If no mime-type already found, or the one found doesn't match
            // the magic bytes it should be, then, guess a mime-type from the
            // document content (magic bytes)
            type = this.mimeTypes.getMimeType(data);
            typeName = type == null ? typeName : type.getName();
        }
        return typeName;
    }

}