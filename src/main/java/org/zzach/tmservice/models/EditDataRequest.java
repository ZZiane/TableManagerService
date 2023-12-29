package org.zzach.tmservice.models;


import java.util.Map;

public record EditDataRequest(Map<String, String> newData, Map<String, String> oldData, long genericTableId){}