package guru.qa.rococo.tests;

import guru.qa.rococo.api.grpc.GrpcArtistClient;
import guru.qa.rococo.api.grpc.GrpcCountryClient;
import guru.qa.rococo.api.grpc.GrpcMuseumClient;
import guru.qa.rococo.api.grpc.GrpcPaintingClient;
import guru.qa.rococo.core.annotations.GrpcTest;

@GrpcTest
public class BaseGrpcTest {
    protected static GrpcArtistClient grpcArtistClient = new GrpcArtistClient();
    protected static GrpcCountryClient grpcCountryClient = new GrpcCountryClient();
    protected static GrpcMuseumClient grpcMuseumClient = new GrpcMuseumClient();
    protected static GrpcPaintingClient grpcPaintingClient = new GrpcPaintingClient();
}
