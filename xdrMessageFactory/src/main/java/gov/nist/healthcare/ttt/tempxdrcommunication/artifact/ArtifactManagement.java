package gov.nist.healthcare.ttt.tempxdrcommunication.artifact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author mccaffrey
 */
public class ArtifactManagement {

    public enum Type {

        XDR_FULL_METADATA, // DONE
        XDR_MINIMAL_METADATA, // DONE
        XDR_VANILLA, // DONE
        XDR_SAML_METADATA,
        NEGATIVE_BAD_SOAP_HEADER,
        NEGATIVE_BAD_SOAP_BODY,
        NEGATIVE_MISSING_DIRECT_BLOCK, // DONE
        NEGATIVE_MISSING_METADATA_ELEMENTS1,
        NEGATIVE_MISSING_METADATA_ELEMENTS2,
        NEGATIVE_MISSING_METADATA_ELEMENTS3,
        NEGATIVE_MISSING_METADATA_ELEMENTS4,
        NEGATIVE_MISSING_METADATA_ELEMENTS5,
        XDR_CCR, // DONE
        XDR_C32, // DONE
        NEGATIVE_MISSING_ASSOCIATION, // DONE
        DELIVERY_STATUS_NOTIFICATION_SUCCESS, //DONE
        DELIVERY_STATUS_NOTIFICATION_FAILURE, //DONE

        C32_FULL_1,
        C32_MINIMAL_1,
        C32_FULL_2,
        C32_MINIMAL_2,
        CCDA_AMBULATORY_FULL,
        CCDA_AMBULATORY_MINIMAL,
        CCDA_INPATIENT_FULL,
        CCDA_INPATIENT_MINIMAL,
        CCR_FULL_1,
        CCR_MINIMAL_1,
        CCR_FULL_2,
        CCR_MINIMAL_2,
        TESTING_ONLY
    };

    public static final String NIST_OID_PREFIX = "2.16.840.1.113883.3.72.5";

    private static final String FILENAME_XDR_FULL_METADATA = "Xdr_full_metadata.xml";
    private static final String FILENAME_XDR_FULL_METADATA_ONLY = "Xdr_full_metadata_only.xml";
    private static final String FILENAME_XDR_FULL_METADATA_ONLY_NO_SOAP = "Xdr_full_metadata_only_no_soap.xml";
    private static final String FILENAME_XDR_SAML_METADATA = "Xdr_saml_metadata.xml";
    private static final String FILENAME_BAD_SOAP = "negative_bad_soap.xml";
    private static final String FILENAME_BAD_SOAP_BODY = "negative_bad_soap_body.xml";
    private static final String FILENAME_MISSING_DIRECT_BLOCK = "negative_missing_direct_block.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS1 = "negative_missing_metadata_elements1.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS2 = "negative_missing_metadata_elements2.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS3 = "negative_missing_metadata_elements3.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS4 = "negative_missing_metadata_elements4.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS5 = "negative_missing_metadata_elements5.xml";

    private static final String FILENAME_MISSING_METADATA_ELEMENTS1_NO_SOAP = "negative_missing_metadata_elements1_no_soap.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS2_NO_SOAP = "negative_missing_metadata_elements2_no_soap.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS3_NO_SOAP = "negative_missing_metadata_elements3_no_soap.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS4_NO_SOAP = "negative_missing_metadata_elements4_no_soap.xml";
    private static final String FILENAME_MISSING_METADATA_ELEMENTS5_NO_SOAP = "negative_missing_metadata_elements5_no_soap.xml";

    private static final String FILENAME_XDR_CCR = "Xdr_Ccr.xml";
    private static final String FILENAME_XDR_CCR_ENCODED = "ccr64.txt";
    private static final String FILENAME_XDR_C32 = "Xdr_C32.xml";
    private static final String FILENAME_XDR_C32_ENCODED = "c3264.txt";
    private static final String FILENAME_MISSING_ASSOCIATION = "negative_missing_association.xml";
    private static final String FILENAME_MISSING_ASSOCIATION_NO_SOAP = "negative_missing_association_no_soap.xml";
    private static final String FILENAME_XDR_MINIMAL_METADATA = "Xdr_minimal_metadata.xml";
    private static final String FILENAME_XDR_MINIMAL_METADATA_ONLY = "Xdr_minimal_metadata_only.xml";
    private static final String FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP = "Xdr_minimal_metadata_only_no_soap.xml";
    private static final String FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP_NO_ROUTING = "Xdr_minimal_metadata_only_no_soap_no_routing.xml";
    private static final String FILENAME_XDR_VANILLA = "Xdr_vanilla.xml";

