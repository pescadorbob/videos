package sorenson.video.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import sorenson.video.db.VideoDAO;
import sorenson.video.domain.Video;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link sorenson.video.resources.VideoResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class VideoResourceTest {
    private static final VideoDAO DAO = mock(VideoDAO.class);
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new VideoResource(DAO))
            .build();

    @Captor
    private ArgumentCaptor<Video> videoCaptor;
    private Video video;

    @Before
    public void setup() {
        video = new Video();
        video.setId(1L);
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void getVideoSuccess() {
        when(DAO.findById(1L)).thenReturn(Optional.of(video));

        Video found = RULE.client().target("/video/1").request().get(Video.class);

        assertThat(found.getId()).isEqualTo(video.getId());
        verify(DAO).findById(1L);
    }

    @Test
    public void getVideoNotFound() {
        when(DAO.findById(2L)).thenReturn(Optional.<Video>absent());
        final Response response = RULE.client().target("/video/2").request().get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        verify(DAO).findById(2L);
    }

    @Test
    public void deleteVideoSuccess() {
        when(DAO.findById(1L)).thenReturn(Optional.of(video));

        final Response response = RULE.client().target("/video/1").request().buildDelete().invoke();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        verify(DAO).delete(1L);
        verify(DAO).findById(1L);
    }

    @Test
    public void deleteVideoFailure() {
        when(DAO.findById(1L)).thenReturn(Optional.<Video>absent());

        final Response response = RULE.client().target("/video/1").request().buildDelete().invoke();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        verify(DAO).findById(1L);

    }

    @Test
    public void createPerson() throws JsonProcessingException {
        when(DAO.create(any(Video.class))).thenReturn(video);
        final Response response = RULE.client().target("/video")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(video, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(DAO).create(videoCaptor.capture());
        assertThat(videoCaptor.getValue()).isEqualTo(video);
    }
    @Test
    public void listVideos() throws Exception {
        final ImmutableList<Video> videos = ImmutableList.of(video);
        when(DAO.findAll()).thenReturn(videos);

        final List<Video> response = RULE.client().target("/video")
                .request().get(new GenericType<List<Video>>() {});

        verify(DAO).findAll();
        assertThat(response).containsAll(videos);
    }


}
