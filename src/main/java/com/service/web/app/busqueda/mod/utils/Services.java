package com.service.web.app.busqueda.mod.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.service.web.app.busqueda.mod.models.Document;
import com.service.web.app.busqueda.mod.models.Relations;

public class Services {

	public String consumeTika(List<MultipartFile> files, Map<String, String> time, Map<String, String> author,
			RestTemplate restClient) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		for (MultipartFile multipartFile : files) {
			// formData.add("files", toFile(multipartFile)/* multipartFile */);
			ByteArrayResource resource;
			try {
				resource = new ByteArrayResource(multipartFile.getBytes()) {
					@Override
					public String getFilename() {
						return multipartFile.getOriginalFilename();
					}
				};
				formData.add("files", resource);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			formData.add("modified", time.get("lastModified_" + multipartFile.getOriginalFilename()));
			formData.add("author", author.get("author"));
			// System.out.println(formData);
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, header);
		// List<Document> text
		// =Arrays.asList(restClient.postForObject("http://localhost:3001/text-tika",request,Document[].class));
		//
		System.out.println(request);
		// "https://backtexttika-production.up.railway.app/text-tika"
		ResponseEntity<String> response = restClient
				.postForEntity("https://fortunate-miracle-production-fffa.up.railway.app/text-tika", request,
						String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			String doc = response.getBody();
			return doc;
		}
		return null;
	}

	public List<ResponseEntity<Document[]>> consumeNer(List<Document> list, RestTemplate restClient) {
		ResponseEntity<Document[]> response = restClient.postForEntity(
				"https://backanalyze-production.up.railway.app/nlp/ner", list,
				Document[].class);

		if (response.getStatusCode().is2xxSuccessful()) {
			List<ResponseEntity<Document[]>> doc = Arrays.asList(response);
			return doc;
		}
		return null;
	}

	public List<ResponseEntity<Document[]>> consumeRelations(List<Document> list, RestTemplate restClient) {
		ResponseEntity<Document[]> response = restClient.postForEntity(
				"https://backanalyze-production.up.railway.app/nlp/relations", list,
				Document[].class);

		if (response.getStatusCode().is2xxSuccessful()) {
			List<ResponseEntity<Document[]>> doc = Arrays.asList(response);
			/*
			 * doc.get(0).getBody()[0].getRelations().stream() .map(d -> new
			 * Relations(d.getGovernor(), d.getDependent(), d.getRelation()))
			 * .collect(Collectors.toList());
			 */
			return doc;
		}
		return null;
	}

	public String indexSolr(List<Object> documents, RestTemplate restClient) {
		ResponseEntity<String> response = restClient.postForEntity("https://spring-solr.loca.lt/solr/index", documents,
				String.class);
		// System.out.println(response);
		if (response.getStatusCode().is2xxSuccessful()) {
			String string = response.getBody();
			return string;
		}
		return null;
	}

	public String searchSolr(String query, RestTemplate restClient) {
		try {
			String url = UriComponentsBuilder.fromUriString("https://spring-solr.loca.lt/solr/search")
					.queryParam("query", query).build().toUriString();
			ResponseEntity<String> response = restClient.getForEntity(url, String.class);
			System.out.println("GABY RESPONSE");
			if (response.getStatusCode().is2xxSuccessful()) {
				String string = response.getBody();
				return string;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "{\"error\": \"" + e.getMessage() + "\"}";
		}
		return null;

	}

	public String getTopics(List<String> ids, RestTemplate restClient) {
		try {
			String url = UriComponentsBuilder.fromUriString("https://spring-solr.loca.lt/solr/topics").build()
					.toUriString();
			ResponseEntity<String> response = restClient.postForEntity(url, ids, String.class);
			System.out.println("GABY RESPONSE");
			if (response.getStatusCode().is2xxSuccessful()) {
				String string = response.getBody();
				return string;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "{\"error\": \"" + e.getMessage() + "\"}";
		}
		return null;

	}

	public String getUnanalyzed(RestTemplate restClient) {
		String url = UriComponentsBuilder.fromUriString("https://spring-solr.loca.lt/solr/unanalyzed").build().toUriString();
		ResponseEntity<String> response = restClient.getForEntity(url, String.class);
		// System.out.println(response);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		}
		return null;
	}

	public String completeSolr(String query, RestTemplate restClient) {
		System.out.println("Esntro a donde llama a solr");
		String url = UriComponentsBuilder.fromUriString("https://spring-solr.loca.lt/solr/complete")
				.queryParam("query", query).build().toUriString();
		ResponseEntity<String> response = restClient.getForEntity(url, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			String string = response.getBody();
			return string;
		}
		return null;
	}

	public String graph(List<Document> list, RestTemplate restClient) {
		ResponseEntity<String> response = restClient.postForEntity(
				"https://backanalyze-production.up.railway.app/nlp/graph", list,
				String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			String string = response.getBody();
			return string;
		}
		return null;
	}

	private static FileSystemResource toFile(MultipartFile multi) {
		try {
			File fil = File.createTempFile(multi.getOriginalFilename(), null);
			// File fil=new File(multi.getOriginalFilename());
			FileCopyUtils.copy(multi.getBytes(), fil);

			// System.out.println(f.setWritable(true));
			FileSystemResource sy = new FileSystemResource(fil);
			return sy;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
