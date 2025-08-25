package com.itp.breathsafe.common.constant;

public class Constants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    public static final String[] ADMIN_ENDPOINTS = {
            "/api/admin/**"
    };
    public static final String[] ALL_MEMBER_ENDPOINTS = {
            "/api/users/**",
            "/api/project/**",
            "/api/member/**"
    };
}
