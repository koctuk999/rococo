package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.AllArtistsRequest;
import guru.qa.grpc.rococo.grpc.AllArtistsResponse;
import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.GetArtistRequest;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.RococoArtistServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import io.grpc.Channel;
import io.qameta.allure.Step;

import java.util.List;
import java.util.Optional;

import static guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.newBlockingStub;
import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcArtistClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel artistChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.artistGrpcHost(), CFG.artistGrpcPort()));

    private static final RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub = newBlockingStub(artistChannel);
    private AllArtistsResponse getAllArtists(Integer size, Integer page) {
        return rococoArtistServiceBlockingStub.getAllArtists(
                AllArtistsRequest
                        .newBuilder()
                        .setSize(size)
                        .setPage(page)
                        .build()
        );
    }

    @Step("Find artist {0} in list with pagination")
    public Optional<Artist> findArtistInList(String artistId) {
        int page = 0;
        while (true) {
            AllArtistsResponse allArtists = getAllArtists(10, page);
            if (allArtists.getArtistList() == null) {
                return Optional.empty();
            }

            Optional<Artist> foundArtist = allArtists.getArtistList()
                    .stream()
                    .filter(artist -> artist.getId().equals(artistId))
                    .findFirst();

            if (foundArtist.isPresent()) {
                return foundArtist;
            }
            page++;
        }
    }

    public Artist addArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.addArtist(artist);
    }

    public Artist getArtistById(String id) {
        return rococoArtistServiceBlockingStub.getArtistById(
                GetArtistRequest
                        .newBuilder()
                        .setId(id)
                        .build()
        );
    }

    public Artist updateArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.updateArtist(artist);
    }

}
