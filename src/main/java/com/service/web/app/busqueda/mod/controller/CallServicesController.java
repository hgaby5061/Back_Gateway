package com.service.web.app.busqueda.mod.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.service.web.app.busqueda.mod.models.Document;
import com.service.web.app.busqueda.mod.service.CallServices;

@RestController
@RequestMapping("/module")

public class CallServicesController {
	@Autowired
	private CallServices services;

	@PostMapping("/extract")
	public ResponseEntity<String> callModule(@RequestPart("files") List<MultipartFile> file,
			@RequestParam Map<String, String> otherParameters, @RequestParam Map<String, String> author) {

		if (file.size() > 0) {
			// System.out.println(otherParameters);
			String resp = services.extractText(file, otherParameters, author);
			if (resp != null) {
				System.out.println("ENTRO A SEARCH");
				// System.out.println(resp);
				return ResponseEntity.ok(resp);
			}
			return ResponseEntity.badRequest().body(resp);
		}
		return ResponseEntity.badRequest().body("No files provided. Please upload files.");
	}

	@PostMapping("/indexing")
	public ResponseEntity<String> callSolr(@RequestBody List<Object> file) {

		// System.out.println(otherParameters);
		String resp = services.indexSolr(file);
		if (resp != null) {
			System.out.println("ENTRO A SEARCH");
			return ResponseEntity.ok(resp);
		}
		return ResponseEntity.badRequest().build();

		// return ResponseEntity.badRequest().body(Collections.singletonList("No files
		// provided. Please upload files."));
	}

	@GetMapping("/get-unanalyzed")
	public ResponseEntity<String> getUnanalyzed() {

		String resp = services.unanalyzed();
		if (resp != null) {
			System.out.println("ENTRO A SEARCH");
			return ResponseEntity.ok(resp);
		}
		return ResponseEntity.badRequest().build();

	}

	@GetMapping("/search")
	public ResponseEntity<String> search(@RequestParam("query") String query) {
		String docu = services.search(query);
		if (docu != null)
			return ResponseEntity.ok(docu);
		return ResponseEntity.badRequest().build(); // Nombre de la vista para mostrar
													// los resultados
	}

	@PostMapping("/get-topics")
	public ResponseEntity<String> get(@RequestBody List<String> ids) {
		String docu = services.getTopics(ids);
		if (docu != null)
			return ResponseEntity.ok(docu);
		return ResponseEntity.badRequest().build(); // Nombre de la vista para mostrar
													// los resultados
	}

	@GetMapping("/complete")
	public ResponseEntity<String> autoComplete(@RequestParam("query") String query) {
		System.out.println(query + "bBUIBIUBUI");
		try {
			String docu = services.autoComplete(query);

			if (docu != null)
				return ResponseEntity.ok(docu);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null; // Nombre de la vista para mostrar
						// los resultados
	}

}
