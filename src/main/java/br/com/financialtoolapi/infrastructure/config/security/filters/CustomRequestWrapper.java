package br.com.financialtoolapi.infrastructure.config.security.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;
import java.util.stream.Collectors;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders = new HashMap<>();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public CustomRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void putHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        if (customHeaders.get(name) == null) {
            return super.getHeader(name);
        }
        return customHeaders.get(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        final Set<String> keys = new HashSet<>(customHeaders.keySet());
        final Enumeration<String> headerNames = ((HttpServletRequest) getRequest()).getHeaderNames();

        while (headerNames.hasMoreElements()) {
            final var next = headerNames.nextElement();
            keys.add(next);
        }
        return Collections.enumeration(keys);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String value = getHeader(name);
        if (customHeaders.containsKey(name) && value != null) {
            final Set<String> set = Arrays.stream(value.split(",")).collect(Collectors.toSet());
            return Collections.enumeration(set);
        }
        return super.getHeaders(name);
    }
}
