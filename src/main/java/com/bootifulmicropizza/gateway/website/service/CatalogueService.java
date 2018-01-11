package com.bootifulmicropizza.gateway.website.service;

import com.bootifulmicropizza.gateway.website.model.Catalogue;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("inventory-service")
public interface CatalogueService {

    @RequestMapping(method = RequestMethod.POST, value = "/catalogues/")
    Resource<Catalogue> createCatalogue(@RequestBody Catalogue catalogue);

    @RequestMapping(method = RequestMethod.GET, value = "/catalogues/{id}/")
    Resource<Catalogue> getCatalogue(@RequestParam("id") String id);

    @RequestMapping(method = RequestMethod.PUT, value = "/catalogues/{id}/")
    Resource<Catalogue> updateCatalogue(@RequestParam("id") String id, @RequestBody Catalogue catalogue);

    @RequestMapping(method = RequestMethod.GET, value = "/catalogues/")
    Resources<Resource<Catalogue>> getCatalogues();

    @RequestMapping(method = RequestMethod.GET, value = "/catalogues/search/by-catalogue-id")
    Resource<Catalogue> getCatalogueByCatalogueId(@RequestParam("catalogueId") String catalogueId);
}
