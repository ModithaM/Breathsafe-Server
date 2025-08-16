package com.itp.breathsafe.common.constant;

public class Constants {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**"
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
