package com.miller.smartscheduler.security;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

  private final Map<String, String> customHeaders = new HashMap<>();

  public MutableHttpServletRequest(HttpServletRequest request) {
    super(request);
  }

  public void putHeader(String name, String value) {

    this.customHeaders.put(name, value);
  }

  @Override
  public String getHeader(String name) {

    return customHeaders.getOrDefault(name, super.getHeader(name));
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    List<String> values = Collections.list(super.getHeaders(name));

    if (customHeaders.containsKey(name)) {
      values.add(customHeaders.get(name));
    }

    return Collections.enumeration(values);
  }
}
