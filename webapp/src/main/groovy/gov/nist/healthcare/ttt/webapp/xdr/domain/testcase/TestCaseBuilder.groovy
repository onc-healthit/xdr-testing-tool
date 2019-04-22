package gov.nist.healthcare.ttt.webapp.xdr.domain.testcase

import gov.nist.healthcare.ttt.database.xdr.Status
import gov.nist.healthcare.ttt.database.xdr.XDRRecordImpl
import gov.nist.healthcare.ttt.database.xdr.XDRRecordInterface
import gov.nist.healthcare.ttt.database.xdr.XDRTestStepInterface
/**
 * Created by gerardin on 10/27/14.
 *
 * Little helper class to set up canonical test case config
 */
class TestCaseBuilder {

    private final XDRRecordInterface record

    public TestCaseBuilder(tcId,username){
        record = new XDRRecordImpl()
        record.setTestCaseNumber(tcId)
        record.setUsername(username)
        record.status = Status.PENDING
        record.testSteps = new LinkedList<XDRTestStepInterface>()
    }

    public TestCaseBuilder(XDRRecordInterface record){
        this.record = record
    }

    public XDRRecordInterface build(){
        return record
    }

    public TestCaseBuilder addStep(XDRTestStepInterface step){
        record.testSteps.add(step)
        return this
    }


}
