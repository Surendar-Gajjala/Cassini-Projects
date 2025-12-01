package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.CustomFields;
import com.cassinisys.platform.util.Feedback;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class FeedbackIntegrationService implements CrudService<Feedback, Integer>, PageableService<Feedback, Integer> {

    @Autowired
    private Environment env;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailService mailService;


    @Transactional
    public static void main(String[] args) throws IOException {

        Feedback t = new Feedback();
        t.setSummary("Testing Creating ticketsss now");
        t.setPriority(5);
        t.setDescription("description");

        t.setNumber(88);
        t.setAssignedToId("dc_9-ejmmr5yoYacwqjQWU"); //aWkuQCMyOr5zqcdmr6CpXy
        t.setNotificationList("dc_9-ejmmr5yoYacwqjQWU");

        //HashMap<String, String> customFields = new HashMap<String, String>();

		/*customFields.put("Ticket_Type", "External");
        customFields.put("Customer_ID", "dppl");
		customFields.put("Customer_Name", "");
		customFields.put("Product_ID", "cassini.erp");
		customFields.put("Product_Name", "");
		customFields.put("Reported_By", "harish@dppl");*/

        CustomFields customFields = new CustomFields();
        customFields.setCustomerId("dppl");
        customFields.setCustomerName("");
        customFields.setProductId("cassini.erp");
        customFields.setProductName("");
        customFields.setApplicationArea("Other");
        customFields.setReportedBy("harish@dppl");
        customFields.setTicketType("External");
        t.setCustomerFields(customFields);
        new FeedbackIntegrationService().create(t);
        //new FeedbackIntegrationService().upload();

        //new FeedbackIntegrationService().update(t);

    }

    @Override
    @Transactional
    public Page<Feedback> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public Feedback create(Feedback feedback) {
        return createTicket(feedback, null, null);
    }

    @Transactional
    public Feedback createTicket(Feedback tickObj, String ticketId, Map<String, MultipartFile> fileMap) {

        try {
            if (tickObj != null) {
                RestTemplate restTemplate = getRestTemplate();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(tickObj);
                json = "{\"ticket\":" + json + "}";
                json = restTemplate.postForObject(getProperty("ticket_create_url"), json, String.class);

                JsonFactory factory = new JsonFactory();
                ObjectMapper mapper = new ObjectMapper(factory);
                TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
                };

                HashMap<String, Object> o = mapper.readValue(json, typeRef);
                tickObj.setId(o.get("id").toString());
                tickObj.setNumber(Integer.parseInt(o.get("number").toString()));
                //  tickObj.setNumber((Integer)o.get("number"));

            }
            //upload if attachments
            if (fileMap != null && fileMap.size() > 0 && ticketId != null && ticketId.length() > 0) {

                for (MultipartFile file : fileMap.values()) {
                    upload(ticketId, file);
                }

            }
            sendFeedbackMail(tickObj);
            return tickObj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickObj;
    }

    public void sendFeedbackMail(Feedback ticket) {
        String email = sessionWrapper.getSession().getLogin().getPerson().getEmail();
        if (email == null) {
            throw new CassiniException(
                    messageSource.getMessage("no_email_specified_for_your_account_please_contact_your_administrator", null, "No email specified for your account. Please contact your administrator.", LocaleContextHolder.getLocale()));
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));

        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("cssIncludes", getCss(host));
        model.put("tickerNumber", ticket.getNumber().toString());


        Mail mail = new Mail();
        mail.setMailTo(email);

        String appName = env.getProperty("app.name");
        if (appName == null) {
            appName = "Cassini";
        }

        mail.setMailSubject(appName + " - Feedback Acknowledgement");
        mail.setTemplatePath("feedback/feedbackmail.html");
        mail.setModel(model);

        mailService.sendEmail(mail);
    }

    private String getCss(String host) {
        String css = "";

        String[] urls = {
                host + "/app/assets/bower_components/bootstrap/dist/css/bootstrap.min.css"
        };

        try {
            for (String url : urls) {
                css += IOUtils.toString(new URL(url), (Charset) null);
                css += "\n\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return css;
    }

    @Transactional
    public List<Feedback> getAllOpenTickets(String reportId, Integer pageNumber) {
        List<Feedback> feedbackList = new ArrayList<>();
        try {
            RestTemplate restTemplate = getRestTemplate();
            String jsonOpen = restTemplate.getForObject(String.format(getProperty("ticket_getall_url"), reportId, pageNumber), String.class);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            try {
                List<Map<String, Object>> o = mapper.readValue(jsonOpen, new TypeReference<List<Map<String, Object>>>(){});
                o.forEach(val -> {
                    Feedback feedback = new Feedback();
                    LinkedHashMap map = (LinkedHashMap) val;
                    feedback.setNumber((Integer) map.get("number"));
                    feedback.setPriority((Integer) map.get("priority"));
                    feedback.setDescription((String) map.get("description"));
                    feedback.setSummary((String) map.get("summary"));
                    feedback.setStatus((String) map.get("status"));
                    try {
                        if(map.get("created_on") != null) {
                            Date createdOn = df.parse((String) map.get("created_on"));
                            feedback.setCreatedDate(outputFormatter.format(createdOn));
                        }
                        if(map.get("updated_at") != null) {
                            Date updatedAt = df.parse((String) map.get("updated_at"));
                            feedback.setUpdatedDate(outputFormatter.format(updatedAt));
                        }
                        if(map.get("completed_date") != null) {
                            Date completedDate = df.parse((String) map.get("completed_date"));
                            feedback.setCompletedDate(outputFormatter.format(completedDate));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    feedbackList.add(feedback);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    @Transactional
    public List<Feedback> getAllClosedTickets(String reportId, Integer pageNumber){
        List<Feedback> feedbackList = new ArrayList<>();
        try {
            RestTemplate restTemplate = getRestTemplate();
            String jsonClose = restTemplate.getForObject(String.format(getProperty("ticket_getall_url"), reportId, pageNumber), String.class);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            DateFormat outputFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            try {
                List<Map<String, Object>> o = mapper.readValue(jsonClose, new TypeReference<List<Map<String, Object>>>(){});
                o.forEach(val -> {
                    Feedback feedback = new Feedback();
                    LinkedHashMap map = (LinkedHashMap) val;
                    feedback.setNumber((Integer) map.get("number"));
                    feedback.setPriority((Integer) map.get("priority"));
                    feedback.setDescription((String) map.get("description"));
                    feedback.setSummary((String) map.get("summary"));
                    feedback.setStatus((String) map.get("status"));
                    try {
                        if(map.get("created_on") != null) {
                            Date createdOn = df.parse((String) map.get("created_on"));
                            feedback.setCreatedDate(outputFormatter.format(createdOn));
                        }
                        if(map.get("updated_at") != null) {
                            Date updatedAt = df.parse((String) map.get("updated_at"));
                            feedback.setUpdatedDate(outputFormatter.format(updatedAt));
                        }
                        if(map.get("completed_date") != null) {
                            Date completedDate = df.parse((String) map.get("completed_date"));
                            feedback.setCompletedDate(outputFormatter.format(completedDate));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    feedbackList.add(feedback);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    @Override
    @Transactional
    public Feedback update(Feedback t) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(t);
            json = "{\"ticket\":" + json + "}";

            restTemplate.put(String.format(getProperty("ticket_update_url"), t.getNumber()), json, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // TODO Auto-generated method stub

    }

    @Override
    @Transactional
    public Feedback get(Integer id) {
        Feedback tickObj = new Feedback();
        try {
            RestTemplate restTemplate = getRestTemplate();
            String json = restTemplate.getForObject(String.format(getProperty("ticket_update_url"), id), String.class);

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            try {
                HashMap<String, Object> o = mapper.readValue(json, typeRef);
                tickObj.setId((String) o.get("id"));

                return tickObj;
            } catch (IOException e) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickObj;
    }

    @Override
    @Transactional
    public List<Feedback> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    public void upload(String ticketId, MultipartFile mFile) throws IOException {

        File file = new File(mFile.getOriginalFilename());
        mFile.transferTo(file);
        PostMethod filePost = new PostMethod(getProperty("ticket_doc_url"));
        //	File targetFile = new File("C:\\Users\\Upendra\\TestDocument2.txt");
        PartBase[] parts = new PartBase[3];
        parts[0] = new StringPart("document[attachable_id]", ticketId/*"137615143"*/);
        parts[1] = new StringPart("document[attachable_type]", "Ticket");
        parts[2] = new FilePart("document[file]", file);
        MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, filePost.getParams());
        filePost.setRequestEntity(requestEntity);
        filePost.addRequestHeader(new Header("Accept", "application/xml"));
        filePost.addRequestHeader("X-Api-Secret", getProperty("api_key_secret"));
        filePost.addRequestHeader("X-Api-Key", getProperty("api_key"));

        HttpClient client = new HttpClient();

        client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
        filePost.setDoAuthentication(true);
        int status = client.executeMethod(filePost);


    }

    @Transactional
    private RestTemplate getRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Accept", MediaType.APPLICATION_JSON_VALUE));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        interceptors.add(new HeaderRequestInterceptor("X-Api-Secret", getProperty("api_key_secret")));
        interceptors.add(new HeaderRequestInterceptor("X-Api-Key", getProperty("api_key")));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


        return restTemplate;
    }

    @Transactional
    private String getProperty(String name) {
        String value = System.getProperty(name);
        if (value == null) {
            value = this.env.getProperty(name);
        }

        return value;
    }

    @Transactional
    private class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

        private final String headerName;
        private final String headerValue;

        public HeaderRequestInterceptor(String headerName, String headerValue) {
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        @Override
        @Transactional
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpRequest wrapper = new HttpRequestWrapper(request);
            wrapper.getHeaders().set(headerName, headerValue);
            return execution.execute(wrapper, body);
        }
    }

}
