package com.bootifulmicropizza.gateway.website.facade;

import com.bootifulmicropizza.gateway.website.model.Catalogue;
import com.bootifulmicropizza.gateway.website.model.Product;
import com.bootifulmicropizza.gateway.website.service.CatalogueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.bootifulmicropizza.gateway.website.facade.CatalogueFacade.PRODUCTS_LINK_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.when;

/**
 * Test to test the {@link CatalogueFacade} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class CatalogueFacadeTest {

    @InjectMocks
    private CatalogueFacade catalogueFacade;

    @Mock
    private CatalogueService catalogueService;

    @Mock
    private OAuth2RestTemplate template;

    @Test
    public void test() {
        // Given
        mockGetCatalogues();
        mockGetProducts();
        mockGetProduct();

        // When
        final Catalogue catalogue = catalogueFacade.getCatalogueByCatalogueId("pizzas");

        // Then
        assertThat(catalogue, notNullValue());
    }

    private void mockGetCatalogues() {
        final Resource<Catalogue> catalogueResource = createCatalogue();
        when(catalogueService.getCatalogueByCatalogueId("pizzas")).thenReturn(catalogueResource);
    }

    private void mockGetProducts() {
        Product product = createProduct();
        Link link = new Link("http://inventory-service/products/123/", "self");
        final Resource<Product> productResource = new Resource<>(product, link);

        Resources<Resource<Product>> productResources = new Resources(Arrays.asList(productResource));
        ResponseEntity<Resources<Resource<Product>>> response = ResponseEntity.ok(productResources);

        when(template.exchange("http://inventory-service/products/123/", HttpMethod.GET, null,
                               new ParameterizedTypeReference<Resources<Resource<Product>>>() {}, Collections.emptyMap()))
            .thenReturn(response);
    }

    private void mockGetProduct() {
        Product product = createProduct();
        Link link = new Link("http://inventory-service/products/123/", "self");
        final Resource<Product> productResource = new Resource<>(product, link);

        ResponseEntity<Resource<Product>> response = ResponseEntity.ok(productResource);

        when(template.exchange("http://inventory-service/products/123/", HttpMethod.GET, null,
                               new ParameterizedTypeReference<Resource<Product>>() {}, Collections.emptyMap()))
            .thenReturn(response);
    }

    private Resource<Catalogue> createCatalogue() {
        final Catalogue catalogue = new Catalogue();
        catalogue.setName("Test Catalogue");

        final Set<Product> products = new HashSet<>();
        products.add(createProduct());
        catalogue.setProducts(products);

        final Link link = new Link("http://inventory-service/products/123/", PRODUCTS_LINK_NAME);
        final Resource<Catalogue> resource = new Resource<>(catalogue, link);

        return resource;
    }

    private Product createProduct() {
        final Product product = new Product();
        product.setName("Test Product");
        product.setProductId("123");

        return product;
    }
}