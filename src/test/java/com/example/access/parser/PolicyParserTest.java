package com.example.access.parser;

import cn.edu.buaa.crypto.access.AccessControlEngine;
import cn.edu.buaa.crypto.access.AccessControlParameter;
import cn.edu.buaa.crypto.access.UnsatisfiedAccessControlException;
import cn.edu.buaa.crypto.access.lsss.lsw10.LSSSLW10Engine;
import cn.edu.buaa.crypto.access.parser.ParserUtils;
import cn.edu.buaa.crypto.access.parser.PolicySyntaxException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.util.Map;

/**
 * Created by Weiran Liu on 2016/7/20.
 */
public class PolicyParserTest {
    private static String access_policy_example_1 = "school:pku and professor and (academy:software or academy:computer)";
    private static String[] access_policy_exampe_1_satisfied_1 = new String[] {"school:pku", "professor", "academy:software"};
    private static String[] access_policy_exampe_1_satisfied_2 = new String[] {"school:pku", "professor", "academy:software", "academy:computer"};
    private static String[] access_policy_exampe_1_unsatisfied_1 = new String[] {"professor", "academy:software", "academy:computer"};

    private static String access_policy_example_2 = "(school:pku and academy:software) or (school:mit and academy:computer)";
    private static String[] access_policy_exampe_2_satisfied_1 = new String[] {"school:pku", "academy:software"};
    private static String[] access_policy_exampe_2_satisfied_2 = new String[] {"school:pku", "school:mit", "academy:computer"};
    private static String[] access_policy_exampe_2_unsatisfied_1 = new String[] {"school:pku"};
    private static String[] access_policy_exampe_2_unsatisfied_2 = new String[] {"school:pku", "academy:computer"};

    private static String access_policy_example_3 =
            "A_00 and A_01 and A_02 and A_03 and A_04 and A_05 and A_06 and A_07 and A_08 and A_09 and " +
            "A_10 and A_11 and A_12 and A_13 and A_14 and A_15 and A_16 and A_17 and A_18 and A_19 and " +
            "A_20 and A_21 and A_22 and A_23 and A_24 and A_25 and A_26 and A_27 and A_28 and A_29 and " +
            "A_30 and A_31 and A_32 and A_33 and A_34 and A_35 and A_36 and A_37 and A_38 and A_39 and " +
            "A_40 and A_41 and A_42 and A_43 and A_44 and A_45 and A_46 and A_47 and A_48 and A_49";
    private static String[] access_policy_exampe_3_satisfied_1 = new String[] {
            "A_00", "A_01", "A_02", "A_03", "A_04", "A_05", "A_06", "A_07", "A_08", "A_09",
            "A_10", "A_11", "A_12", "A_13", "A_14", "A_15", "A_16", "A_17", "A_18", "A_19",
            "A_20", "A_21", "A_22", "A_23", "A_24", "A_25", "A_26", "A_27", "A_28", "A_29",
            "A_30", "A_31", "A_32", "A_33", "A_34", "A_35", "A_36", "A_37", "A_38", "A_39",
            "A_40", "A_41", "A_42", "A_43", "A_44", "A_45", "A_46", "A_47", "A_48", "A_49",
    };
    private static String[] access_policy_exampe_3_unsatisfied_1 = new String[] {
            "A_00", "A_01", "A_02", "A_03", "A_04", "A_05", "A_06", "A_07", "A_08", "A_09",
            "A_10", "A_11", "A_12", "A_13", "A_14", "A_15", "A_16", "A_17", "A_18", "A_19",
            "A_20", "A_21", "A_22", "A_23", "A_24", "A_25", "A_26", "A_27", "A_28", "A_29",
            "A_30", "A_31", "A_32", "A_33", "A_34", "A_35", "A_36", "A_37", "A_38", "A_39",
            "A_40", "A_41", "A_42", "A_43", "A_44", "A_45", "A_46", "A_47", "A_48",
    };
    private static String[] access_policy_exampe_3_unsatisfied_2 = new String[] {
                                                "A_04", "A_05", "A_06", "A_07", "A_08", "A_09",
            "A_10", "A_11", "A_12", "A_13", "A_14", "A_15", "A_16", "A_17", "A_18", "A_19",
            "A_20", "A_21", "A_22", "A_23", "A_24", "A_25", "A_26", "A_27", "A_28", "A_29",
            "A_30", "A_31", "A_32", "A_33", "A_34",          "A_37", "A_38", "A_39",
            "A_40", "A_41", "A_42", "A_43", "A_44", "A_45", "A_46", "A_47", "A_48", "A_49",
    };

    private AccessControlEngine accessControlEngine;
    private Pairing pairing;

    public PolicyParserTest() {
        this.accessControlEngine = LSSSLW10Engine.getInstance();
        TypeACurveGenerator pg = new TypeACurveGenerator(160, 512);
        PairingParameters typeAParams = pg.generate();
        this.pairing = PairingFactory.getPairing(typeAParams);
    }

