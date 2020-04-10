package gov.va.bip.reference.person.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import gov.va.bip.framework.s3.config.S3Properties;
import gov.va.bip.reference.person.api.model.v1.BipListObjectsResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ReferenceS3ServiceImplTest {

	private static final String BUCKET_ID_1 = "bucketId1";
	private static final String BUCKET_NAME_1 = "first-bucket-name";
	private static final String BUCKET_ENDPOINT_1 = "http://localhost:1234/bucket/bucketName1";
	private static final String BUCKET_REGION_1 = "us-east-1";
	
	private static final String EXPECTED_MESSAGE_ID = "UUID";
	private static final String EXPECTED_FILE_KEY = "fileKey";
	private static final String GENERIC_STRING = "Generic String";

	@InjectMocks
	ReferenceS3ServiceImpl instance;

	@Mock
	AmazonS3 s3Service;

	@Mock
	S3Properties s3Properties;

	@Mock
	MultipartFile mockFile;
	@Mock
	InputStream mockInputStream;

	@Mock
	ListObjectsV2Result mockListObjectsV2Result;

	@Before
	public void before() throws IOException {
		when(mockFile.getContentType()).thenReturn(GENERIC_STRING);
		when(mockFile.getSize()).thenReturn(10L);
		when(mockFile.getInputStream()).thenReturn(mockInputStream);
	}

	@Test
	public void testPutObject() {

		PutObjectResult putObjectResult = new PutObjectResult();

		when(s3Service.putObject(any())).thenReturn(putObjectResult);

		instance.putObject(BUCKET_NAME_1, EXPECTED_FILE_KEY, mockFile);

		verify(s3Service, times(1)).putObject(any());
	}

	@Test
	public void testListObjects() {

		List<S3ObjectSummary> expectedObjectSummaries = new ArrayList<>();
		S3ObjectSummary s3ObjectSummary = new S3ObjectSummary();
		s3ObjectSummary.setKey(EXPECTED_FILE_KEY);
		expectedObjectSummaries.add(s3ObjectSummary);

		when(mockListObjectsV2Result.getObjectSummaries()).thenReturn(expectedObjectSummaries);

		when(s3Service.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(mockListObjectsV2Result);

		BipListObjectsResult result = instance.listObjects(BUCKET_NAME_1);

		assertNotNull(result);
		assertNotNull(result.getObjectKeys());
		assertEquals(1, result.getObjectKeys().size());
		assertEquals(EXPECTED_FILE_KEY, result.getObjectKeys().get(0));
	}

}
