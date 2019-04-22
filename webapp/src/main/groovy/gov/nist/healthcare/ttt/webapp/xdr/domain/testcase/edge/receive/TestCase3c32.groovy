package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.edge.receive
import gov.nist.healthcare.ttt.database.xdr.XDRRecordInterface
import gov.nist.healthcare.ttt.database.xdr.XDRTestStepInterface
import gov.nist.healthcare.ttt.tempxdrcommunication.artifact.ArtifactManagement
import gov.nist.healthcare.ttt.webapp.xdr.core.TestCaseExecutor
import gov.nist.healthcare.ttt.webapp.xdr.domain.helper.MsgLabel
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseBuilder
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Result
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by gerardin on 10/27/14.
 */
@Component
final class TestCase3c32 extends TestCase {

    @Autowired
    public TestCase3c32(TestCaseExecutor executor) {
        super(executor)
    }



    @Override
    Result run(Map context, String username) {

        executor.validateInputs(context,["targetEndpointTLS"])

        TestCaseBuilder builder = new TestCaseBuilder(id, username)

        // Correlate this test to a direct_from address and a simulator id so we can be notified
        XDRTestStepInterface step1 = executor.correlateRecordWithSimIdAndDirectAddress(sim, "testcase3c32@$executor.hostname")

        sim = registerDocSrcEndpoint(username,context)

        // Send an xdr with the endpoint created above
        context.simId = sim.simulatorId
        context.endpoint = sim.endpointTLS
        context.wsaTo = context.targetEndpointTLS
        context.directTo = "testcase3c32@$executor.hostname"
        context.directFrom = "testcase3c32@$executor.hostname"
        context.messageType = ArtifactManagement.Type.XDR_C32
        XDRTestStepInterface step2 = executor.executeSendXDRStep(context)

        // Create a new test record
        XDRRecordInterface record = builder.addStep(step1).addStep(step2).build()
        record.setStatus(step2.status)
        executor.db.addNewXdrRecord(record)

        // Build the message to return to the gui
        log.info(MsgLabel.XDR_SEND_AND_RECEIVE.msg)
        def content = executor.buildSendXDRContent(step2)
        return new Result(record.criteriaMet, content)
    }


}
