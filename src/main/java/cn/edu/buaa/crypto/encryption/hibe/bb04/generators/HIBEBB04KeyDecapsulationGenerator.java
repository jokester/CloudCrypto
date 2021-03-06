package cn.edu.buaa.crypto.encryption.hibe.bb04.generators;

import cn.edu.buaa.crypto.Utils;
import cn.edu.buaa.crypto.encryption.hibe.bb04.params.*;
import cn.edu.buaa.crypto.pairingkem.generators.PairingKeyDecapsulationGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.util.Arrays;

/**
 * Created by Weiran Liu on 15-10-1.
 */
public class HIBEBB04KeyDecapsulationGenerator implements PairingKeyDecapsulationGenerator {
    private HIBEBB04DecapsulationParameters params;

    public void init(CipherParameters params) {
        this.params = (HIBEBB04DecapsulationParameters)params;
    }

    public byte[] recoverKey() throws InvalidCipherTextException {
        HIBEBB04PublicKeyParameters publicKeyParameters = this.params.getPublicKeyParameters();
        HIBEBB04SecretKeyParameters secretKeyParameters = this.params.getSecretKeyParameters();
        HIBEBB04CiphertextParameters ciphertextParameters = this.params.getCiphertextParameters();

        int secretKeyLength = secretKeyParameters.getLength();
        int ciphertextLength = ciphertextParameters.getLength();

        Pairing pairing = PairingFactory.getPairing(publicKeyParameters.getParameters());
        Element[] elementIdsCT = Utils.MapToZr(pairing, this.params.getIds());

        if (ciphertextLength < secretKeyLength) {
            throw new InvalidCipherTextException("Secret Key length is longer than Ciphertext length");
        }

        for (int i=0; i<ciphertextLength && i<secretKeyLength; i++){
            if (!secretKeyParameters.getElementIdAt(i).equals(elementIdsCT[i])){
                throw new InvalidCipherTextException("Secret Key identity vector does not match Ciphertext identity vector");
            }
        }

        Element d0 = secretKeyParameters.getD0().getImmutable();
        Element B = ciphertextParameters.getB().getImmutable();
        Element[] Cs = new Element[ciphertextParameters.getLength()];
        Element[] ds = new Element[ciphertextParameters.getLength()];

        Element temp1 = pairing.getGT().newOneElement().getImmutable();
        for (int i=0; i<ciphertextLength; i++){
            Cs[i] = ciphertextParameters.getCsAt(i).getImmutable();
            if (i < secretKeyLength) {
                ds[i] = secretKeyParameters.getDsAt(i).getImmutable();
            } else {
                d0 = d0.mul(publicKeyParameters.getG1().powZn(elementIdsCT[i]).mul(publicKeyParameters.getHsAt(i))).getImmutable();
                ds[i] = publicKeyParameters.getG().getImmutable();
            }
            temp1 = temp1.mul(pairing.pairing(Cs[i], ds[i])).getImmutable();
        }
        Element temp0 = pairing.pairing(B, d0).getImmutable();
        Element sessionKey = temp0.div(temp1).getImmutable();
        byte[] byteArraySessionKey = sessionKey.toBytes();
        return Arrays.copyOf(byteArraySessionKey, byteArraySessionKey.length);
    }
}