    private static final String FILENAME_ENCODED_CCDA = "encodedCCDA.txt";
    private static final String FILENAME_CCDA = "CCDA.xml";
    private static final String FILENAME_IGNORE_PAYLOAD = "ignorePayload.txt";
    private static final String FILENAME_DELIVERY_STATUS_NOTIFICATION_SUCCESS = "DeliveryStatusNotification_success.xml";
    private static final String FILENAME_DELIVERY_STATUS_NOTIFICATION_FAILURE = "DeliveryStatusNotification_failure.xml";
    private static final String FILENAME_DELIVERY_STATUS_NOTIFICATION_SUCCESS_STANDALONE = "DeliveryStatusNotification_success_standalone.xml";
    private static final String FILENAME_DELIVERY_STATUS_NOTIFICATION_FAILURE_STANDALONE = "DeliveryStatusNotification_failure_standalone.xml";

    private static final String FILENAME_XDR_TESTING_ONLY = "XdrTestingOnly.xml";

    private static final String FILENAME_XDR_VANILLA_C32_FULL_1_DOC = "./vanilla_xdr/C32_sample1_full/C32_Sample1.xml";
    private static final String FILENAME_XDR_VANILLA_C32_FULL_1_METADATA = "./vanilla_xdr/C32_sample1_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_C32_MINIMAL_1_DOC = "./vanilla_xdr/C32_sample1_minimal/C32_Sample1.xml";
    private static final String FILENAME_XDR_VANILLA_C32_MINIMAL_1_METADATA = "./vanilla_xdr/C32_sample1_minimal/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_C32_FULL_2_DOC = "./vanilla_xdr/C32_sample2_full/C32_Sample2.xml";
    private static final String FILENAME_XDR_VANILLA_C32_FULL_2_METADATA = "./vanilla_xdr/C32_sample2_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_C32_MINIMAL_2_DOC = "./vanilla_xdr/C32_sample2_minimal/C32_Sample2.xml";
    private static final String FILENAME_XDR_VANILLA_C32_MINIMAL_2_METADATA = "./vanilla_xdr/C32_sample2_minimal/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_DOC = "./vanilla_xdr/CCDA_Ambulatory_full/CCDA_Ambulatory.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_METADATA = "./vanilla_xdr/CCDA_Ambulatory_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_DOC = "./vanilla_xdr/CCDA_Ambulatory_minimal/CCDA_Ambulatory.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_METADATA = "./vanilla_xdr/CCDA_Ambulatory_minimal/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_DOC = "./vanilla_xdr/CCDA_Inpatient_full/CCDA_Inpatient.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_METADATA = "./vanilla_xdr/CCDA_Inpatient_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_DOC = "./vanilla_xdr/CCDA_Inpatient_minimal/CCDA_Inpatient.xml";
    private static final String FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_METADATA = "./vanilla_xdr/CCDA_Inpatient_minimal/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_FULL_1_DOC = "./vanilla_xdr/CCR_sample1_full/CCR_Sample1.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_FULL_1_METADATA = "./vanilla_xdr/CCR_sample1_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_MINIMAL_1_DOC = "./vanilla_xdr/CCR_sample1_minimal/CCR_Sample1.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_MINIMAL_1_METADATA = "./vanilla_xdr/CCR_sample1_minimal/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_FULL_2_DOC = "./vanilla_xdr/CCR_sample2_full/CCR_Sample2.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_FULL_2_METADATA = "./vanilla_xdr/CCR_sample2_full/single_doc.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_MINIMAL_2_DOC = "./vanilla_xdr/CCR_sample2_minimal/CCR_Sample2.xml";
    private static final String FILENAME_XDR_VANILLA_CCR_MINIMAL_2_METADATA = "./vanilla_xdr/CCR_sample2_minimal/single_doc.xml";

