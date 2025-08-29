package com.iaai.chaoxing.chaoxingsignweb.service;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class vpnHttp {

    // 存储最后一次请求返回的所有 Cookie 头
    private Map<String, List<String>> lastResponseHeaders;

    // 存储合并后的完整 Cookie 字符串
    @Getter
    private String mergedCookies;

    public String GET(String url, boolean isReturnCookie) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            // 如果 mergedCookies 不为空，添加到请求头
            if (mergedCookies != null && !mergedCookies.isEmpty()) {
                con.setRequestProperty("Cookie", mergedCookies);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 记录所有响应头
                this.lastResponseHeaders = con.getHeaderFields();

                // 合并所有 Cookie
                this.mergedCookies = mergeCookiesFromHeaders();

                return response.toString();
            } else {
                return "网络请求失败";
            }
        } catch (Exception e) {
            return "数据请求异常";
        }
    }

    public String POST(String url, String requestBody, String contentType) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", contentType);

            // 如果 mergedCookies 不为空，添加到请求头
            if (mergedCookies != null && !mergedCookies.isEmpty()) {
                con.setRequestProperty("Cookie", mergedCookies);
            }

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 记录所有响应头
                this.lastResponseHeaders = con.getHeaderFields();

                // 合并所有 Cookie
                this.mergedCookies = mergeCookiesFromHeaders();

                return response.toString();
            } else {
                return "网络请求失败";
            }
        } catch (Exception e) {
            return "数据请求异常";
        }
    }

    /**
     * 从响应头中合并所有 Cookie
     */
    String mergeCookiesFromHeaders() {
        if (lastResponseHeaders == null) {
            return null;
        }

        StringBuilder cookieBuilder = new StringBuilder();

        // 获取所有的 Set-Cookie 头
        List<String> setCookieHeaders = lastResponseHeaders.get("Set-Cookie");
        if (setCookieHeaders != null && !setCookieHeaders.isEmpty()) {
            for (String cookieHeader : setCookieHeaders) {
                // 提取 name=value 部分（去掉 Path, HttpOnly, Secure 等属性）
                String[] cookieParts = cookieHeader.split(";");
                if (cookieParts.length > 0) {
                    String nameValue = cookieParts[0].trim();
                    if (!nameValue.isEmpty()) {
                        if (!cookieBuilder.isEmpty()) {
                            cookieBuilder.append(";");
                        }
                        cookieBuilder.append(nameValue);
                    }
                }
            }
        }

        return !cookieBuilder.isEmpty() ? cookieBuilder.toString() : null;
    }

    /**
     * 获取所有响应头（用于调试）
     */
    public Map<String, List<String>> getAllHeaders() {
        return this.lastResponseHeaders;
    }

    /**
     * 获取特定的 Cookie 值
     */
    public String getCookieValue(String cookieName) {
        if (mergedCookies == null) {
            return null;
        }

        String[] cookies = mergedCookies.split(";");
        for (String cookie : cookies) {
            String[] parts = cookie.split("=", 2);
            if (parts.length == 2 && parts[0].trim().equals(cookieName)) {
                return parts[1].trim();
            }
        }
        return null;
    }
}