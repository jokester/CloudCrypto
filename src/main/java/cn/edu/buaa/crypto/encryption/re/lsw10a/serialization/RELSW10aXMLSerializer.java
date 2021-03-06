package cn.edu.buaa.crypto.encryption.re.lsw10a.serialization;

import cn.edu.buaa.crypto.SerializationUtils;
import cn.edu.buaa.crypto.Utils;
import cn.edu.buaa.crypto.encryption.re.lsw10a.RELSW10aEngine;
import cn.edu.buaa.crypto.encryption.re.lsw10a.params.RELSW10aCiphertextParameters;
import cn.edu.buaa.crypto.encryption.re.lsw10a.params.RELSW10aMasterSecretKeyParameters;
import cn.edu.buaa.crypto.encryption.re.lsw10a.params.RELSW10aPublicKeyParameters;
import cn.edu.buaa.crypto.encryption.re.lsw10a.params.RELSW10aSecretKeyParameters;
import cn.edu.buaa.crypto.pairingkem.serialization.PairingParameterXMLSerializer;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.encoders.Hex;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.security.InvalidParameterException;

/**
 * Created by Weiran Liu on 2016/4/4.
 */
public class RELSW10aXMLSerializer implements PairingParameterXMLSerializer {

    private static final String TAG_SCHEME_NAME = RELSW10aEngine.SCHEME_NAME;

    //Tags for public key
    private static final String TAG_PK_G = "G";
    private static final String TAG_PK_Gb = "Gb";
    private static final String TAG_PK_Gb2 = "Gb2";
    private static final String TAG_PK_Hb = "Hb";
    private static final String TAG_PK_EggAlpha = "EggAlpha";

    //Tags for master secret key
    private static final String TAG_MSK_ALPHA = "Alpha";
    private static final String TAG_MSK_B = "b";
    private static final String TAG_MSK_H = "h";

    //Tags for secret key
    private static final String TAG_SK_ID = "id";
    private static final String TAG_SK_D0 = "d0";
    private static final String TAG_SK_D1 = "d1";
    private static final String TAG_SK_D2 = "d2";

    //Tags for ciphertexts
    private static final String TAG_CT_C0 = "C0";
    private static final String TAG_CT_C1S = "C1s";
    private static final String TAG_CT_C1I = "C1i";
    private static final String TAG_CT_C2S = "C2s";
    private static final String TAG_CT_C2I = "C2i";

    private static final RELSW10aXMLSerializer INSTANCE = new RELSW10aXMLSerializer();

    private RELSW10aXMLSerializer() { }

    public static RELSW10aXMLSerializer getInstance(){
        return INSTANCE;
    }

