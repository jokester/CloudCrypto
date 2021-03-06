package cn.edu.buaa.crypto.application.llw15.params;

import org.bouncycastle.crypto.CipherParameters;

import java.util.Arrays;

/**
 * Created by Weiran Liu on 16/5/19.
 */
public class RBACLLW15EncapsulationGenParameters implements CipherParameters {
    private RBACLLW15PublicKeyParameters publicKeyParameters;
    private String id;
    private String[] roles;
    private String time;

    public RBACLLW15EncapsulationGenParameters(
            CipherParameters publicKeyParameters, String id, String[] roles, String time) {
        this.publicKeyParameters = (RBACLLW15PublicKeyParameters)publicKeyParameters;
        assert(roles.length == this.publicKeyParameters.getMaxRoleNumber());
        this.roles = Arrays.copyOf(roles, roles.length);
        this.id = id;
        this.time = time;
    }

    public RBACLLW15PublicKeyParameters getPublicKeyParameters() {
        return this.publicKeyParameters;
    }

    public String[] getRoles() { return Arrays.copyOf(roles, roles.length); }

    public String getRoleAt(int index) { return this.roles[index]; }

    public String getId() { return this.id; }

    public String getTime() { return this.time; }
}
