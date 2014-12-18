package sorenson.video.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "videos")
@NamedQueries({
        @NamedQuery(
                name = "sorenson.video.domain.Video.findAll",
                query = "SELECT v FROM Video v"
        )
})
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "producer", nullable = false)
    private String producer;

    @Column(name = "actors", nullable = false)
    private String actors;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Video)) return false;

        final Video that = (Video) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.producer, that.producer) &&
                Objects.equals(this.actors, that.actors) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, producer, actors, description);
    }
}
