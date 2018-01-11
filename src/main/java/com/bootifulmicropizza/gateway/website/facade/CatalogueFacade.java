package com.bootifulmicropizza.gateway.website.facade;

import com.bootifulmicropizza.gateway.website.model.Catalogue;
import com.bootifulmicropizza.gateway.website.model.Product;
import com.bootifulmicropizza.gateway.website.service.CatalogueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class CatalogueFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueFacade.class);

    private static final String UPSTREAM_SERVICE_NAME = "inventory-service";

    private static final String CACHE_NAME = "catalogues";

    protected static final String PRODUCTS_LINK_NAME = "products";

    private CatalogueService catalogueService;

    private OAuth2RestTemplate oAuth2RestTemplate;

    public CatalogueFacade(final CatalogueService catalogueService, final OAuth2RestTemplate oAuth2RestTemplate) {
        this.catalogueService = catalogueService;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    @Scheduled(fixedDelay = 86400000) // 24 hours
    public void evictCache() {
        LOGGER.info("Evicting cache " + CACHE_NAME);
    }

    @Cacheable(cacheNames = CACHE_NAME)
    public Catalogue getCatalogueByCatalogueId(final String catalogueId) {
        final Resource<Catalogue> resource = catalogueService.getCatalogueByCatalogueId(catalogueId);

        // TODO Need to add check here for when the catalogue was not found and return error response

        final Catalogue catalogue = resource.getContent();
//        final Link productsLink = resource.getLink(PRODUCTS_LINK_NAME);
//        catalogue.setProducts(getProducts(productsLink.getHref()));

        return catalogue;
    }

    private String convertUrl(String href) {
        int i = href.indexOf("//") + 2;
        int i1 = href.indexOf("/", i);

        String url = href.substring(0, i) + UPSTREAM_SERVICE_NAME + href.substring(i1);
        return url;
    }

    @Cacheable(cacheNames = CACHE_NAME)
    public Set<Product> getProducts(final String href) {
        final Set<Product> products = new HashSet<>();

        final ResponseEntity<Resources<Resource<Product>>> responseEntity =
            oAuth2RestTemplate.exchange(convertUrl(href), HttpMethod.GET, null,
                                        new ParameterizedTypeReference<Resources<Resource<Product>>>() {}, Collections
                .emptyMap());

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            final Resources<Resource<Product>> body = responseEntity.getBody();
            final Collection<Resource<Product>> bodyContent = body.getContent();
            bodyContent.forEach(productResource -> {
                products.add(productResource.getContent());
            } );
        }

        return products;
    }
}
