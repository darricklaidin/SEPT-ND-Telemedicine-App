package com.sept.authmicroservice.security;

public class SecurityConstant {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SECRET = "SuperFiveSecret173";
    public static final long EXPIRATION_TIME = 604800000; // 7 days
    public static final String AUTH = "api/auth/";
    public static final String SIGN_UP = "signup";
    public static final String LOGIN = "login";
    public static final String VALIDATE_JWT = "user";
}
