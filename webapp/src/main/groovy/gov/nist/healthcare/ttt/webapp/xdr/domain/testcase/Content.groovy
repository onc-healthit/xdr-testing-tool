package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase

import com.fasterxml.jackson.databind.JsonNode

/**
 * We control what values a test step execution send back.
 *
 * Created by gerardin on 12/11/14.
 */
class Content {

    String request

    String response

    String report

    String endpoint

    String endpointTLS
	
	JsonNode ccdaReport

    List<String> endpoints

}
