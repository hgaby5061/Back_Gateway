package com.service.web.app.busqueda.mod.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.service.web.app.busqueda.mod.models.Document;
import com.service.web.app.busqueda.mod.utils.Services;

@Service
public class CallServicesImpl implements CallServices {

	@Autowired
	private RestTemplate restClient;

	@Override
	public String extractText(List<MultipartFile> files, Map<String, String> modif, Map<String, String> author) {
		Services service = new Services();

		if (files.size() > 0) {
			try {
				String documents = service.consumeTika(files, modif, author, restClient);
				return documents;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String search(String query) {
		System.out.println(query + " dentro de impl call");
		Services service = new Services();
		try {
			String docSearch = service.searchSolr(query, restClient);
			System.out.println("BUSCAR SOLR");
			// Parse the response to check for errors
			if (docSearch != null) {
				// Assuming the response is a JSON string
				if (docSearch.contains("\"error\"")) {
					return "Error: " + docSearch; // Return the error message
				}
				return docSearch; // Return the valid response
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTopics(List<String> ids) {
		System.out.println(ids + " dentro de impl call");
		Services service = new Services();
		try {
			String docSearch = service.getTopics(ids, restClient);
			System.out.println("toppic SOLR");
			if (docSearch.contains("\"error\"")) {
				return "Error: " + docSearch; // Return the error message
			}
			return docSearch; // Return the valid response

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String autoComplete(String query) {
		Services service = new Services();
		System.out.println("Entra a llamar a complete con restClient");
		String tags = service.completeSolr(query, restClient);
		System.out.println("TAGS SOLR" + tags);
		return tags;
	}

	@Override
	public String unanalyzed() {
		Services service = new Services();
		System.out.println("Entra a llamar a complete con restClient");
		String docs_unanalyzed = service.getUnanalyzed(restClient);
		return docs_unanalyzed;
	}

	@Override
	public String indexSolr(List<Object> docs) {
		Services service = new Services();
		String docindex = service.indexSolr(docs, restClient);
		System.out.println("INDEXAR SOLR" + docindex);
		return docindex;
	}

	private List<Object> toList(List<ResponseEntity<Object[]>> lista) {
		List<Object> documents = new ArrayList<>();
		ResponseEntity<Object[]> res = lista.get(0);
		for (int i = 0; i < res.getBody().length; i++) {
			documents.add(res.getBody()[i]);
		}
		return documents;
	}
}
