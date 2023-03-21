package com.lsecotaro.tenpo.svcchallenge.filter;

public final class SwaggerHelper {
    private SwaggerHelper() {}

    public static boolean isSwaggerPath(String uri) {
        return uri.contains("swagger") || uri.contains("api-docs");
    }
}
