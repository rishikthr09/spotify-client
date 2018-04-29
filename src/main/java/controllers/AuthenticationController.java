package controllers;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import configurations.SpotifyConfig;

@RestController
@Import({SpotifyConfig.class})
public class AuthenticationController
{
    
    @Autowired
    private SpotifyApi spotifyApi;

    @GetMapping("/getauthcodeuri")
    public ModelAndView getAuthCodeURI(HttpServletResponse httpServletResponse) throws IOException
    {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("tempstate")
                .scope("user-read-birthdate,user-read-email")
                .show_dialog(true)
                .build();

        URI uri = authorizationCodeUriRequest.execute();

        return new ModelAndView("redirect:" + uri.toString());

    }

    @GetMapping("/authcallback")
    public ModelAndView authuricallback(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "error", required = false) String error) throws SpotifyWebApiException, IOException
    {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

        return new ModelAndView("redirect:/userinfo");

    }

    @GetMapping("/userinfo")
    public String userInfo() throws SpotifyWebApiException, IOException
    {
        User user = spotifyApi.getCurrentUsersProfile().build().execute();

        return user.getDisplayName();
    }

}
