package gov.nist.healthcare.ttt.webapp.xdr.controller

import gov.nist.healthcare.ttt.database.xdr.Status
import gov.nist.healthcare.ttt.webapp.xdr.core.TestCaseManager

//import com.wordnik.swagger.annotations.ApiOperation
import gov.nist.healthcare.ttt.webapp.xdr.domain.testcase.Result
import gov.nist.healthcare.ttt.webapp.xdr.domain.ui.UIResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import java.security.Principal
/**
 * Created by gerardin on 10/17/14.
 */

@RestController
@RequestMapping("api/xdr/tc")
class XdrTestCaseController {

    private static Logger log = LoggerFactory.getLogger(XdrTestCaseController.class)

    private final TestCaseManager testCaseManager

    @Autowired
    public XdrTestCaseController(TestCaseManager manager) {
        testCaseManager = manager
    }

    //@ApiOperation(value = "configure a test case")
    @RequestMapping(value = "/{id}/configure", method = RequestMethod.GET)
    @ResponseBody
    UIResponse configure(@PathVariable("id") String id) {

        log.debug("received configure request for tc$id")

        try {
            Result event = testCaseManager.configure(id)
            return new UIResponse(UIResponse.UIStatus.SUCCESS,"test case with id $id is configured", event)
        }
        catch(Exception e){
            return new UIResponse(UIResponse.UIStatus.ERROR, e.getMessage(), null)
        }


    }


    //@ApiOperation(value = "run a test case")
    @RequestMapping(value = "/{id}/run", method = RequestMethod.POST)
    @ResponseBody
    UIResponse run(@PathVariable("id") String id, @RequestBody HashMap config, Principal principal) {

        //User must be authenticated in order to run a test case=
        if (principal == null) {
            return new UIResponse(UIResponse.UIStatus.ERROR, "user not identified")
        }

        //rename variables to make their semantic more obvious
        def tcid = id
        def username = principal.getName()

        log.debug("received run request for tc$tcid from $username")

        try {
            Result event = testCaseManager.run(id, config, username)
            return new UIResponse(UIResponse.UIStatus.SUCCESS,"ran tc $tcid", event)
        }
        catch(Exception e){
            e.printStackTrace() //TODO flag so it is not logged in production
            return new UIResponse(UIResponse.UIStatus.ERROR, e.getMessage(), null)
        }


    }


    //@ApiOperation(value = "check status of a test case")
    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
    @ResponseBody
    UIResponse status(
            @PathVariable("id") String id, Principal principal) {

        if (principal == null) {
            return new UIResponse(UIResponse.UIStatus.ERROR, "user not identified")
        }

        //rename variables to make their semantic more obvious
        def tcid = id
        def username = principal.getName()
        def status
        String msg
        Result result

        log.debug("received status request for tc$id from $username")

        try {
            result = testCaseManager.status(username, tcid)

            log.debug("[status is $result.criteriaMet]")
            status = UIResponse.UIStatus.SUCCESS
            msg = "result of test case $id"
            return new UIResponse<Status>(status, msg , result)
        }catch(Exception e){
            e.printStackTrace()
            status = UIResponse.UIStatus.ERROR
            msg = "error while trying to fetch status for test case $id"
            result = new Result(Status.FAILED,e.getCause())
            return new UIResponse<Status>(status, msg , result)
        }
    }
}
