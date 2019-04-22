package gov.nist.healthcare.ttt.webapp.testFramework;

//import com.mangofactory.swagger.plugin.EnableSwagger;
import gov.nist.healthcare.ttt.webapp.common.config.ComponentConfig;
import gov.nist.healthcare.ttt.webapp.common.config.ToolkitClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * Same as the real application but here we have a additional @ComponentScan
 * in gov.nist.healthcare.ttt.webapp.testFramework, which :
 * - replace the clock by a mock clock returning always the same time.
 */

@ComponentScan({"gov.nist.healthcare.ttt.webapp.testFramework"})
@EnableAutoConfiguration
//@EnableSwagger
@Import({ComponentConfig.class,
        ToolkitClientConfig.class
})
@ImportResource("classpath:/spring/resources.xml")
public class TestApplication {

    private static Logger log = LoggerFactory.getLogger(TestApplication.class);

//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//        tomcat.addAdditionalTomcatConnectors(createSslConnector());
//        log.info("created a SSL connector for Tomcat");
//        return tomcat;
//    }
//
//    private Connector createSslConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//        try {
//            File keystore = new ClassPathResource("keystore/keystore").getFile();
//            File truststore = new ClassPathResource("keystore/keystore").getFile();
//            connector.setScheme("https");
//            connector.setSecure(true);
//            connector.setPort(9443);
//            protocol.setSSLEnabled(true);
//            protocol.setKeystoreFile(keystore.getAbsolutePath());
//            protocol.setKeystorePass("changeit");
//            protocol.setTruststoreFile(truststore.getAbsolutePath());
//            protocol.setTruststorePass("changeit");
//            protocol.setKeyAlias("1");
//            return connector;
//        }
//        catch (IOException ex) {
//            throw new IllegalStateException("can't access keystore: [" + "keystore"
//                    + "] or truststore: [" + "keystore" + "]", ex);
//        }
//    }


    /*
    Not really necessary since it is equivalent to springboot default
     */
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /*
    Important to integrate swagger-ui code
     */
//    @Bean
//    public WebMvcConfigurerAdapter customWebMvcConfigurerAdapter() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addViewControllers(ViewControllerRegistry registry) {
//                super.addViewControllers(registry);
//                // Use forward: prefix so that no view resolution is done
//                registry.addViewController("/swagger/").setViewName("forward:/index.html");
//                return;
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }


}