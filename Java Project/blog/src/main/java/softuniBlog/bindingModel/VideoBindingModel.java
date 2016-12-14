package softuniBlog.bindingModel;


import javax.validation.constraints.NotNull;

public class VideoBindingModel {

    @NotNull
    private String title;

    @NotNull
    private String fullUrl;

    private Integer articleId;

    private Integer cameramanId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getCameramanId() {
        return cameramanId;
    }

    public void setCameramanId(Integer cameramanId) {
        this.cameramanId = cameramanId;
    }
}
