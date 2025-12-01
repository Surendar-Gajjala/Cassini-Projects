package com.cassinisys.platform.service.common;

import com.autodesk.client.ApiException;
import com.autodesk.client.ApiResponse;
import com.autodesk.client.api.BucketsApi;
import com.autodesk.client.api.DerivativesApi;
import com.autodesk.client.api.ObjectsApi;
import com.autodesk.client.auth.Credentials;
import com.autodesk.client.auth.OAuth2TwoLegged;
import com.autodesk.client.model.*;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.UriBuilder;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by Nageshreddy on 02-05-2019.
 */
@Service
@Transactional
public class ForgeService {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private Environment environment;

    @Autowired
    private PreferenceRepository preferenceRepository;

    private static String CLIENT_ID = null;
    private static String CLIENT_SECRET = null;
    private static String token = null;

    // Initialize the relevant clients; in this example, the Objects, Buckets and
    // Derivatives clients, which are part of the Data Management API and Model
    // Derivatives API
    private static final BucketsApi bucketsApi = new BucketsApi();
    private static final ObjectsApi objectsApi = new ObjectsApi();
    private static final DerivativesApi derivativesApi = new DerivativesApi();
    private static OAuth2TwoLegged oauth2TwoLegged;
    private static Credentials twoLeggedCredentials;
    private final String BUCKET_KEY1 = "cassiniplm_1230_cloud_";
    private String BUCKET_KEY = null;
    private String[] formats = new String[]{"3dm", "3ds", "asm", "catpart", "catproduct", "cgr", "collaboration", "dae", "dgn", "dlv3", "dwf", "dwfx", "dwg", "dwt", "dxf", "emodel", "exp", "f3d", "fbx",
            "g", "gbxml", "glb", "gltf", "iam", "idw", "ifc", "ige", "iges", "igs", "ipt", "iwm", "jt", "max", "model", "neu", "nwc", "nwd", "obj", "pdf", "pmlprj", "pmlprjz", "prt",
            "psmodel", "rcp", "rvt", "sab", "sat", "session", "skp", "sldasm", "sldprt", "ste", "step", "stl", "stla", "stlb", "stp", "stpz", "wire", "x_b", "x_t", "xas", "xpr", "zip"};

    public String getAuthentication() {
        return this.isForgeActive() ? token : token;
    }

    public Boolean checkAuthenticate(String clientId, String clientKey) {
        try {
            // You must provide at least one valid scope
            List<String> scopes = new ArrayList<String>();
            scopes.add("data:read");
            scopes.add("data:write");
            scopes.add("bucket:create");
            scopes.add("bucket:read");
            scopes.add("bucket:update");
            scopes.add("bucket:delete");

            // Set autoRefresh to `true` to automatically refresh the access token when it expires.
            oauth2TwoLegged = new OAuth2TwoLegged(clientId, clientKey, scopes, true);
            twoLeggedCredentials = oauth2TwoLegged.authenticate();
            createBucketIfNotExists();
            return (oauth2TwoLegged != null && oauth2TwoLegged.getCredentials() != null && oauth2TwoLegged.getCredentials().getAccessToken() != null);
        } catch (Exception e) {
            throw new CassiniException("Please provide valid client id and secret key");

        }
    }

    public Boolean isForgeActive() {
        Preference val = this.preferenceRepository.findByPreferenceKey("APPLICATION.FORGE_ACTIVE");
        if (val != null && val.getBooleanValue()) {
            CLIENT_ID = this.getPreferenceValue("APPLICATION.FORGE_CLIENT_ID");
            CLIENT_SECRET = this.getPreferenceValue("APPLICATION.FORGE_CLIENT_SECRET_KEY");
            token = this.initializeOAuth();
            return (token != null && !token.trim().equals(""));
        } else {
            return false;
        }
    }

