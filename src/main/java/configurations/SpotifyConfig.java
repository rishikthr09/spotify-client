package configurations;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;

@Configuration
public class SpotifyConfig
{

    private static final String clientId = "";
    private static final String clientSecret = "";
    private static final URI authRedirectUri = SpotifyHttpManager.makeUri("");
    private static final URI accessRedirectUri = SpotifyHttpManager.makeUri("");



    @Bean
    public SpotifyApi spotifyApi()
    {

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(authRedirectUri)
                .build();

        return spotifyApi;
    }

    @Bean
    public SpotifyApi spotifyAccessApi()
    {

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(accessRedirectUri)
                .build();

        return spotifyApi;
    }

}
