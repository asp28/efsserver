package uk.co.ankeetpatel.encryptedfilesystem.efsserver.ACL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LocalPermissionService {

    @Autowired
    private MutableAclService aclService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void updatePermissionForUser(File targetObj, Permission permission, String username, boolean permissionType) {
        final Sid sid = new PrincipalSid(username);
        addPermissionForSid(targetObj, permission, sid, permissionType);
    }

    public void updatePermissionForAuthority(File targetObj, Permission permission, String authority, boolean permissionType) {
        final Sid sid = new GrantedAuthoritySid(authority);
        addPermissionForSid(targetObj, permission, sid, permissionType);
    }

    private void addPermissionForSid(File targetObj, Permission permission, Sid sid, boolean permissionType) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);

        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

                MutableAcl acl = null;
                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }

                acl.insertAce(acl.getEntries().size(), permission, sid, permissionType);
                aclService.updateAcl(acl);
            }
        });
    }

    public List<Integer> getPermissions(File targetObj, Authentication authentication) {
        final ObjectIdentity oid = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

        SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
        List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
        // Lookup only ACLs for SIDs we're interested in
        Acl acl = aclService.readAclById(oid, sids);
        List<AccessControlEntry> aces = acl.getEntries();
        List<Integer> permissionsList = new ArrayList<>();
        for (AccessControlEntry ace : aces ) {
            if(sids.contains(ace.getSid())) {
                if(!permissionsList.contains(ace.getPermission().getMask())) {
                    permissionsList.add(ace.getPermission().getMask());
                }
            }


        }
        return permissionsList;
    }

}
