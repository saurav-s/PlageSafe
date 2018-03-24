package com.phasec.plagsafe.upload;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.phasec.plagsafe.ComparisonService;
import com.phasec.plagsafe.RestUploadController;
import com.phasec.plagsafe.StorageService;
import com.phasec.plagsafe.objects.Report;

/**
 * test cases for RestUploadController
 * @author sanketsaurav
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(RestUploadController.class)
public class RestUploadControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StorageService storageService;

	@MockBean
	private ComparisonService comparisonService;
	
	@Mock (name = "files")
	private List<String> filesMock;

	/**
	 * test file upload success scenario
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFileUploadSuccess() throws Exception {

		List<Report> reports = new ArrayList<>();
		Report rep = new Report("file1", "file2", 80, "test remark");
		reports.add(rep);
		Gson gson = new Gson();
		String reportsJson = gson.toJson(reports);
		when(comparisonService.runComparisionForFiles(anyList())).thenReturn(reports);
		when(storageService.getFile(anyString())).thenReturn(new File(""));

		MockMultipartFile firstFile = new MockMultipartFile("uploadfile1", "sample1.py", "text/plain",
				"some xml".getBytes());
		MockMultipartFile secondFile = new MockMultipartFile("uploadfile2", "", "sample2.py",
				"some other type".getBytes());
		MockMultipartFile thirdFile = new MockMultipartFile("uploadfile3", "", "sample3.py",
				"some more other type".getBytes());

		mvc.perform(MockMvcRequestBuilders
				.fileUpload("/api/uploadfile")
				.file(firstFile).file(secondFile).file(thirdFile)
				.param("some-random", "4"))
				.andExpect(status().is(200)).andExpect(content().json(reportsJson));
	}
	
	/**
	 * test file upload fail scenario
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testFileUploadFail() throws Exception {

		List<Report> reports = new ArrayList<>();
		Report rep = new Report("file1", "file2", 80, "test remark");
		reports.add(rep);
		Gson gson = new Gson();
		String response = gson.toJson("Error occured while uploading the files");
		when(comparisonService.runComparisionForFiles(anyList())).thenThrow(FileNotFoundException.class);
		when(storageService.getFile(anyString())).thenReturn(new File(""));

		MockMultipartFile firstFile = new MockMultipartFile("uploadfile1", "sample1.py", "",
				"some xml".getBytes());
		MockMultipartFile secondFile = new MockMultipartFile("uploadfile2", "sample2.py"," text/plain",
				"some other type".getBytes());
		MockMultipartFile thirdFile = new MockMultipartFile("uploadfile3", "sample3.py","text/plain",
				"some more other type".getBytes());

		mvc.perform(MockMvcRequestBuilders
				.fileUpload("/api/uploadfile")
				.file(firstFile).file(secondFile).file(thirdFile)
				.param("some-random", "4"))
				.andExpect(status().is(200)).andExpect(content().json(response));
	}
	
	/**
	 * test get file success
	 * @throws Exception
	 */
	@Test
	public void testGetFilesSuccess() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("uploadfile1", "sample1.py", "",
				"some xml".getBytes());
		MockMultipartFile secondFile = new MockMultipartFile("uploadfile2", "sample2.py"," text/plain",
				"some other type".getBytes());
		MockMultipartFile thirdFile = new MockMultipartFile("uploadfile3", "sample3.py","text/plain",
				"some more other type".getBytes());

		mvc.perform(MockMvcRequestBuilders
				.fileUpload("/api/uploadfile")
				.file(firstFile).file(secondFile).file(thirdFile)
				.param("some-random", "4"))
				.andExpect(status().is(200));

		List<String> uriList = new ArrayList<>();
		uriList.add("sampleURIPath");

		mvc.perform(get("/api/getallfiles")
		 .contentType(MediaType.APPLICATION_JSON))
		 .andExpect(status().isOk());
		 
	}
	


}