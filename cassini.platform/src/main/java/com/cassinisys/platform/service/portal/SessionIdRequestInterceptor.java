package com.cassinisys.platform.service.portal;

import com.cassinisys.platform.exceptions.CassiniException;

import java.io.IOException;

public class SessionIdRequestInterceptor implements okhttp3.Interceptor {
    private String jSessionId = null;

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Request authenticatedRequest = authenticateRequest(chain.request());
        okhttp3.Response response = chain.proceed(authenticatedRequest);

        if (isAuthenticated(response)) {
            saveSessionId(response);
        } else if (jSessionId != null) {
            jSessionId = null;
            response.body().close();
            authenticatedRequest = authenticateRequest(authenticatedRequest);
            response = chain.proceed(authenticatedRequest);
        }

        if (!isAuthenticated(response)) {
            throw new CassiniException("Failed to authenticate with the portal");
        }
        return response;
    }

    private void saveSessionId(okhttp3.Response response) {
        String setCookie = response.header("Set-Cookie");
        if (setCookie != null) {
            jSessionId = setCookie.split(";")[0].split("=")[1];
        }
    }

    private boolean isAuthenticated(okhttp3.Response response) {
        return !response.request().url().encodedPath().contains("/saas/api/auth/signin");
    }

    private okhttp3.Request authenticateRequest(okhttp3.Request request) {
        okhttp3.Request.Builder builder = request.newBuilder();
        if (jSessionId != null) {
            builder.addHeader("Cookie", "JSESSIONID=" + jSessionId);
        }
        return builder.build();
    }
}
