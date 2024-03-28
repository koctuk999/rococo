package guru.qa.rococo.service;

import com.google.protobuf.Empty;
import guru.qa.grpc.rococo.grpc.*;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@GrpcService
public class GrpcCountryService extends RococoCountryServiceGrpc.RococoCountryServiceImplBase {
    private final CountryRepository countryRepository;

    @Autowired
    public GrpcCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getCountries(CountriesRequest request, StreamObserver<CountriesResponse> responseObserver) {
        Page<CountryEntity> countryEntities = countryRepository.findAll(PageRequest.of(request.getPage(), request.getSize()));
        List<Country> countries = countryEntities
                .stream()
                .map(CountryEntity::toGrpc)
                .toList();
        responseObserver.onNext(
                CountriesResponse
                        .newBuilder()
                        .addAllCountry(countries)
                        .setTotalCount(countryEntities.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getCountryById(CountryByIdRequest request, StreamObserver<Country> responseObserver) {
        responseObserver.onNext(
                countryRepository.findById(
                        UUID.fromString(request.getId())
                ).get().toGrpc()
        );
        responseObserver.onCompleted();
    }
}
