package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.edge.send.mu2

import gov.nist.healthcare.ttt.database.xdr.Status
import gov.nist.healthcare.ttt.database.xdr.XDRRecordInterface
import gov.nist.healthcare.ttt.database.xdr.XDRTestStepInterface
import gov.nist.healthcare.ttt.webapp.xdr.core.TestCaseExecutor
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseBuilder
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Result
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Content
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.TestCaseSender
import gov.nist.healthcare.ttt.xdr.domain.TkValidationReport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
/**
 * Created by gerardin on 10/27/14.
 */
@Component
final class TestCase48mu2 extends TestCaseSender {

    @Autowired
    public TestCase48mu2(TestCaseExecutor ex) {
        super(ex)
    }

    @Override
    Result run(Map context, String username) {

        executor.validateInputs(context,["direct_from"])

        //correlate this test to a direct_from address and a simulator id so we can be notified
        TestCaseBuilder builder = new TestCaseBuilder(id, username)
        XDRTestStepInterface step1 = executor.correlateRecordWithSimIdAndDirectAddress(sim, context.direct_from)
        executor.db.addNewXdrRecord(builder.addStep(step1).build())

        def content = new Content()
        content.endpoint = endpoints[0]
        content.endpointTLS = endpoints[1]
        return new Result(Status.PENDING, content)
    }

    @Override
    public void notifyXdrReceive(XDRRecordInterface r, TkValidationReport report) {

        XDRTestStepInterface step = executor.executeStoreXDRReport(report)
        step.directFrom = report.directFrom
        step.messageId = report.messageId

        XDRRecordInterface record = new TestCaseBuilder(r).addStep(step).build()

        //TODO cleaner implementation : choose relevant steps + better way to compare message ids.

        if(record.testSteps.size() != 4) {
            executor.db.updateXDRRecord(record)
        }
        else {

            def steps = record.testSteps.findAll{
                 it.name == "XDR_RECEIVE"
            }

            def messageId1 = steps[0].messageId
            def messageId2 = steps[1].messageId
            def messageId3 = steps[2].messageId

            log.info(" comparing ${messageId1}, ${messageId2}, ${messageId3}")

            boolean one = messageId1 != messageId2
            boolean two = messageId1 != messageId3
            boolean three = messageId2 != messageId3
            if(one & two & three) {
                record.status = Status.PASSED
                executor.db.updateXDRRecord(record)
            }
            else{
                record.status = Status.FAILED
                executor.db.updateXDRRecord(record)
            }
        }

    }
}