    public static String getPayload(Type type, Settings settings) throws IOException {
        makeSettingsSafe(settings);
        String payload = null;
        switch (type) {
            case XDR_FULL_METADATA:
                payload = getXdrFullMetadata(settings);
                break;
            case XDR_SAML_METADATA:
                payload = getXdrSamlMetadata(settings);
                break;
            case XDR_MINIMAL_METADATA:
                payload = getXdrMinimalMetadata(settings);
                break;
            case DELIVERY_STATUS_NOTIFICATION_SUCCESS:
                payload = getDeliveryStatusNotificationSuccess(settings);
                break;
            case DELIVERY_STATUS_NOTIFICATION_FAILURE:
                payload = getDeliveryStatusNotificationFailure(settings);
                break;
            default:
                break;

        }

        return payload;

    }

    public static String getMtomSoap(Type type, Settings settings) {
        makeSettingsSafe(settings);
        String payload = null;
        switch (type) {
            case XDR_FULL_METADATA:
                payload = getXdrFullMetadataOnly(settings);
                break;
            case XDR_SAML_METADATA:
                payload = getXdrSamlMetadata(settings);
                break;
            case XDR_MINIMAL_METADATA:
                payload = getXdrMinimalMetadataOnly(settings);
                break;
            case NEGATIVE_BAD_SOAP_HEADER:
                payload = getXdrBadSoap(settings);
                break;
            case NEGATIVE_MISSING_DIRECT_BLOCK:
                payload = getXdrMissingDirectBlock(settings);
                break;
            case NEGATIVE_BAD_SOAP_BODY:
                payload = getXdrBadSoapBody(settings);
                break;
            case NEGATIVE_MISSING_METADATA_ELEMENTS1:
                payload = getXdrMissingMetadataElements1(settings);
                break;
            case NEGATIVE_MISSING_METADATA_ELEMENTS2:
                payload = getXdrMissingMetadataElements2(settings);
                break;
            case NEGATIVE_MISSING_METADATA_ELEMENTS3:
                payload = getXdrMissingMetadataElements3(settings);
                break;
            case NEGATIVE_MISSING_METADATA_ELEMENTS4:
                payload = getXdrMissingMetadataElements4(settings);
                break;

            case NEGATIVE_MISSING_METADATA_ELEMENTS5:
                payload = getXdrMissingMetadataElements5(settings);
                break;
            case XDR_CCR:
                payload = getXdrCcr(settings);
                break;
            case XDR_C32:
                payload = getXdrC32(settings);
                break;
            case NEGATIVE_MISSING_ASSOCIATION:
                payload = getXdrMissingAssociation(settings);
                break;
            case TESTING_ONLY:
                payload = getXdrTestingOnly(settings);
        }

        return payload;

    }

    private static String getDeliveryStatusNotificationSuccessStandalone(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_DELIVERY_STATUS_NOTIFICATION_SUCCESS_STANDALONE);
        message = message.replaceAll("#DIRECT_RECIPIENT#", settings.getDirectFrom());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    private static String getDeliveryStatusNotificationFailureStandalone(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_DELIVERY_STATUS_NOTIFICATION_FAILURE_STANDALONE);
        message = message.replaceAll("#DIRECT_RECIPIENT#", settings.getDirectFrom());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    private static String generateVanillaXDR(String message, Settings settings) {
        message = message.replaceAll("#PATIENT_ID#", settings.getPatientId());
        return message;
    }

