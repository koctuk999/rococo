package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.grpc.rococo.grpc.CountriesResponse;
import guru.qa.rococo.service.api.GrpcCountryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static guru.qa.rococo.utils.Hellpers.convertGrpcToJson;


@RestController
@RequestMapping("/api/country")
public class CountryController {
    private final GrpcCountryApi grpcCountryApi;

    @Autowired
    public CountryController(GrpcCountryApi grpcCountryApi) {
        this.grpcCountryApi = grpcCountryApi;
    }

    @GetMapping
    Page<JsonNode> getCountries(@PageableDefault Pageable pageable) {
        CountriesResponse countriesResponse = grpcCountryApi.getCountries(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        List<JsonNode> countriesList = countriesResponse
                .getCountryList()
                .stream()
                .map(country -> convertGrpcToJson(country))
                .toList();
        return new PageImpl<>(
                countriesList,
                pageable,
                countriesResponse.getTotalCount()
        );
    }
}
