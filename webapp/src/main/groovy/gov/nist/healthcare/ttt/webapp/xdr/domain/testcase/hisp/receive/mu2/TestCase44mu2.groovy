package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.hisp.receive.mu2

import gov.nist.healthcare.ttt.database.xdr.Status
import gov.nist.healthcare.ttt.database.xdr.XDRRecordInterface
import gov.nist.healthcare.ttt.database.xdr.XDRSimulatorInterface;
import gov.nist.healthcare.ttt.database.xdr.XDRTestStepInterface
import gov.nist.healthcare.ttt.tempxdrcommunication.artifact.ArtifactManagement
import gov.nist.healthcare.ttt.webapp.xdr.core.TestCaseExecutor
import gov.nist.healthcare.ttt.webapp.xdr.domain.helper.MsgLabel
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Content
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseBuilder
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Result
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCase
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseSender
import gov.nist.healthcare.ttt.xdr.domain.TkValidationReport;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * Created by gerardin on 10/27/14.
 */
@Component
final class TestCase44mu2 extends TestCaseSender {


	@Autowired
	TestCase44mu2(TestCaseExecutor executor) {
		super(executor)
	}

	@Override
	Result configure(){
		Content c = new Content()
		c.endpoint = sim.endpoint
		c.endpointTLS = sim.endpointTLS
		new Result(Status.PENDING, c)
	}

	@Override
	Result run(Map context, String username) {

		executor.validateInputs(context, ["targetEndpointTLS", "outgoing_from"])

		TestCaseBuilder builder = new TestCaseBuilder(id, username)

		// Correlate this test to a direct_from address and a simulator id so we can be notified
		if(context.direct_from != null) {
			XDRTestStepInterface step1 = executor.correlateRecordWithSimIdAndDirectAddress(sim, context.direct_from)
			builder.addStep(step1)
		}

		registerDocSrcEndpoint(username,context)

		// Send an xdr with the endpoint created above
		context.endpoint = context.targetEndpointTLS
		context.simId = id + "_" + username
		context.wsaTo = context.endpointTLS
		//an address that provides a processed MDN and a failure MDN after n seconds (n < sending hisp timeout)
		//  context.directTo = "processedfailure@ttpedgetest.sitenv.org"
		//	context.directTo = "processedtimeoutfailure@$executor.hostname"
		
		println("TIMEOUT FOR XDR 44 "+context.timeout)
		if(context.timeout == null){			
			context.directTo = "delaydispatched"+"65"+"@$executor.hostname"
		}
		else {
			context.directTo = "delaydispatched"+context.timeout+"@$executor.hostname"
		}
		
		context.directFrom = context.outgoing_from
		context.finalDestinationDelivery = "true"
		context.messageType = ArtifactManagement.Type.XDR_MINIMAL_METADATA
		XDRTestStepInterface step2 = executor.executeSendXDRStep(context)

		// Create a new test record
		XDRRecordInterface record = builder.addStep(step2).build()
		record.setStatus(step2.status)
		executor.db.addNewXdrRecord(record)

		// Build the message to return to the gui
		log.info(MsgLabel.XDR_SEND_AND_RECEIVE.msg)
		def content = executor.buildSendXDRContent(step2)
		return new Result(Status.PENDING, content)
	}

	@Override
	public void notifyXdrReceive(XDRRecordInterface record, TkValidationReport report) {

		//we parse the XDR report
		XDRTestStepInterface step = executor.executeStoreXDRReport(report)

		//we update the record
		XDRRecordInterface updatedRecord = new TestCaseBuilder(record).addStep(step).build()
		updatedRecord.status = Status.MANUAL
		executor.db.updateXDRRecord(updatedRecord)
	}

	@Override
	public Result getReport(XDRRecordInterface record) {
		executor.getSimpleSendReport(record)
	}

}
