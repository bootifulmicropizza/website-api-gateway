package com.bootifulmicropizza.gateway.website.rest.v1;

import com.bootifulmicropizza.gateway.website.facade.CatalogueFacade;
import com.bootifulmicropizza.gateway.website.model.Catalogue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/catalogues", produces = "application/json")
public class CatalogueRestController {

    public static final String PIZZAS_CATALOGUE_NAME = "pizzas";

    public static final String SIDES_CATALOGUE_NAME = "sides";

    public static final String DESSERTS_CATALOGUE_NAME = "desserts";

    public static final String DRINKS_CATALOGUE_NAME = "drinks";

    private CatalogueFacade catalogueFacade;

    public CatalogueRestController(final CatalogueFacade catalogueFacade) {
        this.catalogueFacade = catalogueFacade;
    }

    @RequestMapping(value = "/pizzas", method = RequestMethod.GET)
    public ResponseEntity<Catalogue> getPizzaCatalogue() {
        final Catalogue catalogue = catalogueFacade.getCatalogueByCatalogueId(PIZZAS_CATALOGUE_NAME);

        if (catalogue == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(catalogue);
    }

    @RequestMapping(value = "/sides", method = RequestMethod.GET)
    public ResponseEntity<Catalogue> getSidesCatalogue() {
        final Catalogue catalogue = catalogueFacade.getCatalogueByCatalogueId(SIDES_CATALOGUE_NAME);

        if (catalogue == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(catalogue);
    }

    @RequestMapping(value = "/desserts", method = RequestMethod.GET)
    public ResponseEntity<Catalogue> getDessertsCatalogue() {
        final Catalogue catalogue = catalogueFacade.getCatalogueByCatalogueId(DESSERTS_CATALOGUE_NAME);

        if (catalogue == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(catalogue);
    }

    @RequestMapping(value = "/drinks", method = RequestMethod.GET)
    public ResponseEntity<Catalogue> getDrinksCatalogue() {
        final Catalogue catalogue = catalogueFacade.getCatalogueByCatalogueId(DRINKS_CATALOGUE_NAME);

        if (catalogue == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(catalogue);
    }
}
