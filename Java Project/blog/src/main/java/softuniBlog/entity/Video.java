package softuniBlog.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "videos")
public class Video {

    private Integer id;

    private String name;

    private String url;

    private Article article;

    private User cameraman;

    private Date dateAdded;

    public Video() {
    }

    public Video(String name, String url, User cameraman, Article article) {
        this.name = name;
        this.url = url;
        this.cameraman = cameraman;
        this.article = article;
        this.dateAdded = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @ManyToOne()
    @JoinColumn(nullable = false, name = "videoId")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "cameramanId")
    public User getCameraman() {
        return cameraman;
    }

    public void setCameraman(User cameraman) {
        this.cameraman = cameraman;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