    public Document documentSerialization(CipherParameters cipherParameters) {
        if (cipherParameters instanceof RELSW10aPublicKeyParameters) {
            return getInstance().publicKeyParametersSerialization((RELSW10aPublicKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof RELSW10aMasterSecretKeyParameters) {
            return getInstance().masterSecretKeyParametersSerialization((RELSW10aMasterSecretKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof RELSW10aSecretKeyParameters) {
            return getInstance().secretKeyParametersSerialization((RELSW10aSecretKeyParameters) cipherParameters);
        } else if (cipherParameters instanceof RELSW10aCiphertextParameters) {
            return getInstance().ciphertextParametersSerialization((RELSW10aCiphertextParameters) cipherParameters);
        } else {
            throw new InvalidParameterException("Invalid CipherParameter Instance of " + RELSW10aEngine.SCHEME_NAME
                    + " Scheme, find" + cipherParameters.getClass().getName());
        }
    }

    private Document publicKeyParametersSerialization(RELSW10aPublicKeyParameters publicKeyParameters){
        try {
            Document publicKeyParametersDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = publicKeyParametersDocument.createElement(RELSW10aXMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, PairingParameterXMLSerializer.TYPE_PK);
            publicKeyParametersDocument.appendChild(schemeElement);
            //Set g
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_G, publicKeyParameters.getG());
            //Set gb
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_Gb, publicKeyParameters.getGb());
            //Set gb2
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_Gb2, publicKeyParameters.getGb2());
            //Set hb
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_Hb, publicKeyParameters.getHb());
            //Set eggAlpha
            SerializationUtils.SetElement(publicKeyParametersDocument, schemeElement, TAG_PK_EggAlpha, publicKeyParameters.getEggAlpha());
            return publicKeyParametersDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document masterSecretKeyParametersSerialization(RELSW10aMasterSecretKeyParameters masterSecretKeyParameters) {
        try {
            Document masterSecretKeyDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = masterSecretKeyDocument.createElement(RELSW10aXMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, PairingParameterXMLSerializer.TYPE_MSK);
            masterSecretKeyDocument.appendChild(schemeElement);
            //Set alpha
            SerializationUtils.SetElement(masterSecretKeyDocument, schemeElement, TAG_MSK_ALPHA, masterSecretKeyParameters.getAlpha());
            //Set b
            SerializationUtils.SetElement(masterSecretKeyDocument, schemeElement, TAG_MSK_B, masterSecretKeyParameters.getB());
            //Set h
            SerializationUtils.SetElement(masterSecretKeyDocument, schemeElement, TAG_MSK_H, masterSecretKeyParameters.getH());
            return masterSecretKeyDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document secretKeyParametersSerialization(RELSW10aSecretKeyParameters secretKeyParameters){
        try {
            Document secretKeyDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = secretKeyDocument.createElement(RELSW10aXMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, PairingParameterXMLSerializer.TYPE_SK);
            secretKeyDocument.appendChild(schemeElement);
            //Set id
            SerializationUtils.SetString(secretKeyDocument, schemeElement, TAG_SK_ID, secretKeyParameters.getId());
            //Set d0
            SerializationUtils.SetElement(secretKeyDocument, schemeElement, TAG_SK_D0, secretKeyParameters.getD0());
            //Set d1
            SerializationUtils.SetElement(secretKeyDocument, schemeElement, TAG_SK_D1, secretKeyParameters.getD1());
            //Set d2
            SerializationUtils.SetElement(secretKeyDocument, schemeElement, TAG_SK_D2, secretKeyParameters.getD2());
            return secretKeyDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document ciphertextParametersSerialization(RELSW10aCiphertextParameters ciphertextParameters){
        try {
            Document ciphertextDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element schemeElement = ciphertextDocument.createElement(RELSW10aXMLSerializer.TAG_SCHEME_NAME);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_TYPE, PairingParameterXMLSerializer.TYPE_CT);
            schemeElement.setAttribute(PairingParameterXMLSerializer.ATTRI_LENGTH, Integer.toString(ciphertextParameters.getLength()));
            ciphertextDocument.appendChild(schemeElement);
            //Set C0
            SerializationUtils.SetElement(ciphertextDocument, schemeElement, TAG_CT_C0, ciphertextParameters.getC0());
            //Set C1s
            SerializationUtils.SetElementArray(ciphertextDocument, schemeElement, TAG_CT_C1S, TAG_CT_C1I, ciphertextParameters.getC1s());
            //Set C2s
            SerializationUtils.SetElementArray(ciphertextDocument, schemeElement, TAG_CT_C2S, TAG_CT_C2I, ciphertextParameters.getC2s());
            return ciphertextDocument;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CipherParameters documentDeserialization(PairingParameters pairingParameters, Document document) {
        Element schemeElement = document.getDocumentElement();
        String cipherParameterType = schemeElement.getAttribute(PairingParameterXMLSerializer.ATTRI_TYPE);
        if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_PK)){
            return getInstance().publicKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_MSK)){
            return getInstance().masterSecretKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_SK)) {
            return getInstance().secretKeyParametersDeserialization(pairingParameters, schemeElement);
        } else if (cipherParameterType.equals(PairingParameterXMLSerializer.TYPE_CT)) {
            return getInstance().ciphertextParametersDeserialization(pairingParameters, schemeElement);
        } else {
            throw new InvalidParameterException("Illegal " + RELSW10aEngine.SCHEME_NAME +
                    " Document Type, find " + cipherParameterType);
        }
    }

    private CipherParameters publicKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element g = null;
        it.unisa.dia.gas.jpbc.Element gb = null;
        it.unisa.dia.gas.jpbc.Element gb2 = null;
        it.unisa.dia.gas.jpbc.Element hb = null;
        it.unisa.dia.gas.jpbc.Element eggAlpha = null;
        for (int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_PK_G)) {
                //Set g
                String gString = node.getFirstChild().getNodeValue();
                g = pairing.getG1().newElementFromBytes(Hex.decode(gString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_PK_Gb)) {
                //Set gb
                String gbString = node.getFirstChild().getNodeValue();
                gb = pairing.getG1().newElementFromBytes(Hex.decode(gbString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_PK_Gb2)) {
                //Set gb2
                String gb2String = node.getFirstChild().getNodeValue();
                gb2 = pairing.getG1().newElementFromBytes(Hex.decode(gb2String)).getImmutable();
            } else if (node.getNodeName().equals(TAG_PK_Hb)) {
                //Set hb
                String hbString = node.getFirstChild().getNodeValue();
                hb = pairing.getG1().newElementFromBytes(Hex.decode(hbString)).getImmutable();
            }else if (node.getNodeName().equals(TAG_PK_EggAlpha)) {
                //Set eggAlpha
                String eggAlphaString = node.getFirstChild().getNodeValue();
                eggAlpha = pairing.getG1().newElementFromBytes(Hex.decode(eggAlphaString)).getImmutable();
            }
        }
        return new RELSW10aPublicKeyParameters(pairingParameters, g, gb, gb2, hb, eggAlpha);
    }

    private CipherParameters masterSecretKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement){
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element alpha = null;
        it.unisa.dia.gas.jpbc.Element b = null;
        it.unisa.dia.gas.jpbc.Element h = null;
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            //Set alpha
            if (node.getNodeName().equals(TAG_MSK_ALPHA)) {
                String alphaString = node.getFirstChild().getNodeValue();
                alpha = pairing.getZr().newElementFromBytes(Hex.decode(alphaString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_MSK_B)) {
                String bString = node.getFirstChild().getNodeValue();
                b = pairing.getZr().newElementFromBytes(Hex.decode(bString)).getImmutable();
            } else if (node.getNodeName().equals(TAG_MSK_H)) {
                String hString = node.getFirstChild().getNodeValue();
                h = pairing.getG1().newElementFromBytes(Hex.decode(hString)).getImmutable();
            }
        }
        return new RELSW10aMasterSecretKeyParameters(pairingParameters, alpha, b, h);
    }

    private CipherParameters secretKeyParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        NodeList nodeList = schemeElement.getChildNodes();
        String id = null;
        it.unisa.dia.gas.jpbc.Element d0 = null;
        it.unisa.dia.gas.jpbc.Element d1 = null;
        it.unisa.dia.gas.jpbc.Element d2 = null;
        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_SK_ID)) {
                id = node.getFirstChild().getNodeValue();
            } else if (node.getNodeName().equals(TAG_SK_D0)) {
                //Set d0
                String d0String = node.getFirstChild().getNodeValue();
                d0 = pairing.getG1().newElementFromBytes(Hex.decode(d0String)).getImmutable();
            } else if (node.getNodeName().equals(TAG_SK_D1)) {
                //Set d1
                String d1String = node.getFirstChild().getNodeValue();
                d1 = pairing.getG1().newElementFromBytes(Hex.decode(d1String)).getImmutable();
            } else if (node.getNodeName().equals(TAG_SK_D2)) {
                //Set d2
                String d2String = node.getFirstChild().getNodeValue();
                d2 = pairing.getG1().newElementFromBytes(Hex.decode(d2String)).getImmutable();
            }
        }
        it.unisa.dia.gas.jpbc.Element elementId = Utils.MapToZr(pairing, id);
        return new RELSW10aSecretKeyParameters(pairingParameters, id, elementId, d0, d1, d2);
    }

    private CipherParameters ciphertextParametersDeserialization(PairingParameters pairingParameters, Element schemeElement) {
        Pairing pairing = PairingFactory.getPairing(pairingParameters);
        int length = Integer.valueOf(schemeElement.getAttribute(PairingParameterXMLSerializer.ATTRI_LENGTH));
        NodeList nodeList = schemeElement.getChildNodes();
        it.unisa.dia.gas.jpbc.Element C0 = null;
        it.unisa.dia.gas.jpbc.Element[] C1s = new it.unisa.dia.gas.jpbc.Element[length];
        it.unisa.dia.gas.jpbc.Element[] C2s = new it.unisa.dia.gas.jpbc.Element[length];
        for (int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(TAG_CT_C0)) {
                //Set C0
                String c0String = node.getFirstChild().getNodeValue();
                C0 = pairing.getG1().newElementFromBytes(Hex.decode(c0String)).getImmutable();
            } else if (node.getNodeName().equals(TAG_CT_C1S)) {
                //Set C1s
                NodeList nodeHsList = ((Element) node).getElementsByTagName(TAG_CT_C1I);
                for (int j=0; j<nodeHsList.getLength(); j++) {
                    Element elementC1i = (Element) nodeHsList.item(j);
                    int index = Integer.valueOf(elementC1i.getAttribute(PairingParameterXMLSerializer.ATTRI_INDEX));
                    String c1iString = elementC1i.getFirstChild().getNodeValue();
                    C1s[index] = pairing.getG1().newElementFromBytes(Hex.decode(c1iString)).getImmutable();
                }
            } else if (node.getNodeName().equals(TAG_CT_C2S)) {
                //Set C2s
                NodeList nodeHsList = ((Element) node).getElementsByTagName(TAG_CT_C2I);
                for (int j=0; j<nodeHsList.getLength(); j++) {
                    Element elementC2i = (Element) nodeHsList.item(j);
                    int index = Integer.valueOf(elementC2i.getAttribute(PairingParameterXMLSerializer.ATTRI_INDEX));
                    String c2iString = elementC2i.getFirstChild().getNodeValue();
                    C2s[index] = pairing.getG1().newElementFromBytes(Hex.decode(c2iString)).getImmutable();
                }
            }
        }
        return new RELSW10aCiphertextParameters(pairingParameters,length, C0, C1s, C2s);
    }
}