    private String initializeOAuth() {
        try {
            // You must provide at least one valid scope
            List<String> scopes = new ArrayList<String>();
            scopes.add("data:read");
            scopes.add("data:write");
            scopes.add("bucket:create");
            scopes.add("bucket:read");
            scopes.add("bucket:update");
            scopes.add("bucket:delete");

            // Set autoRefresh to `true` to automatically refresh the access token when it expires.
            oauth2TwoLegged = new OAuth2TwoLegged(CLIENT_ID, CLIENT_SECRET, scopes, true);
            twoLeggedCredentials = oauth2TwoLegged.authenticate();
            createBucketIfNotExists();
            return oauth2TwoLegged.getCredentials().getAccessToken();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public String getThumbnail(String urn) {
        try {
            ApiResponse<File> apiResponse = derivativesApi.getThumbnail(urn, 80, 80, oauth2TwoLegged, twoLeggedCredentials);
            byte[] fileContent = FileUtils.readFileToByteArray(apiResponse.getData());
            String encodedString = java.util.Base64.getEncoder().encodeToString(fileContent);
            return encodedString;
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        return null;
    }

    public void createBucketIfNotExists() {
//        BUCKET_KEY1 = preferenceRepository.findByPreferenceKey("APPLICATION.CLIENT_BUCKET_NAME").getStringValue();
        BUCKET_KEY = BUCKET_KEY1 + sessionWrapper.getTenantId();
        try {
            ApiResponse<Bucket> bucketApiResponse = bucketsApi.getBucketDetails(BUCKET_KEY, oauth2TwoLegged, twoLeggedCredentials);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            createBucket();
        }
    }

    public void createBucket() {
        try {
            PostBucketsPayload postBucketsPayload = new PostBucketsPayload();
            List<PostBucketsPayloadAllow> postBucketsPayloadAllows = new ArrayList();
            PostBucketsPayloadAllow postBucketsPayloadAllow = new PostBucketsPayloadAllow();

            postBucketsPayloadAllow.setAuthId(CLIENT_ID);
            postBucketsPayloadAllow.setAccess(PostBucketsPayloadAllow.AccessEnum.FULL);

            postBucketsPayloadAllows.add(postBucketsPayloadAllow);

            postBucketsPayload.setBucketKey(BUCKET_KEY);
            postBucketsPayload.setPolicyKey(PostBucketsPayload.PolicyKeyEnum.PERSISTENT);
            postBucketsPayload.setAllow(postBucketsPayloadAllows);

            bucketsApi.createBucket(postBucketsPayload, null, oauth2TwoLegged, twoLeggedCredentials);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> uploadForgeFile(String FILE_NAME, String FILE_PATH) {
        if (oauth2TwoLegged == null || oauth2TwoLegged.isAccessTokenExpired()) {
            getAuthentication();
        }
        String ext2 = FilenameUtils.getExtension(FILE_NAME);
        boolean result = Arrays.stream(formats).anyMatch(ext2::equalsIgnoreCase);
        if (oauth2TwoLegged != null && result && isForgeActive()) {
            File fileToUpload = new File(FILE_PATH);
            Map<String, String> fileMap = new HashMap();
            ApiResponse<ObjectDetails> response = null;
            ObjectDetails objectDetails = null;
            Job job = null;
            try {
                response = objectsApi.uploadObject(BUCKET_KEY, FILE_NAME,
                        (int) fileToUpload.length(), fileToUpload, null, null, oauth2TwoLegged, twoLeggedCredentials);

                objectDetails = response.getData();
                job = translateToSVF(objectDetails.getObjectId());
                ApiResponse<Manifest> manifest = derivativesApi.getManifest(job.getUrn(), null, oauth2TwoLegged, twoLeggedCredentials);
                Manifest manifest1 = manifest.getData();
                while ((!manifest1.getStatus().contains("success") && !manifest1.getHasThumbnail()) &&
                        (!manifest1.getStatus().contains("failed"))) {
                    Thread.sleep(1000 * 5);
                    manifest = derivativesApi.getManifest(job.getUrn(), null, oauth2TwoLegged, twoLeggedCredentials);
                    manifest1 = manifest.getData();
                }
                String thumbnail = getThumbnail(job.getUrn());
                fileMap.put("urn", job.getUrn());
                fileMap.put("thumbnail", thumbnail);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return fileMap;
        } else {
            return null;
        }

    }

    private Job translateToSVF(String urn) throws ApiException, Exception {
        JobPayload job = new JobPayload();

        byte[] urnBase64 = Base64.encodeBase64(urn.getBytes());

        JobPayloadInput input = new JobPayloadInput();
        input.setUrn(new String(urnBase64));

        JobPayloadOutput output = new JobPayloadOutput();
        JobPayloadItem formats = new JobPayloadItem();
        formats.setType(JobPayloadItem.TypeEnum.SVF);
        formats.setViews(Arrays.asList(JobPayloadItem.ViewsEnum._3D));
        output.setFormats(Arrays.asList(formats));

        job.setInput(input);
        job.setOutput(output);

        ApiResponse<Job> response = derivativesApi.translate(job, true, oauth2TwoLegged, twoLeggedCredentials);

        return response.getData();
    }

    public void openViewer(String base64Urn) {
        try {
            //System.out.println("***** Opening SVF file in viewer with urn:" + base64Urn);
            File htmlFile = new File("viewer.html");
            UriBuilder builder = UriBuilder.fromPath("file:///" + htmlFile.getAbsolutePath())
                    .queryParam("token", twoLeggedCredentials.getAccessToken()).queryParam("urn", base64Urn);
            Desktop.getDesktop().browse(builder.build());
        } catch (Exception e) {

        }
    }

    @Transactional
    private String getProperty(String name) {
        String value = this.environment.getProperty(name);
        if (value == null || value.equals("")) {
            throw new CassiniException("Please provide client_id and client_secret value to get forge Authentication");
        } else {
            return value;
        }
    }

    @Transactional
    private String getPreferenceValue(String name) {
        Preference pref = preferenceRepository.findByPreferenceKey(name);
        if (pref == null) {
            throw new CassiniException("Please provide client_id and client_secret value to get forge Authentication");
        }
        String value = preferenceRepository.findByPreferenceKey(name).getStringValue();
        if (value == null || value.equals("")) {
            throw new CassiniException("Please provide client_id and client_secret value to get forge Authentication");
        } else {
            return value.trim();
        }
    }
}
