package com.opuscapita.peppol.commons.template.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

public class CommonRestController {

    @RequestMapping("/api/health/check")
    public CommonRestResponse health() {
        return new CommonRestResponse("Yes, I'm alive!");
    }

    @RequestMapping("/api/list/apis")
    public List<ApiListRestResponse> list() {
        return getApiList();
    }

    @SuppressWarnings("WeakerAccess")
    protected List<ApiListRestResponse> getApiList() {
        return new ArrayList<>();
    }
}
