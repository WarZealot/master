/**
 *
 */
package tka.binding.dropbox;

/**
 * @author Konstantin
 *
 */
public class DropboxUploadEntity {

    private String directory;
    private String mediaUrl;

    public DropboxUploadEntity() {
    }

    public DropboxUploadEntity(String directory, String mediaUrl) {
        this.directory = directory;
        this.mediaUrl = mediaUrl;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

}
