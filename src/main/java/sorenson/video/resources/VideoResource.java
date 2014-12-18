package sorenson.video.resources;


import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import sorenson.video.db.VideoDAO;
import sorenson.video.domain.Video;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/video")
@Produces(MediaType.APPLICATION_JSON)
public class VideoResource {

    private final VideoDAO videoDAO;

    public VideoResource(VideoDAO videoDAO) {
        this.videoDAO = videoDAO;
    }

    @Path("{videoId}")
    @GET
    @UnitOfWork
    public Video getVideo(@PathParam("videoId") LongParam videoId) {
        return findSafely(videoId.get());
    }

    @Path("{videoId}")
    @DELETE
    @UnitOfWork
    public void deleteVideo(@PathParam("videoId") LongParam videoId) {
        Video v = findSafely(videoId.get());
        videoDAO.delete(v.getId());
    }

    @POST
    @UnitOfWork
    public Video createVideo(Video video) {
        return videoDAO.create(video);
    }

    @GET
    @UnitOfWork
    public List<Video> listVideos() {
        return videoDAO.findAll();
    }

    private Video findSafely(long videoId) {
        final Optional<Video> video = videoDAO.findById(videoId);
        if (!video.isPresent()) {
            throw new NotFoundException("No such video.");
        }
        return video.get();
    }
}
