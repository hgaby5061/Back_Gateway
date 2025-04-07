package com.service.web.app.busqueda.mod.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.service.web.app.busqueda.mod.models.Document;

public interface CallServices {

	public String extractText(List<MultipartFile> file, Map<String, String> modif, Map<String, String> author);

	public String search(String query);

	public String autoComplete(String query);

	public String unanalyzed();

	public String indexSolr(List<Object> docs);

	public String getTopics(List<String> ids);
}