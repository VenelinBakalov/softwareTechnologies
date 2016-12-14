package softuniBlog.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "videos")
public class Video {

    private Integer id;

    private String title;

    private String url;

    private Article article;

    private User author;

    private User cameraman;

    private Date dateAdded;

    public Video() {
    }

    public Video(String title, String url, User author, User cameraman, Article article) {
        this.title = title;
        this.url = url;
        this.author = author;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false, unique = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "articleId")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
