package gov.nist.healthcare.ttt.webapp.api.xdr.edge.receiving.tls

import gov.nist.healthcare.ttt.webapp.TestUtils
import gov.nist.healthcare.ttt.webapp.XDRSpecification
import gov.nist.healthcare.ttt.webapp.testFramework.TestApplication
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebAppConfiguration
@IntegrationTest
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = TestApplication.class)
class XdrTestCase8Test extends XDRSpecification {

    String simId = "8"
    String tcId = "8"
    String simEndpoint = TestUtils.simEndpoint(simId, system)
    public String testCaseConfig =
            """{
    "ip_address": "127.0.0.1",
    "port": "12085"
}"""

    def "user succeeds in running test case"() throws Exception {

        when: "receiving a request to run test case"
        MockHttpServletRequestBuilder getRequest = TestUtils.run(tcId, userId, testCaseConfig)

        then: "we receive back a message with status and report of the transaction"

        gui.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("SUCCESS"))
                .andExpect(jsonPath("content.criteriaMet").value("PASSED"))
    }

}

