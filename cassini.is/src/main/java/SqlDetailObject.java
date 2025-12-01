/**
 * The Class is for SqlDetailObject
 **/
public class SqlDetailObject {

    private String groupId;

    private String artifactId;

    private String version;

    private Integer sequence;

    public SqlDetailObject() {
    }

    public SqlDetailObject(String groupId, Integer sequence, String artifactId, String version) {
        this.groupId = groupId;
        this.sequence = sequence;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * The methods Getters and Setters are used to get and set all the values for SqlDetailObject Class
     **/
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
