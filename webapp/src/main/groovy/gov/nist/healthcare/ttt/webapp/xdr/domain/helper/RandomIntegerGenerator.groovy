package gov.nist.healthcare.ttt.webapp.xdr.domain.helper
/**
 * Created by gerardin on 5/11/15.
 */
class RandomIntegerGenerator {


    /*
    generate an integer between 1 and upperBound
     */
    static def generate(int upperBound){
        Random rand = new Random()
        int max = upperBound
        rand.nextInt(max)+1
    }
}
