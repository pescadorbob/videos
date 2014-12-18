package sorenson.video.db;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import sorenson.video.domain.Video;

import java.util.List;

public class VideoDAO extends AbstractDAO<Video> {
    public VideoDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Video> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Video create(Video Video) {
        return persist(Video);
    }

    public List<Video> findAll() {
        return list(namedQuery("sorenson.video.domain.Video.findAll"));
    }

    public void delete(Long id){
        this.currentSession().delete(get(id));
    }
}