    // if messageId null or empty, creates one
    private static String getDeliveryStatusNotificationSuccess(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_DELIVERY_STATUS_NOTIFICATION_SUCCESS);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = message.replaceAll("#DIRECT_RELATESTO#", settings.getDirectRelatesTo());
        message = message.replaceAll("#DIRECT_RECIPIENT#", settings.getDirectRecipient());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    private static String getDeliveryStatusNotificationFailure(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_DELIVERY_STATUS_NOTIFICATION_FAILURE);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = message.replaceAll("#DIRECT_RELATESTO#", settings.getDirectRelatesTo());
        message = message.replaceAll("#DIRECT_RECIPIENT#", settings.getDirectRecipient());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrFullMetadata(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_FULL_METADATA);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }
    
    public static String getXdrSamlMetadata(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_SAML_METADATA);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrFullMetadataOnly(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_FULL_METADATA_ONLY);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMinimalMetadata(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_MINIMAL_METADATA);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMinimalMetadataOnly(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrTestingOnly(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_TESTING_ONLY);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrBadSoap(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_BAD_SOAP);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrBadSoapBody(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_BAD_SOAP_BODY);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingDirectBlock(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_DIRECT_BLOCK);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingMetadataElements1(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_METADATA_ELEMENTS1);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingMetadataElements2(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_METADATA_ELEMENTS2);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingMetadataElements3(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_METADATA_ELEMENTS3);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingMetadataElements4(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_METADATA_ELEMENTS4);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingMetadataElements5(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_METADATA_ELEMENTS5);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrCcr(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_CCR);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrC32(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_XDR_C32);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    public static String getXdrMissingAssociation(Settings settings) {
        makeSettingsSafe(settings);
        String message = getTemplate(FILENAME_MISSING_ASSOCIATION);
        message = setDirectAddressBlock(message, settings.getDirectTo(), settings.getDirectFrom());
        message = setSOAPHeaders(message, settings.getWsaTo());
        message = setIds(message, settings.getMessageId());

        return message;
    }

    private static String getTemplate(String resourceName) {

        InputStream is = ArtifactManagement.class.getClassLoader().getResourceAsStream(resourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line + "\r\n");
            }
            reader.close();
        } catch (IOException ioe) {
            //TODO
            System.err.println("RESOURCE NOT FOUND: " + resourceName);
            ioe.printStackTrace();
        }
        return out.toString();
    }

    public static String getIgnorePayload() {
        return getTemplate(FILENAME_IGNORE_PAYLOAD);
    }

    public static String getBaseEncodedCCDA() {
        return getTemplate(FILENAME_ENCODED_CCDA);
    }

    public static String getCCDA() {
        return getTemplate(FILENAME_CCDA);
    }

    public static String getBaseEncodedC32() {
        return getTemplate(FILENAME_XDR_C32_ENCODED);
    }

    public static String getBaseEncodedCCR() {
        return getTemplate(FILENAME_XDR_CCR_ENCODED);
    }

    private static String setIds(String metadata, String messageId, String documentId) {

        metadata = metadata.replaceAll("#MESSAGE_ID#", messageId);
        String entryUuid = UUID.randomUUID().toString();
        metadata = metadata.replaceAll("#ENTRY_UUID#", entryUuid);
        metadata = metadata.replaceAll("#DOCUMENT_ID#", documentId);
        long timestamp = Calendar.getInstance().getTimeInMillis();
        String uniqueId = NIST_OID_PREFIX + "." + timestamp;
        metadata = metadata.replaceAll("#UNIQUE_ID_SS#", uniqueId);
        metadata = metadata.replaceAll("#UNIQUE_ID#", Long.toString(timestamp));
        return metadata;

    }

    private static String setIds(
            String message,
            String messageId) {

        if (messageId == null || "".equals(messageId)) {
            messageId = UUID.randomUUID().toString();
        }
        message = message.replaceAll("#MESSAGE_ID#", messageId);
        String entryUuid = UUID.randomUUID().toString();
        message = message.replaceAll("#ENTRY_UUID#", entryUuid);
        String documentUuid = UUID.randomUUID().toString();
        message = message.replaceAll("#DOCUMENT_ID#", documentUuid);
        long timestamp = Calendar.getInstance().getTimeInMillis();
        String uniqueId = NIST_OID_PREFIX + "." + timestamp;
        message = message.replaceAll("#UNIQUE_ID_SS#", uniqueId);
        message = message.replaceAll("#UNIQUE_ID#", Long.toString(timestamp));
        return message;
    }

    private static String setDirectAddressBlock(String message, String directTo, String directFrom) {
        message = message.replaceAll("#DIRECT_TO#", directTo);
        message = message.replaceAll("#DIRECT_FROM#", directFrom);
        return message;
    }

    private static String setSOAPHeaders(String message, String wsaTo) {
        return message.replaceAll("#WSA_TO#", wsaTo);
    }

    private static void makeSettingsSafe(Settings settings) {
        if (settings == null) {
            settings = new Settings();
        }
        if (settings.getDirectFrom() == null) {
            settings.setDirectFrom("");
        }
        if (settings.getDirectRecipient() == null) {
            settings.setDirectRecipient("");
        }
        if (settings.getDirectRelatesTo() == null) {
            settings.setDirectRelatesTo("");
        }
        if (settings.getDirectTo() == null) {
            settings.setDirectTo("");
        }
        if (settings.getWsaTo() == null) {
            settings.setWsaTo("");
        }

    }

    // TODO: This is -- of course -- terrible.  Re-do this if we ever get some time.
    public static String generateDirectMessageBlock(Settings settings) {

        StringBuilder directBlock = new StringBuilder();
        directBlock.append("<direct:addressBlock xmlns:direct=\"urn:direct:addressing\" xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" ");
        directBlock.append("soapenv:role=\"urn:direct:addressing:destination\" ");
        directBlock.append("soapenv:relay=\"true\"> ");
        directBlock.append("<direct:from>" + settings.getDirectFrom() + "</direct:from> ");
        directBlock.append("<direct:to>" + settings.getDirectTo() + "</direct:to> ");
        String[] additionalDirectTo = settings.getAdditionalDirectTo();
        if (additionalDirectTo != null && additionalDirectTo.length > 0) {
            for (int i = 0; i < additionalDirectTo.length; i++) {
                directBlock.append("<direct:to>" + additionalDirectTo[i] + "</direct:to> ");
            }
        }
        String relatesTo = settings.getDirectRelatesTo();
        if (relatesTo != null && !relatesTo.isEmpty()) {
            directBlock.append("<direct:notification relatesTo=\"" + relatesTo + "\"/>");
        }

        String finalDestinationDelivery = settings.getFinalDestinationDelivery();
        if (finalDestinationDelivery != null && !finalDestinationDelivery.equals("")) {
            directBlock.append("<direct:X-DIRECT-FINAL-DESTINATION-DELIVERY>" + finalDestinationDelivery + "</direct:X-DIRECT-FINAL-DESTINATION-DELIVERY> ");

        }
        directBlock.append("</direct:addressBlock>");

        return directBlock.toString();

    }

    public static ArrayList<String> generateExtraHeaders(Settings settings, boolean full) {

        ArrayList<String> headers = new ArrayList<String>();
        if (full) {
            headers.add("<direct:metadata-level xmlns:direct=\"urn:direct:addressing\">XDS</direct:metadata-level>");
        } else {
            headers.add("<direct:metadata-level xmlns:direct=\"urn:direct:addressing\">minimal</direct:metadata-level>");
        }
        headers.add(generateDirectMessageBlock(settings));

        // Add the relatesTo header
        String relatesTo = settings.getDirectRelatesTo();
        if (relatesTo != null && !relatesTo.isEmpty()) {
            headers.add("<direct:notification xmlns:direct=\"urn:direct:addressing\" relatesTo=\"" + relatesTo + "\"/>");
        }
        return headers;

    }
    
    public static String generateSamlHeaders(Settings settings) {
    	return getTemplate("SamlHeaders.xml");
    }

    public static String removeXmlDeclaration(String xml) {

        int start = xml.indexOf("<?xml ");
        int end = xml.indexOf("?>");
        if (start != -1 && end != -1 && end > start) {
            xml = xml.substring(0, start) + xml.substring(end + 2);
        }
        return xml;
    }

    public static String escapeXml(String xml) {
        xml = xml.replace("<", "&lt;");
        xml = xml.replace(">", "&gt;");
        return xml;

    }

    public static Artifacts getVanillaXdr(Type type) {

        Artifacts artifacts = new Artifacts();

        switch (type) {
            case C32_FULL_1:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_1_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_1_METADATA));
                break;
            case C32_MINIMAL_1:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_1_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_1_METADATA));
                break;
            case C32_FULL_2:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_2_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_2_METADATA));
                break;
            case C32_MINIMAL_2:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_2_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_2_METADATA));
                break;
            case CCDA_AMBULATORY_FULL:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_METADATA));
                break;
            case CCDA_AMBULATORY_MINIMAL:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_METADATA));
                break;
            case CCDA_INPATIENT_FULL:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_METADATA));
                break;
            case CCDA_INPATIENT_MINIMAL:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_METADATA));
                break;
            case CCR_FULL_1:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_1_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_1_METADATA));
                break;
            case CCR_MINIMAL_1:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_1_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_1_METADATA));
                break;
            case CCR_FULL_2:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_2_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_2_METADATA));
                break;
            case CCR_MINIMAL_2:
                artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_2_DOC));
                artifacts.setMetadata(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_2_METADATA));
                break;
            default:
                break;
        }
        return artifacts;
    }

    public static Artifacts generateArtifacts(Type type, Settings settings) {

        Artifacts artifacts = new Artifacts();
        artifacts.setDocumentId("id_extrinsicobject"); //TODO
        if(settings != null) {
            if (settings.getMessageId() != null && !settings.getMessageId().isEmpty()) {
                artifacts.setMessageId(settings.getMessageId());
            } else {
                String messageId = UUID.randomUUID().toString();
                settings.setMessageId(messageId);
                artifacts.setMessageId(messageId);
            }
        }
        artifacts.setMimeType("text/xml"); // TODO: make this configurable
        String metadata = null;
        String payload = null;
        if(settings != null) {
            payload = settings.getPayload();
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        if (payload != null && !payload.isEmpty()) {
            System.out.println("Payload is not empty " + formattedDate);
            //       payload = ArtifactManagement.removeXmlDeclaration(payload);
            //       payload = ArtifactManagement.escapeXml(payload);
            /*
            if (!ArtifactManagement.isBase64Encoded(payload)) {
             //   System.out.println("!!!Payload is not base64encoded " + payload);                                                             
                try {
                    payload = Base64.getEncoder().encodeToString(payload.getBytes("utf-8"));                 
                  //  System.out.println("!!!now base64encoded " + payload);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ArtifactManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
              //  System.out.println("!!! Payload IS base64 encoded " + payload);
            }
             */
            artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
            artifacts.setDocument(payload);
            System.out.println("Setting doc to payload " + formattedDate);
            metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
        } else {
            System.out.println("PAYLOAD EMPTY " + formattedDate);
            switch (type) {
                case XDR_FULL_METADATA:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, true));
                    artifacts.setDocument(getCCDA());
                    metadata = getTemplate(FILENAME_XDR_FULL_METADATA_ONLY_NO_SOAP);
                    break;
                case XDR_SAML_METADATA:
                	ArrayList<String> samlHeaders = new ArrayList<String>();
                	samlHeaders.add(generateSamlHeaders(settings));
                    artifacts.setExtraHeaders(samlHeaders);
                    artifacts.setDocument(getCCDA());
                    metadata = getTemplate(FILENAME_XDR_SAML_METADATA);
                    break;
                case XDR_MINIMAL_METADATA:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    artifacts.setDocument(getCCDA());
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
                    break;
                case XDR_VANILLA:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    //artifacts.setDocument(getCCDA());                    
                    metadata = getTemplate(FILENAME_XDR_VANILLA);
                    metadata = generateVanillaXDR(metadata, settings);
                    break;
                case DELIVERY_STATUS_NOTIFICATION_SUCCESS:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    artifacts.setDocument(getDeliveryStatusNotificationSuccessStandalone(settings));
                    System.out.println("Setting artifact document to notification success");
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
                    break;
                case DELIVERY_STATUS_NOTIFICATION_FAILURE:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    artifacts.setDocument(getDeliveryStatusNotificationFailureStandalone(settings));
                    System.out.println("Setting artifact document to notification failure");
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
                    break;
                case XDR_C32:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    artifacts.setDocument(getBaseEncodedC32());
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
                    break;
                case XDR_CCR:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    artifacts.setDocument(getBaseEncodedCCR());
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP);
                    break;
                case NEGATIVE_MISSING_DIRECT_BLOCK:
                    artifacts.setExtraHeaders(new ArrayList<String>());
                    // artifacts.setDocument(getBaseEncodedCCDA());
                    artifacts.setDocument(getCCDA());
                    //artifacts.setMimeType("text/plain");
                    metadata = getTemplate(FILENAME_XDR_MINIMAL_METADATA_ONLY_NO_SOAP_NO_ROUTING);
                    break;
                case NEGATIVE_MISSING_ASSOCIATION:
                    artifacts.setExtraHeaders(generateExtraHeaders(settings, false));
                    // artifacts.setDocument(getBaseEncodedCCDA());
                    artifacts.setDocument(getIgnorePayload());
                    //artifacts.setMimeType("text/plain");
                    metadata = getTemplate(FILENAME_MISSING_ASSOCIATION_NO_SOAP);
                    break;

                case C32_FULL_1:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_1_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_1_METADATA);
                    break;
                case C32_MINIMAL_1:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_1_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_1_METADATA);
                    break;
                case C32_FULL_2:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_2_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_FULL_2_METADATA);
                    break;
                case C32_MINIMAL_2:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_2_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_C32_MINIMAL_2_METADATA);
                    break;
                case CCDA_AMBULATORY_FULL:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_FULL_METADATA);
                    break;
                case CCDA_AMBULATORY_MINIMAL:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_AMBULATORY_MINIMAL_METADATA);
                    break;
                case CCDA_INPATIENT_FULL:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_FULL_METADATA);
                    break;
                case CCDA_INPATIENT_MINIMAL:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCDA_INPATIENT_MINIMAL_METADATA);
                    break;
                case CCR_FULL_1:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_1_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_1_METADATA);
                    break;
                case CCR_MINIMAL_1:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_1_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_1_METADATA);
                    break;
                case CCR_FULL_2:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_2_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_FULL_2_METADATA);
                    break;
                case CCR_MINIMAL_2:
                    artifacts.setDocument(ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_2_DOC));
                    metadata = ArtifactManagement.getTemplate(FILENAME_XDR_VANILLA_CCR_MINIMAL_2_METADATA);
                    break;

                default:
                    throw new UnsupportedOperationException("not yet, guys");
            }
        }
        metadata = setIds(metadata, artifacts.getMessageId(), artifacts.getDocumentId());
        artifacts.setMetadata(metadata);

        return artifacts;
    }

    private static boolean isBase64Encoded(String stringBase64) {
        String base64Regex = "([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)";
        Pattern pattern = Pattern.compile(base64Regex);
        if (pattern.matcher(stringBase64).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public final static void main(String args[]) {

        try {

            Settings settings = new Settings();
            settings.setDirectFrom("directFrom");
            settings.setDirectTo("directTo");
            settings.setWsaTo("wsaTo");
            settings.setDirectRelatesTo("MESSAGEID1");
            //    settings.setPayload("THIS IS MY PAYLOAD IN BASE64!!!");
            //  settings.setPayload("VEhJUyBJUyBNWSBQQVlMT0FEIElOIEJBU0U2NCEhIQ==");
            String[] directTos = {};
            settings.setAdditionalDirectTo(directTos);
            //   String payload = getPayload(Type.XDR_MINIMAL_METADATA, settings);
            // System.out.println("here!\n" + payload);
            //    URL url = ClassLoader.getSystemResource("DeliveryStatusNotification_success.xml");
            //  System.out.println(url.getPath());
            /*    
             System.out.println(getDeliveryStatusNotificationSuccess("/home/mccaffrey/xdr/DeliveryStatusNotification_success.xml",
             "directTo",
             "directFrom",
             "relatesTo",
             "recipient",
             "wsaTo",
             null));
             */
            //       Artifacts art = ArtifactManagement.generateArtifacts(Type.NEGATIVE_MISSING_ASSOCIATION, settings);
//            Artifacts art = ArtifactManagement.generateArtifacts(Type.NEGATIVE_BAD_SOAP_HEADER, settings);

//Artifacts art = ArtifactManagement.generateArtifacts(Type.DELIVERY_STATUS_NOTIFICATION_FAILURE, settings);
//Artifacts art = ArtifactManagement.generateArtifacts(Type.NEGATIVE_MISSING_DIRECT_BLOCK, settings);
//Artifacts art = ArtifactManagement.generateArtifacts(Type.XDR_FULL_METADATA, settings);
//Artifacts art = ArtifactManagement.generateArtifacts(Type.XDR_MINIMAL_METADATA, settings);
//Artifacts art = ArtifactManagement.generateArtifacts(Type.NEGATIVE_BAD_SOAP_HEADER, settings);
            /*      settings.setPatientId("909");
            Artifacts art = ArtifactManagement.generateArtifacts(Type.XDR_VANILLA, settings);

            System.out.println("docId = " + art.getDocumentId());
            System.out.println("headers = " + art.getExtraHeaders());
            System.out.println("messageId = " + art.getMessageId());
            System.out.println("metadata = " + art.getMetadata());
            System.out.println("mimetype = " + art.getMimeType());
            System.out.println("document = " + art.getDocument());

            String testXMl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"CDA.xsl\"?>\n"
                    + "<ClinicalDocument>\n"
                    + "hello world\n"
                    + "</ClinicalDocument>";

            //     System.out.println(testXMl + "\n\n");
//            System.out.println(ArtifactManagement.escapeXml(testXMl));
             */
         //   Artifacts art = ArtifactManagement.getVanillaXdr(Type.CCR_MINIMAL_2);

            Artifacts art = ArtifactManagement.generateArtifacts(Type.CCR_MINIMAL_2,null);
         
            System.out.println(art.getDocument());
            System.out.println("\n\n\n---------------------------\n\n\n");
            System.out.println(art.getMetadata());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
