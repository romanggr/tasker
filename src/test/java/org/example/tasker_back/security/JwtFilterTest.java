package org.example.tasker_back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {
    private JwtFilter jwtFilter;
    private JwtService jwtService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;


    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter();
        jwtService = new JwtService();

        String secretKey = "testSecretKey";
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
        ReflectionTestUtils.setField(jwtFilter, "secretKey", secretKey);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }


    @Test
    void doFilterInternal_success() throws ServletException, IOException {
        String token = jwtService.generateToken("test@email.com");

        // Mock for HttpServletResponse
        PrintWriter writer = new PrintWriter(new StringWriter());

        when(response.getWriter()).thenReturn(writer);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_nullToken() throws ServletException, IOException {
        // Mock for HttpServletResponse
        PrintWriter writer = new PrintWriter(new StringWriter());

        when(response.getWriter()).thenReturn(writer);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
    }



    @Test
    void doFilterInternal_tokenExpired() throws ServletException, IOException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -10000L);

        String token = jwtService.generateToken("test@email.com", -10000L);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Invalid or expired token"));

        verify(filterChain, never()).doFilter(request, response);
    }



    @Test
    void shouldNotFilter_success() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        JwtFilter jwtFilter = new JwtFilter();
        ReflectionTestUtils.setField(jwtFilter, "secretKey", "testSecretKey");

        when(mockRequest.getRequestURI()).thenReturn("/api/v1/auth/login");

        assertTrue(jwtFilter.shouldNotFilter(mockRequest));
    }

    @Test
    void shouldNotFilter_fail() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        JwtFilter jwtFilter = new JwtFilter();
        ReflectionTestUtils.setField(jwtFilter, "secretKey", "testSecretKey");

        when(mockRequest.getRequestURI()).thenReturn("/api/v1/tasks");

        assertFalse(jwtFilter.shouldNotFilter(mockRequest));
    }

}
