package com.cassinisys.erp.ui;

import com.cassinisys.erp.ui.security.model.NavigationItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

/**
 * Created by reddy on 9/11/15.
 */
public class NavigationMenuTest {
    private String navFile = "/Users/reddy/MyHome/Dev/cassinisys/cassinisys.apps/cassini.erp/src/main/webapp/app/components/main/navigation.json";

    @Test
    public void testNavigation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<NavigationItem> items = mapper.readValue( new FileInputStream(navFile),
                new TypeReference<List<NavigationItem>>() {});

        System.out.println(items);
    }
}
