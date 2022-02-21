package com.glim.bot;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

public class SpotifyHelper {
    private SpotifyApi spotifyApi;

    public SpotifyHelper() throws IOException, ParseException, SpotifyWebApiException {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId("CLIENT_ID")
                .setClientSecret("CLIENT_SECRET")
                .build();

        ClientCredentialsRequest.Builder request = new ClientCredentialsRequest.Builder(spotifyApi.getClientId(), spotifyApi.getClientSecret());
        ClientCredentials creds = request.grant_type("client_credentials").build().execute();
        spotifyApi.setAccessToken(creds.getAccessToken());
    }

    //1 - Western, popular songs //track_id 04gDigrS5kc9YWfZHwBETP,7qiZfU4dY1lWllzX7mPBI3 //artist_id 3fMbdgg4jU18AjLCKBhRSm,5rSXSAkZ67PYJSvpUpkOr7 //genres pop
    //2 - Chinese, popular songs //track_id 7z2M7DsEjZjwXBkWG3zd21,3kcnCi8MfkMqFKeznqEEOE //artist_id 2QcZxAgcs2I1q7CtCkl6MI,6zCAdMK7SVxKyGMnAc26Cy //genres mandopop
    //3 - Japanese, popular songs //track_id 3A4FRzgve9BjfKbvVXRIFO,04TshWXkhV1qkqHzf31Hn6 //artist_id 1EowJ1WwkMzkCkRomFhui7,1snhtMLeb2DYoMOcVbb8iB //genres j-pop
    //4 - Piano //artist_id 7y97mc3bZRFXzT2szRM4L4,4NJhFmfw43RLBLjQvxDuRS,1385hLNbrnbCJGokfH2ac2,2wOqMjp9TyABvtHdOSOTUS //genres piano
    public String getSongRecommendations(int choice) throws IOException, ParseException, SpotifyWebApiException {
        TrackSimplified track = null;
        if (choice == 1) {
            track = spotifyApi.getRecommendations()
                    .seed_artists("3fMbdgg4jU18AjLCKBhRSm,5rSXSAkZ67PYJSvpUpkOr7")
                    .seed_tracks("04gDigrS5kc9YWfZHwBETP,7qiZfU4dY1lWllzX7mPBI3")
                    .seed_genres("pop")
                    .min_popularity(70)
                    .limit(1)
                    .build().execute().getTracks()[0];
        } else if (choice == 2) {
            track = spotifyApi.getRecommendations()
                    .seed_artists("2QcZxAgcs2I1q7CtCkl6MI,6zCAdMK7SVxKyGMnAc26Cy")
                    .seed_tracks("7z2M7DsEjZjwXBkWG3zd21,3kcnCi8MfkMqFKeznqEEOE")
                    .seed_genres("mandopop")
                    .min_popularity(50)
                    .limit(1)
                    .build().execute().getTracks()[0];
        } else if (choice == 3) {
            track = spotifyApi.getRecommendations()
                    .seed_artists("1EowJ1WwkMzkCkRomFhui7,1snhtMLeb2DYoMOcVbb8iB")
                    .seed_tracks("3A4FRzgve9BjfKbvVXRIFO,04TshWXkhV1qkqHzf31Hn6")
                    .seed_genres("j-pop")
                    .min_popularity(60)
                    .limit(1)
                    .build().execute().getTracks()[0];
        } else if (choice == 4) {
            track = spotifyApi.getRecommendations()
                    .seed_artists("7y97mc3bZRFXzT2szRM4L4,4NJhFmfw43RLBLjQvxDuRS,1385hLNbrnbCJGokfH2ac2")
                    .seed_genres("piano,classical")
                    .min_popularity(40)
                    .limit(1)
                    .build().execute().getTracks()[0];
        }

        StringBuilder songDetail = new StringBuilder();
        songDetail.append(track.getName() + " - ");
        for (ArtistSimplified artist : track.getArtists()) {
            songDetail.append(artist.getName() + " ");
        }
        System.out.println(songDetail);
        return songDetail.toString();
    }
}
