package poc.inetum.flowable.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import poc.inetum.DTO.DocDTO;
import poc.inetum.flowable.utility.Constants;
import poc.inetum.flowable.utility.DirectoryTraverser;
import poc.inetum.flowable.utility.HttpUtil;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Slf4j
@Service
public class CmisService {

    @Value("${alfresco.repository.url}")
    String alfrescoUrl;

    @Value("${alfresco.repository.user}")
    String username;

    @Value("${alfresco.repository.pass}")
    String pwd;

    @Value("${alfresco.connection.name}")
    String connectionName;

    private static Map<String, Session> connections = new ConcurrentHashMap<>();

    private Session currentSession;

    @PostConstruct
    public void init() {

        currentSession = getSession();

        log.info(String.valueOf(currentSession));

    }

    public Session getSession() {

        Session session = connections.get(connectionName);

        if (session == null) {

            log.info("Not connected, creating new connection to" +
                    " Alfresco with the connection id (" + connectionName + ")");

            SessionFactory factory = SessionFactoryImpl.newInstance();

            String alfrescoBrowserUrl = alfrescoUrl + "/api/-default-/public/cmis/versions/1.1/browser";

            Map<String, String> parameters = new HashMap<>();

            parameters.put(SessionParameter.USER, username);
            parameters.put(SessionParameter.PASSWORD, pwd);

            parameters.put(SessionParameter.BROWSER_URL, alfrescoBrowserUrl);
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.BROWSER.value());

            parameters.put(SessionParameter.COMPRESSION, "true");
            parameters.put(SessionParameter.CACHE_TTL_OBJECTS, "0");


            session = factory.getRepositories(parameters).get(0).createSession();

            connections.put(connectionName, session);

        } else {
            Logger.getLogger("Already connected to Alfresco with the " +
                    "connection id (" + connectionName + ")");
        }

        return session;
    }

    public List<DocDTO> getSiteContent() {

        List<DocDTO> docsList = new ArrayList<>();

        ItemIterable<QueryResult> results = query("SELECT cmis:objectId,cmis:name FROM cmis:document WHERE CONTAINS('PATH:\"/app:company_home/st:sites/cm:testpoc/cm:documentLibrary/cm:testUpload/*\"')");

        for (QueryResult hit : results) {

            String objectid = hit.getPropertyValueByQueryName("cmis:objectId");
            String name = hit.getPropertyValueByQueryName("cmis:name");

            docsList.add(new DocDTO(objectid.substring(0, objectid.length() - 4), name));
            Logger.getLogger(objectid.substring(0, objectid.length() - 4) + " " + name);
            Logger.getLogger("--------------------------------------------------------");
        }

        return docsList;

    }

    private ItemIterable<QueryResult> query(String query) {
        return currentSession.query(query, false);
    }


    public void documentUpload() throws IOException {

        final String uploadURI = "http://10.1.1.179:8080/alfresco/service/api/upload";
        final String authURI = "http://10.1.1.179:8080/alfresco/service/api/login";
        final String inputUri = "/uploads"; // files to be uploaded from this directory
        final String siteID = "testpoc"; //id of the site for e.g if site name is TestPoc the id will be testpoc
        final String uploadDir = "testUpload"; //directory created under document library

        final HttpUtil httpUtil = new HttpUtil();
        String authTicket = Constants.EMPTY;
        try {
            authTicket = httpUtil.getAuthTicket(authURI, username, pwd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final StringBuilder responseBody = new StringBuilder();

        final File fileObject = new File(inputUri);
        //if uri is a directory the upload all files..
        if (fileObject.isDirectory()) {
            final Set<File> setOfFiles = DirectoryTraverser.getFileUris(fileObject);
            for (Iterator<File> iterator = setOfFiles.iterator(); iterator.hasNext(); ) {
                final File fileObj = iterator.next();
                //call document upload
                if (fileObj.isFile()) {
                    responseBody.append(httpUtil.documentUpload(
                            fileObj, authTicket, uploadURI, siteID,
                            uploadDir));
                    responseBody.append(Constants.BR);
                }
            }
        } else {
            responseBody.append(httpUtil.documentUpload(
                    fileObject, authTicket, uploadURI, siteID,
                    uploadDir));
        }

        Logger.getLogger("Response of upload operation >>>: " + responseBody);
    }


}
