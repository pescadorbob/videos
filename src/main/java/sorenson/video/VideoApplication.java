package sorenson.video;

import io.dropwizard.Application;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import sorenson.video.db.VideoDAO;
import sorenson.video.domain.Video;
import sorenson.video.resources.VideoResource;


public class VideoApplication extends Application<VideoConfiguration> {
    public static void main(String[] args) throws Exception {
        new VideoApplication().run(args);
    }

    private final HibernateBundle<VideoConfiguration> hibernate = new HibernateBundle<VideoConfiguration>(Video.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(VideoConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<VideoConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<VideoConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(VideoConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernate);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void run(VideoConfiguration configuration,
                    Environment environment) {

        VideoDAO videoDAO = new VideoDAO(hibernate.getSessionFactory());
        final VideoResource videoResource = new VideoResource(videoDAO);
        environment.jersey().register(videoResource);
    }

}