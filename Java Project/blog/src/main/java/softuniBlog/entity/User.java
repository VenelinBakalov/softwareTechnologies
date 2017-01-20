package softuniBlog.entity;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private Set<Role> roles;

    private Set<Article> articles;

    private Set<Video> authorVideos;

    private Set<Video> cameramanVideos;

    private Position position;

    private String imagePath;

    private Set<Comment> comments;

    private Set<Article> articlesLiked;

    public User(String email, String fullName, String password) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;

        this.roles = new HashSet<>();
        this.articles = new HashSet<>();
        this.authorVideos = new HashSet<>();
        this.cameramanVideos = new HashSet<>();
        this.comments = new HashSet<>();
        this.articlesLiked = new HashSet<>();
    }

    public User() {    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullName", nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @OneToMany(mappedBy = "author")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @ManyToOne()
    @JoinColumn(name = "positionId")
    public Position getPosition() {
        return position;
    }

    @OneToMany(mappedBy = "author")
    public Set<Video> getAuthorVideos() {
        return authorVideos;
    }

    public void setAuthorVideos(Set<Video> authorVideos) {
        this.authorVideos = authorVideos;
    }

    @OneToMany(mappedBy = "cameraman")
    public Set<Video> getCameramanVideos() {
        return cameramanVideos;
    }

    public void setCameramanVideos(Set<Video> videos) {
        this.cameramanVideos = cameramanVideos;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Column(name = "imagePath")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @OneToMany(mappedBy = "author")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @ManyToMany(mappedBy = "usersLiked")
    public Set<Article> getArticlesLiked() {
        return articlesLiked;
    }

    public void setArticlesLiked(Set<Article> articlesLiked) {
        this.articlesLiked = articlesLiked;
    }

    @Transient
    public boolean isAdmin() {
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Article article) {
        return Objects.equals(this.getId(), article.getAuthor().getId());
    }

    @Transient
    public boolean isAuthor(Video video) {
        return Objects.equals(this.getId(), video.getAuthor().getId());
    }

    @Transient
    public boolean isAuthor(Comment comment) {
        return Objects.equals(this.getId(), comment.getAuthor().getId());
    }

    @Transient
    public boolean hasLiked(Article article) {
        return this.getArticlesLiked().contains(article);
    }
}