    private void test_valid_access_policy(int testIndex, String accessPolicyString, final String[] attributeSet) {
        try {
            int[][] accessPolicy = ParserUtils.GenerateAccessPolicy(accessPolicyString);
//            for (int i = 0; i < accessPolicy.length; i++) {
//                for (int j = 0 ; j < accessPolicy[i].length; j++) {
//                    System.out.print(accessPolicy[i][j] + ", ");
//                }
//                System.out.println();
//            }
//            System.out.println();
            String[] rhos = ParserUtils.GenerateRhos(accessPolicyString);
            //Access Policy Generation
            AccessControlParameter accessControlParameter = accessControlEngine.generateAccessControl(accessPolicy, rhos);
            //SecretSharing
            Element secret = pairing.getZr().newRandomElement().getImmutable();
//        System.out.println("Generated Secret s = " + secret);
            Map<String, Element> lambdaElementsMap = accessControlEngine.secretSharing(pairing, secret, accessControlParameter);
            //Secret Reconstruction
            Map<String, Element> omegaElementsMap = accessControlEngine.reconstructOmegas(pairing, attributeSet, accessControlParameter);
            Element reconstructedSecret = pairing.getZr().newZeroElement().getImmutable();
            for (int i = 0; i < attributeSet.length; i++) {
                if (omegaElementsMap.containsKey(attributeSet[i])) {
                    reconstructedSecret = reconstructedSecret.add(lambdaElementsMap.get(attributeSet[i]).mulZn(omegaElementsMap.get(attributeSet[i]))).getImmutable();
                }
            }
//        System.out.println("Reconstruct Secret s = " + reconstructedSecret);
            if (!reconstructedSecret.equals(secret)) {
                System.out.println("Access Policy with Combined Gate Satisfied Test + " + testIndex + ", Reconstructed Secret Wrong...");
//                System.exit(0);
            } else {
                System.out.println("Access Policy with Combined Gate Satisfied Test + " + testIndex + " Passed.");
            }
        } catch (UnsatisfiedAccessControlException e) {
            System.out.println("Access Policy with Combined Gate Satisfied Test + " + testIndex + ", Error for getting Exceptions...");
            e.printStackTrace();
            System.exit(0);
        } catch (PolicySyntaxException e) {
            System.out.println("Access Policy with Combined Gate Satisfied Test + " + testIndex + ", Error for parsing...");
            e.printStackTrace();
        }
    }

    private void test_invalid_access_policy(int testIndex, String accessPolicyString, final String[] attributeSet) {
        try {
            int[][] accessPolicy = ParserUtils.GenerateAccessPolicy(accessPolicyString);
            String[] rhos = ParserUtils.GenerateRhos(accessPolicyString);
            //Access Policy Generation
            AccessControlParameter accessControlParameter = accessControlEngine.generateAccessControl(accessPolicy, rhos);
            //SecretSharing
            Element secret = pairing.getZr().newRandomElement().getImmutable();
//        System.out.println("Generated Secret s = " + secret);
            Map<String, Element> lambdaElementsMap = accessControlEngine.secretSharing(pairing, secret, accessControlParameter);
            //Secret Reconstruction
            Map<String, Element> omegaElementsMap = accessControlEngine.reconstructOmegas(pairing, attributeSet, accessControlParameter);
            Element reconstructedSecret = pairing.getZr().newZeroElement().getImmutable();
            for (int i = 0; i < attributeSet.length; i++) {
                if (omegaElementsMap.containsKey(attributeSet[i])) {
                    reconstructedSecret = reconstructedSecret.add(lambdaElementsMap.get(attributeSet[i]).mulZn(omegaElementsMap.get(attributeSet[i]))).getImmutable();
                }
            }
            System.out.println("Access Policy with Combined Gate Unsatisfied Test " + testIndex + ", Error for not getting Exceptions...");
            System.exit(0);
        } catch (UnsatisfiedAccessControlException e) {
            System.out.println("Access Policy with Combined Gate Unsatisfied Test " + testIndex + " Passed.");
        } catch (PolicySyntaxException e) {
            System.out.println("Access Policy with Combined Gate Satisfied Test + " + testIndex + ", Error for parsing...");
            e.printStackTrace();
        }
    }

    public void testPolicyParser() {
        test_valid_access_policy(1, access_policy_example_1, access_policy_exampe_1_satisfied_1);
        test_valid_access_policy(2, access_policy_example_1, access_policy_exampe_1_satisfied_2);
        test_valid_access_policy(11, access_policy_example_2, access_policy_exampe_2_satisfied_1);
        test_valid_access_policy(12, access_policy_example_2, access_policy_exampe_2_satisfied_2);
        test_valid_access_policy(21, access_policy_example_3, access_policy_exampe_3_satisfied_1);

        test_invalid_access_policy(1, access_policy_example_1, access_policy_exampe_1_unsatisfied_1);
        test_invalid_access_policy(11, access_policy_example_2, access_policy_exampe_2_unsatisfied_1);
        test_invalid_access_policy(12, access_policy_example_2, access_policy_exampe_2_unsatisfied_2);
        test_invalid_access_policy(21, access_policy_example_3, access_policy_exampe_3_unsatisfied_1);
        test_invalid_access_policy(22, access_policy_example_3, access_policy_exampe_3_unsatisfied_2);
    }

    public static void main(String[] args) {
        PolicyParserTest policyParserTest = new PolicyParserTest();
        policyParserTest.testPolicyParser();
    }
}
