package guru.qa.rococo.api.grpc;

import guru.qa.grpc.rococo.grpc.Artist;
import guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.RococoArtistServiceBlockingStub;
import guru.qa.rococo.api.grpc.channel.ChannelProvider;
import guru.qa.rococo.config.Config;
import io.grpc.Channel;
import org.apache.commons.lang3.tuple.Pair;

import static guru.qa.grpc.rococo.grpc.RococoArtistServiceGrpc.newBlockingStub;
import static org.apache.commons.lang3.tuple.Pair.of;

public class GrpcArtistClient {

    private static final Config CFG = Config.getInstance();

    private static final Channel artistChannel = ChannelProvider
            .INSTANCE
            .channel(of(CFG.artistGrpcHost(),CFG.artistGrpcPort()));

    private static final RococoArtistServiceBlockingStub rococoArtistServiceBlockingStub = newBlockingStub(artistChannel);

    public Artist addArtist(Artist artist) {
        return rococoArtistServiceBlockingStub.addArtist(artist);
    }
}
